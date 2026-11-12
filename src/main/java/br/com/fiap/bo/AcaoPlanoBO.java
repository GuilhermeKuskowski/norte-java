package br.com.fiap.bo;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import br.com.fiap.dao.ConnectionFactory;
import br.com.fiap.dao.AcaoPlanoDAO;
import br.com.fiap.dao.LeiDAO;
import br.com.fiap.dao.PlanoAcaoDAO;
import br.com.fiap.exception.BancoDeDadosException;
import br.com.fiap.exception.ConflitoException;
import br.com.fiap.exception.RecursoNaoEncontradoException;
import br.com.fiap.exception.RegraDeNegocioException;
import br.com.fiap.model.AcaoPlano;

/**
 * Regras de negocio dos passos do plano de acao.
 * O semaforo nao e escolhido pelo usuario: e calculado a partir do prazo.
 */
@Service
public class AcaoPlanoBO {

    /** Ate quantos dias antes do prazo o passo fica AMARELO. */
    public static final int DIAS_ALERTA = 3;

    private final ConnectionFactory factory;

    public AcaoPlanoBO(ConnectionFactory factory) {
        this.factory = factory;
    }

    /**
     * Regra do semaforo:
     * concluida ou sem prazo = VERDE; prazo vencido = VERMELHO;
     * faltam ate 3 dias = AMARELO; senao VERDE.
     */
    public static String calcularSemaforo(LocalDate prazo, boolean concluida, LocalDate hoje) {
        if (concluida || prazo == null) {
            return "VERDE";
        }
        if (prazo.isBefore(hoje)) {
            return "VERMELHO";
        }
        if (!prazo.isAfter(hoje.plusDays(DIAS_ALERTA))) {
            return "AMARELO";
        }
        return "VERDE";
    }

    /** Recalcula o semaforo para o dia de hoje (o prazo pode ter vencido desde a gravacao). */
    public static void atualizarSemaforo(AcaoPlano a) {
        a.setSemaforo(calcularSemaforo(a.getPrazo(), a.isConcluida(), LocalDate.now()));
    }

    public ArrayList<AcaoPlano> listarPorPlano(int idPlano) {
        try (Connection con = factory.abrirConexao()) {
            if (new PlanoAcaoDAO(con).buscarPorId(idPlano) == null) {
                throw new RecursoNaoEncontradoException("Plano de acao", idPlano);
            }
            ArrayList<AcaoPlano> acoes = new AcaoPlanoDAO(con).listarPorPlano(idPlano);
            acoes.forEach(AcaoPlanoBO::atualizarSemaforo);
            return acoes;
        } catch (SQLException e) {
            throw new BancoDeDadosException(e);
        }
    }

    public AcaoPlano buscar(int id) {
        try (Connection con = factory.abrirConexao()) {
            AcaoPlano a = buscarExistente(new AcaoPlanoDAO(con), id);
            atualizarSemaforo(a);
            return a;
        } catch (SQLException e) {
            throw new BancoDeDadosException(e);
        }
    }

    public AcaoPlano adicionar(AcaoPlano a) {
        validar(a);
        try (Connection con = factory.abrirConexao()) {
            if (new PlanoAcaoDAO(con).buscarPorId(a.getIdPlano()) == null) {
                throw new RegraDeNegocioException("Plano de acao " + a.getIdPlano() + " nao existe.");
            }
            AcaoPlanoDAO dao = new AcaoPlanoDAO(con);
            validarReferencias(con, dao, a, 0);
            a.setIdAcao(dao.proximoId());
            atualizarSemaforo(a);
            dao.inserir(a);
            return a;
        } catch (SQLException e) {
            throw new BancoDeDadosException(e);
        }
    }

    public AcaoPlano atualizar(int id, AcaoPlano a) {
        validar(a);
        try (Connection con = factory.abrirConexao()) {
            AcaoPlanoDAO dao = new AcaoPlanoDAO(con);
            AcaoPlano atual = buscarExistente(dao, id);
            a.setIdAcao(id);
            a.setIdPlano(atual.getIdPlano()); // o passo nao muda de plano
            validarReferencias(con, dao, a, id);
            atualizarSemaforo(a);
            dao.alterar(a);
            return a;
        } catch (SQLException e) {
            throw new BancoDeDadosException(e);
        }
    }

    public void excluir(int id) {
        try (Connection con = factory.abrirConexao()) {
            AcaoPlanoDAO dao = new AcaoPlanoDAO(con);
            AcaoPlano atual = buscarExistente(dao, id);
            if (dao.listarPorPlano(atual.getIdPlano()).size() <= 1) {
                throw new RegraDeNegocioException("O plano precisa de pelo menos um passo. Exclua o plano inteiro se for o caso.");
            }
            dao.excluir(id);
        } catch (SQLException e) {
            throw new BancoDeDadosException(e);
        }
    }

    // ---------------- regras (tambem usadas pelo PlanoAcaoBO) ----------------

    static void validar(AcaoPlano a) {
        if (a.getDescricao() != null) {
            a.setDescricao(a.getDescricao().trim());
        }
        if (a.getDescricao() == null || a.getDescricao().length() < 5 || a.getDescricao().length() > 300) {
            throw new RegraDeNegocioException("Descricao do passo obrigatoria, entre 5 e 300 caracteres.");
        }
        if (a.getOrdem() <= 0 || a.getOrdem() > 99) {
            throw new RegraDeNegocioException("Ordem do passo deve ser de 1 a 99.");
        }
    }

    static void validarLei(Connection con, Integer idLei) throws SQLException {
        if (idLei != null && new LeiDAO(con).buscarPorId(idLei) == null) {
            throw new RegraDeNegocioException("Lei " + idLei + " nao existe (ver GET /leis).");
        }
    }

    private void validarReferencias(Connection con, AcaoPlanoDAO dao, AcaoPlano a, int ignorarId) throws SQLException {
        validarLei(con, a.getIdLei());
        if (dao.ordemEmUso(a.getIdPlano(), a.getOrdem(), ignorarId)) {
            throw new ConflitoException("Ja existe um passo de ordem " + a.getOrdem() + " neste plano.");
        }
    }

    private AcaoPlano buscarExistente(AcaoPlanoDAO dao, int id) throws SQLException {
        AcaoPlano a = dao.buscarPorId(id);
        if (a == null) {
            throw new RecursoNaoEncontradoException("Passo do plano", id);
        }
        return a;
    }
}
