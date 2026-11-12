package br.com.fiap.bo;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import br.com.fiap.dao.ConnectionFactory;
import br.com.fiap.dao.BairroDAO;
import br.com.fiap.dao.CidadaoDAO;
import br.com.fiap.dao.SolicitacaoDAO;
import br.com.fiap.exception.BancoDeDadosException;
import br.com.fiap.exception.ConflitoException;
import br.com.fiap.exception.RecursoNaoEncontradoException;
import br.com.fiap.exception.RegraDeNegocioException;
import br.com.fiap.model.Cidadao;

/**
 * Regras de negocio do cadastro do cidadao.
 */
@Service
public class CidadaoBO {

    private final ConnectionFactory factory;

    public CidadaoBO(ConnectionFactory factory) {
        this.factory = factory;
    }

    public ArrayList<Cidadao> listar() {
        try (Connection con = factory.abrirConexao()) {
            return new CidadaoDAO(con).listarTodos();
        } catch (SQLException e) {
            throw new BancoDeDadosException(e);
        }
    }

    public Cidadao buscar(int id) {
        try (Connection con = factory.abrirConexao()) {
            Cidadao c = new CidadaoDAO(con).buscarPorId(id);
            if (c == null) {
                throw new RecursoNaoEncontradoException("Cidadao", id);
            }
            return c;
        } catch (SQLException e) {
            throw new BancoDeDadosException(e);
        }
    }

    public Cidadao cadastrar(Cidadao c) {
        normalizar(c);
        validar(c);
        try (Connection con = factory.abrirConexao()) {
            CidadaoDAO dao = new CidadaoDAO(con);
            validarReferencias(con, dao, c, 0);
            c.setIdCidadao(dao.proximoId());
            // O cadastro so e enviado pelo app depois que a pessoa aceita os termos de uso
            LocalDateTime agora = LocalDateTime.now().withNano(0);
            c.setDataCadastro(agora);
            c.setDataAceiteTermos(agora);
            dao.inserir(c);
            return c;
        } catch (SQLException e) {
            throw new BancoDeDadosException(e);
        }
    }

    public Cidadao atualizar(int id, Cidadao c) {
        normalizar(c);
        validar(c);
        try (Connection con = factory.abrirConexao()) {
            CidadaoDAO dao = new CidadaoDAO(con);
            Cidadao atual = dao.buscarPorId(id);
            if (atual == null) {
                throw new RecursoNaoEncontradoException("Cidadao", id);
            }
            validarReferencias(con, dao, c, id);
            c.setIdCidadao(id);
            dao.alterar(c);
            c.setDataCadastro(atual.getDataCadastro());
            c.setDataAceiteTermos(atual.getDataAceiteTermos());
            return c;
        } catch (SQLException e) {
            throw new BancoDeDadosException(e);
        }
    }

    public void excluir(int id) {
        try (Connection con = factory.abrirConexao()) {
            CidadaoDAO dao = new CidadaoDAO(con);
            if (dao.buscarPorId(id) == null) {
                throw new RecursoNaoEncontradoException("Cidadao", id);
            }
            if (new SolicitacaoDAO(con).cidadaoTemSolicitacoes(id)) {
                throw new ConflitoException("O cidadao " + id + " tem solicitacoes registradas e nao pode ser excluido.");
            }
            dao.excluir(id);
        } catch (SQLException e) {
            throw new BancoDeDadosException(e);
        }
    }

    // ---------------- regras ----------------

    private void normalizar(Cidadao c) {
        if (c.getNome() != null) {
            c.setNome(c.getNome().trim());
        }
        if (c.getCelular() != null) {
            c.setCelular(c.getCelular().replaceAll("\\D", ""));
        }
        if (c.getEmail() != null) {
            c.setEmail(c.getEmail().trim().toLowerCase());
            if (c.getEmail().isEmpty()) {
                c.setEmail(null);
            }
        }
    }

    private void validar(Cidadao c) {
        if (c.getNome() == null || c.getNome().length() < 3 || c.getNome().length() > 100) {
            throw new RegraDeNegocioException("Nome obrigatorio, entre 3 e 100 caracteres.");
        }
        if (c.getDataNascimento() == null) {
            throw new RegraDeNegocioException("Data de nascimento obrigatoria (AAAA-MM-DD).");
        }
        LocalDate hoje = LocalDate.now();
        if (!c.getDataNascimento().isBefore(hoje) || c.getDataNascimento().isBefore(hoje.minusYears(120))) {
            throw new RegraDeNegocioException("Data de nascimento invalida.");
        }
        if (c.getCelular() == null || c.getCelular().length() < 10 || c.getCelular().length() > 11) {
            throw new RegraDeNegocioException("Celular obrigatorio, com DDD (10 ou 11 digitos).");
        }
        if (c.getEmail() != null && (!c.getEmail().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$") || c.getEmail().length() > 120)) {
            throw new RegraDeNegocioException("E-mail invalido.");
        }
        if (c.getIdBairro() <= 0) {
            throw new RegraDeNegocioException("Informe o bairro (idBairro).");
        }
    }

    private void validarReferencias(Connection con, CidadaoDAO dao, Cidadao c, int ignorarId) throws SQLException {
        if (new BairroDAO(con).buscarPorId(c.getIdBairro()) == null) {
            throw new RegraDeNegocioException("Bairro " + c.getIdBairro() + " nao existe.");
        }
        if (c.getEmail() != null && dao.emailEmUso(c.getEmail(), ignorarId)) {
            throw new ConflitoException("E-mail ja cadastrado para outro cidadao.");
        }
    }
}
