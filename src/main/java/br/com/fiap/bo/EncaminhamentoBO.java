package br.com.fiap.bo;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.fiap.dao.ConnectionFactory;
import br.com.fiap.dao.CidadaoDAO;
import br.com.fiap.dao.EncaminhamentoDAO;
import br.com.fiap.dao.PontoAtendimentoDAO;
import br.com.fiap.dao.SolicitacaoDAO;
import br.com.fiap.exception.BancoDeDadosException;
import br.com.fiap.exception.ConflitoException;
import br.com.fiap.exception.RecursoNaoEncontradoException;
import br.com.fiap.exception.RegraDeNegocioException;
import br.com.fiap.model.Cidadao;
import br.com.fiap.model.Encaminhamento;
import br.com.fiap.model.PontoAtendimento;
import br.com.fiap.model.Solicitacao;

/**
 * Regras de negocio do encaminhamento para atendimento gratuito.
 */
@Service
public class EncaminhamentoBO {

    public static final List<String> STATUS = List.of("INDICADO", "AGENDADO", "COMPARECEU", "NAO COMPARECEU");

    private final ConnectionFactory factory;

    public EncaminhamentoBO(ConnectionFactory factory) {
        this.factory = factory;
    }

    public ArrayList<Encaminhamento> listar(Integer idSolicitacao) {
        try (Connection con = factory.abrirConexao()) {
            return new EncaminhamentoDAO(con).listar(idSolicitacao);
        } catch (SQLException e) {
            throw new BancoDeDadosException(e);
        }
    }

    public Encaminhamento buscar(int id) {
        try (Connection con = factory.abrirConexao()) {
            return buscarExistente(new EncaminhamentoDAO(con), id);
        } catch (SQLException e) {
            throw new BancoDeDadosException(e);
        }
    }

    public Encaminhamento encaminhar(Encaminhamento e) {
        try (Connection con = factory.abrirConexao()) {
            Solicitacao s = new SolicitacaoDAO(con).buscarPorId(e.getIdSolicitacao());
            if (s == null) {
                throw new RegraDeNegocioException("Solicitacao " + e.getIdSolicitacao() + " nao existe.");
            }
            if (SolicitacaoBO.encerrada(s.getStatus())) {
                throw new RegraDeNegocioException("Solicitacao " + s.getStatus() + " nao recebe encaminhamento.");
            }
            EncaminhamentoDAO dao = new EncaminhamentoDAO(con);
            e.setDataEncaminhamento(LocalDateTime.now().withNano(0));
            if (e.getStatus() == null) {
                e.setStatus(e.getDataAgendada() == null ? "INDICADO" : "AGENDADO");
            }
            validar(e);
            validarPonto(con, s, e.getIdPonto());
            if (dao.jaEncaminhado(e.getIdSolicitacao(), e.getIdPonto(), 0)) {
                throw new ConflitoException("Esta solicitacao ja foi encaminhada para o ponto " + e.getIdPonto() + ".");
            }
            e.setIdEncaminhamento(dao.proximoId());
            dao.inserir(e);
            return e;
        } catch (SQLException ex) {
            throw new BancoDeDadosException(ex);
        }
    }

    public Encaminhamento atualizar(int id, Encaminhamento e) {
        try (Connection con = factory.abrirConexao()) {
            EncaminhamentoDAO dao = new EncaminhamentoDAO(con);
            Encaminhamento atual = buscarExistente(dao, id);
            // solicitacao e data do encaminhamento nao mudam
            e.setIdEncaminhamento(id);
            e.setIdSolicitacao(atual.getIdSolicitacao());
            e.setDataEncaminhamento(atual.getDataEncaminhamento());
            if (e.getIdPonto() == 0) {
                e.setIdPonto(atual.getIdPonto());
            }
            if (e.getStatus() == null) {
                e.setStatus(atual.getStatus());
            }
            validar(e);
            if (e.getIdPonto() != atual.getIdPonto()) {
                Solicitacao s = new SolicitacaoDAO(con).buscarPorId(atual.getIdSolicitacao());
                validarPonto(con, s, e.getIdPonto());
                if (dao.jaEncaminhado(e.getIdSolicitacao(), e.getIdPonto(), id)) {
                    throw new ConflitoException("Esta solicitacao ja foi encaminhada para o ponto " + e.getIdPonto() + ".");
                }
            }
            dao.alterar(e);
            return e;
        } catch (SQLException ex) {
            throw new BancoDeDadosException(ex);
        }
    }

    public void excluir(int id) {
        try (Connection con = factory.abrirConexao()) {
            EncaminhamentoDAO dao = new EncaminhamentoDAO(con);
            buscarExistente(dao, id);
            dao.excluir(id);
        } catch (SQLException e) {
            throw new BancoDeDadosException(e);
        }
    }

    // ---------------- regras ----------------

    private void validar(Encaminhamento e) {
        e.setStatus(e.getStatus().trim().toUpperCase());
        if (!STATUS.contains(e.getStatus())) {
            throw new RegraDeNegocioException("Status invalido. Use: " + STATUS);
        }
        if (e.getDataAgendada() != null && e.getDataAgendada().isBefore(e.getDataEncaminhamento())) {
            throw new RegraDeNegocioException("A data agendada nao pode ser anterior ao encaminhamento.");
        }
        if (!"INDICADO".equals(e.getStatus()) && e.getDataAgendada() == null) {
            throw new RegraDeNegocioException("Informe a data agendada (dataAgendada) para o status " + e.getStatus() + ".");
        }
    }

    /** O ponto precisa existir, ser gratuito e, se o cidadao for PcD, ser acessivel. */
    private void validarPonto(Connection con, Solicitacao s, int idPonto) throws SQLException {
        PontoAtendimento ponto = new PontoAtendimentoDAO(con).buscarPorId(idPonto);
        if (ponto == null) {
            throw new RegraDeNegocioException("Ponto de atendimento " + idPonto + " nao existe (ver GET /pontos).");
        }
        if (!ponto.isGratuito()) {
            throw new RegraDeNegocioException("O Norte so encaminha para atendimento gratuito.");
        }
        Cidadao c = new CidadaoDAO(con).buscarPorId(s.getIdCidadao());
        if (c != null && c.isPcd() && !ponto.isAcessivel()) {
            throw new RegraDeNegocioException("O ponto " + ponto.getNome()
                    + " nao e acessivel e o cidadao e pessoa com deficiencia. Escolha um ponto acessivel (GET /pontos?acessivel=true).");
        }
    }

    private Encaminhamento buscarExistente(EncaminhamentoDAO dao, int id) throws SQLException {
        Encaminhamento e = dao.buscarPorId(id);
        if (e == null) {
            throw new RecursoNaoEncontradoException("Encaminhamento", id);
        }
        return e;
    }
}
