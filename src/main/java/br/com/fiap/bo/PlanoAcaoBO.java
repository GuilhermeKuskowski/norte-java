package br.com.fiap.bo;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import br.com.fiap.dao.ConnectionFactory;
import br.com.fiap.dao.AcaoPlanoDAO;
import br.com.fiap.dao.PlanoAcaoDAO;
import br.com.fiap.dao.SolicitacaoDAO;
import br.com.fiap.exception.BancoDeDadosException;
import br.com.fiap.exception.ConflitoException;
import br.com.fiap.exception.RecursoNaoEncontradoException;
import br.com.fiap.exception.RegraDeNegocioException;
import br.com.fiap.model.AcaoPlano;
import br.com.fiap.model.ItemChecklist;
import br.com.fiap.model.PlanoAcao;
import br.com.fiap.model.Solicitacao;

/**
 * Regras de negocio do plano de acao. Um plano e gravado junto com seus passos e o
 * checklist de documentos em uma unica transacao (tudo ou nada).
 */
@Service
public class PlanoAcaoBO {

    private final ConnectionFactory factory;

    public PlanoAcaoBO(ConnectionFactory factory) {
        this.factory = factory;
    }

    public ArrayList<PlanoAcao> listar() {
        try (Connection con = factory.abrirConexao()) {
            ArrayList<PlanoAcao> planos = new PlanoAcaoDAO(con).listarTodos();
            for (PlanoAcao p : planos) {
                completar(con, p);
            }
            return planos;
        } catch (SQLException e) {
            throw new BancoDeDadosException(e);
        }
    }

    public PlanoAcao buscar(int id) {
        try (Connection con = factory.abrirConexao()) {
            PlanoAcao p = new PlanoAcaoDAO(con).buscarPorId(id);
            if (p == null) {
                throw new RecursoNaoEncontradoException("Plano de acao", id);
            }
            completar(con, p);
            return p;
        } catch (SQLException e) {
            throw new BancoDeDadosException(e);
        }
    }

    public PlanoAcao buscarPorSolicitacao(int idSolicitacao) {
        try (Connection con = factory.abrirConexao()) {
            PlanoAcao p = new PlanoAcaoDAO(con).buscarPorSolicitacao(idSolicitacao);
            if (p == null) {
                throw new RecursoNaoEncontradoException("A solicitacao " + idSolicitacao + " ainda nao tem plano de acao.");
            }
            completar(con, p);
            return p;
        } catch (SQLException e) {
            throw new BancoDeDadosException(e);
        }
    }

    public PlanoAcao criar(PlanoAcao p) {
        validarPlano(p);
        if (p.getAcoes() == null || p.getAcoes().isEmpty()) {
            throw new RegraDeNegocioException("O plano precisa de pelo menos um passo (acoes).");
        }
        Set<Integer> ordens = new HashSet<>();
        for (AcaoPlano a : p.getAcoes()) {
            AcaoPlanoBO.validar(a);
            if (!ordens.add(a.getOrdem())) {
                throw new RegraDeNegocioException("Ordem " + a.getOrdem() + " repetida nos passos.");
            }
        }
        try (Connection con = factory.abrirConexao()) {
            SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO(con);
            PlanoAcaoDAO planoDAO = new PlanoAcaoDAO(con);
            AcaoPlanoDAO acaoDAO = new AcaoPlanoDAO(con);

            Solicitacao s = solicitacaoDAO.buscarPorId(p.getIdSolicitacao());
            if (s == null) {
                throw new RegraDeNegocioException("Solicitacao " + p.getIdSolicitacao() + " nao existe.");
            }
            if (SolicitacaoBO.encerrada(s.getStatus())) {
                throw new RegraDeNegocioException("Solicitacao " + s.getStatus() + " nao recebe plano de acao.");
            }
            if (planoDAO.buscarPorSolicitacao(p.getIdSolicitacao()) != null) {
                throw new ConflitoException("A solicitacao " + p.getIdSolicitacao() + " ja tem plano de acao.");
            }
            for (AcaoPlano a : p.getAcoes()) {
                AcaoPlanoBO.validarLei(con, a.getIdLei());
            }
            validarChecklist(planoDAO, p);

            con.setAutoCommit(false);
            try {
                p.setIdPlano(planoDAO.proximoId());
                p.setDataGeracao(LocalDateTime.now().withNano(0));
                planoDAO.inserir(p);

                int proximoIdAcao = acaoDAO.proximoId();
                for (AcaoPlano a : p.getAcoes()) {
                    a.setIdAcao(proximoIdAcao++);
                    a.setIdPlano(p.getIdPlano());
                    AcaoPlanoBO.atualizarSemaforo(a);
                    acaoDAO.inserir(a);
                }
                for (ItemChecklist item : p.getChecklist()) {
                    planoDAO.inserirItemChecklist(p.getIdPlano(), item);
                }
                if ("ABERTA".equals(s.getStatus())) {
                    solicitacaoDAO.alterarStatus(s.getIdSolicitacao(), "EM ANDAMENTO");
                }
                con.commit();
            } catch (SQLException | RuntimeException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
            completar(con, p);
            return p;
        } catch (SQLException e) {
            throw new BancoDeDadosException(e);
        }
    }

    /** Altera o resumo, o "precisa de advogado?" e, se vier preenchido, substitui o checklist. */
    public PlanoAcao atualizar(int id, PlanoAcao p) {
        validarPlano(p);
        try (Connection con = factory.abrirConexao()) {
            PlanoAcaoDAO planoDAO = new PlanoAcaoDAO(con);
            PlanoAcao atual = planoDAO.buscarPorId(id);
            if (atual == null) {
                throw new RecursoNaoEncontradoException("Plano de acao", id);
            }
            validarChecklist(planoDAO, p);
            p.setIdPlano(id);
            con.setAutoCommit(false);
            try {
                planoDAO.alterar(p);
                if (!p.getChecklist().isEmpty()) {
                    planoDAO.excluirChecklist(id);
                    for (ItemChecklist item : p.getChecklist()) {
                        planoDAO.inserirItemChecklist(id, item);
                    }
                }
                con.commit();
            } catch (SQLException | RuntimeException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
            PlanoAcao atualizado = planoDAO.buscarPorId(id);
            completar(con, atualizado);
            return atualizado;
        } catch (SQLException e) {
            throw new BancoDeDadosException(e);
        }
    }

    /** Exclui o plano com seus passos e checklist; a solicitacao volta para ABERTA. */
    public void excluir(int id) {
        try (Connection con = factory.abrirConexao()) {
            PlanoAcaoDAO planoDAO = new PlanoAcaoDAO(con);
            PlanoAcao atual = planoDAO.buscarPorId(id);
            if (atual == null) {
                throw new RecursoNaoEncontradoException("Plano de acao", id);
            }
            SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO(con);
            Solicitacao s = solicitacaoDAO.buscarPorId(atual.getIdSolicitacao());
            con.setAutoCommit(false);
            try {
                new AcaoPlanoDAO(con).excluirPorPlano(id);
                planoDAO.excluirChecklist(id);
                planoDAO.excluir(id);
                if (s != null && "EM ANDAMENTO".equals(s.getStatus())) {
                    solicitacaoDAO.alterarStatus(s.getIdSolicitacao(), "ABERTA");
                }
                con.commit();
            } catch (SQLException | RuntimeException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new BancoDeDadosException(e);
        }
    }

    // ---------------- regras ----------------

    private void completar(Connection con, PlanoAcao p) throws SQLException {
        ArrayList<AcaoPlano> acoes = new AcaoPlanoDAO(con).listarPorPlano(p.getIdPlano());
        acoes.forEach(AcaoPlanoBO::atualizarSemaforo);
        p.setAcoes(acoes);
        p.setChecklist(new PlanoAcaoDAO(con).listarChecklist(p.getIdPlano()));
    }

    private void validarPlano(PlanoAcao p) {
        if (p.getResumo() != null) {
            p.setResumo(p.getResumo().trim());
        }
        if (p.getResumo() == null || p.getResumo().length() < 5 || p.getResumo().length() > 400) {
            throw new RegraDeNegocioException("Resumo do plano obrigatorio, entre 5 e 400 caracteres.");
        }
        if (p.getChecklist() == null) {
            p.setChecklist(new ArrayList<>());
        }
    }

    private void validarChecklist(PlanoAcaoDAO dao, PlanoAcao p) throws SQLException {
        Set<Integer> documentos = new HashSet<>();
        for (ItemChecklist item : p.getChecklist()) {
            if (!documentos.add(item.getIdDocumento())) {
                throw new RegraDeNegocioException("Documento " + item.getIdDocumento() + " repetido no checklist.");
            }
            if (!dao.documentoExiste(item.getIdDocumento())) {
                throw new RegraDeNegocioException("Documento " + item.getIdDocumento() + " nao existe no catalogo.");
            }
        }
    }
}
