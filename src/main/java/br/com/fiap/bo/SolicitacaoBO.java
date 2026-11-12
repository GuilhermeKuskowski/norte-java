package br.com.fiap.bo;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.fiap.dao.ConnectionFactory;
import br.com.fiap.dao.AreaDireitoDAO;
import br.com.fiap.dao.CidadaoDAO;
import br.com.fiap.dao.EncaminhamentoDAO;
import br.com.fiap.dao.PlanoAcaoDAO;
import br.com.fiap.dao.SolicitacaoDAO;
import br.com.fiap.exception.BancoDeDadosException;
import br.com.fiap.exception.ConflitoException;
import br.com.fiap.exception.RecursoNaoEncontradoException;
import br.com.fiap.exception.RegraDeNegocioException;
import br.com.fiap.model.Solicitacao;

/**
 * Regras de negocio da solicitacao (o problema contado pelo cidadao).
 */
@Service
public class SolicitacaoBO {

    public static final List<String> CANAIS = List.of("TEXTO", "AUDIO");
    public static final List<String> STATUS = List.of("ABERTA", "EM ANDAMENTO", "CONCLUIDA", "CANCELADA");

    private final ConnectionFactory factory;

    public SolicitacaoBO(ConnectionFactory factory) {
        this.factory = factory;
    }

    public ArrayList<Solicitacao> listar(Integer idCidadao, Integer idArea, String status) {
        String filtroStatus = status == null ? null : status.trim().toUpperCase();
        if (filtroStatus != null && !STATUS.contains(filtroStatus)) {
            throw new RegraDeNegocioException("Status invalido. Use: " + STATUS);
        }
        try (Connection con = factory.abrirConexao()) {
            return new SolicitacaoDAO(con).listar(idCidadao, idArea, filtroStatus);
        } catch (SQLException e) {
            throw new BancoDeDadosException(e);
        }
    }

    public Solicitacao buscar(int id) {
        try (Connection con = factory.abrirConexao()) {
            return buscarExistente(new SolicitacaoDAO(con), id);
        } catch (SQLException e) {
            throw new BancoDeDadosException(e);
        }
    }

    public Solicitacao abrir(Solicitacao s) {
        normalizar(s);
        validar(s);
        try (Connection con = factory.abrirConexao()) {
            if (new CidadaoDAO(con).buscarPorId(s.getIdCidadao()) == null) {
                throw new RegraDeNegocioException("Cidadao " + s.getIdCidadao() + " nao existe.");
            }
            validarArea(con, s.getIdArea());
            SolicitacaoDAO dao = new SolicitacaoDAO(con);
            s.setIdSolicitacao(dao.proximoId());
            s.setDataAbertura(LocalDateTime.now().withNano(0));
            s.setStatus("ABERTA");
            dao.inserir(s);
            return s;
        } catch (SQLException e) {
            throw new BancoDeDadosException(e);
        }
    }

    public Solicitacao atualizar(int id, Solicitacao s) {
        normalizar(s);
        validar(s);
        try (Connection con = factory.abrirConexao()) {
            SolicitacaoDAO dao = new SolicitacaoDAO(con);
            Solicitacao atual = buscarExistente(dao, id);
            if (encerrada(atual.getStatus())) {
                throw new RegraDeNegocioException("Solicitacao " + atual.getStatus() + " nao pode mais ser alterada.");
            }
            if (s.getStatus() == null) {
                s.setStatus(atual.getStatus());
            } else if (!STATUS.contains(s.getStatus())) {
                throw new RegraDeNegocioException("Status invalido. Use: " + STATUS);
            }
            validarArea(con, s.getIdArea());
            // O dono da solicitacao e a data de abertura nao mudam
            s.setIdSolicitacao(id);
            s.setIdCidadao(atual.getIdCidadao());
            s.setDataAbertura(atual.getDataAbertura());
            dao.alterar(s);
            return s;
        } catch (SQLException e) {
            throw new BancoDeDadosException(e);
        }
    }

    public void excluir(int id) {
        try (Connection con = factory.abrirConexao()) {
            SolicitacaoDAO dao = new SolicitacaoDAO(con);
            buscarExistente(dao, id);
            if (new PlanoAcaoDAO(con).buscarPorSolicitacao(id) != null) {
                throw new ConflitoException("A solicitacao " + id + " tem plano de acao. Exclua o plano antes.");
            }
            if (new EncaminhamentoDAO(con).solicitacaoTemEncaminhamentos(id)) {
                throw new ConflitoException("A solicitacao " + id + " tem encaminhamentos. Exclua-os antes.");
            }
            dao.excluir(id);
        } catch (SQLException e) {
            throw new BancoDeDadosException(e);
        }
    }

    // ---------------- regras ----------------

    static boolean encerrada(String status) {
        return "CONCLUIDA".equals(status) || "CANCELADA".equals(status);
    }

    private Solicitacao buscarExistente(SolicitacaoDAO dao, int id) throws SQLException {
        Solicitacao s = dao.buscarPorId(id);
        if (s == null) {
            throw new RecursoNaoEncontradoException("Solicitacao", id);
        }
        return s;
    }

    private void validarArea(Connection con, int idArea) throws SQLException {
        if (new AreaDireitoDAO(con).buscarPorId(idArea) == null) {
            throw new RegraDeNegocioException("Area " + idArea + " nao existe (use de 1 a 10, ver GET /areas).");
        }
    }

    private void normalizar(Solicitacao s) {
        if (s.getRelato() != null) {
            s.setRelato(s.getRelato().trim());
        }
        if (s.getCanal() != null) {
            s.setCanal(s.getCanal().trim().toUpperCase());
        }
        if (s.getStatus() != null) {
            s.setStatus(s.getStatus().trim().toUpperCase());
        }
    }

    private void validar(Solicitacao s) {
        if (s.getRelato() == null || s.getRelato().length() < 10 || s.getRelato().length() > 1000) {
            throw new RegraDeNegocioException("Conte o problema com 10 a 1000 caracteres (relato).");
        }
        if (s.getCanal() == null || !CANAIS.contains(s.getCanal())) {
            throw new RegraDeNegocioException("Canal invalido. Use: " + CANAIS);
        }
        if (s.getValorEstimado() != null && s.getValorEstimado() < 0) {
            throw new RegraDeNegocioException("Valor estimado nao pode ser negativo.");
        }
    }
}
