package br.com.fiap.bo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.fiap.dao.ConnectionFactory;
import br.com.fiap.dao.AreaDireitoDAO;
import br.com.fiap.dao.BairroDAO;
import br.com.fiap.dao.LeiDAO;
import br.com.fiap.dao.PontoAtendimentoDAO;
import br.com.fiap.exception.BancoDeDadosException;
import br.com.fiap.exception.RecursoNaoEncontradoException;
import br.com.fiap.exception.RegraDeNegocioException;
import br.com.fiap.model.AreaDireito;
import br.com.fiap.model.Bairro;
import br.com.fiap.model.Lei;
import br.com.fiap.model.PontoAtendimento;

/**
 * Consultas de apoio usadas pelas telas: bairros, areas, leis e pontos de atendimento.
 */
@Service
public class ConsultaBO {

    public static final List<String> TIPOS_PONTO = List.of("DEFENSORIA", "PROCON", "JUIZADO", "CRAS", "CREAS", "NPJ", "INSS", "OUTRO");

    private final ConnectionFactory factory;

    public ConsultaBO(ConnectionFactory factory) {
        this.factory = factory;
    }

    public ArrayList<Bairro> listarBairros() {
        try (Connection con = factory.abrirConexao()) {
            return new BairroDAO(con).listarTodos();
        } catch (SQLException e) {
            throw new BancoDeDadosException(e);
        }
    }

    public ArrayList<AreaDireito> listarAreas() {
        try (Connection con = factory.abrirConexao()) {
            return new AreaDireitoDAO(con).listarTodas();
        } catch (SQLException e) {
            throw new BancoDeDadosException(e);
        }
    }

    public AreaDireito buscarArea(int id) {
        try (Connection con = factory.abrirConexao()) {
            AreaDireito a = new AreaDireitoDAO(con).buscarPorId(id);
            if (a == null) {
                throw new RecursoNaoEncontradoException("Area", id);
            }
            return a;
        } catch (SQLException e) {
            throw new BancoDeDadosException(e);
        }
    }

    public ArrayList<Lei> listarLeisDaArea(int idArea) {
        try (Connection con = factory.abrirConexao()) {
            if (new AreaDireitoDAO(con).buscarPorId(idArea) == null) {
                throw new RecursoNaoEncontradoException("Area", idArea);
            }
            return new LeiDAO(con).listarPorArea(idArea);
        } catch (SQLException e) {
            throw new BancoDeDadosException(e);
        }
    }

    public ArrayList<Lei> listarLeis() {
        try (Connection con = factory.abrirConexao()) {
            return new LeiDAO(con).listarVigentes();
        } catch (SQLException e) {
            throw new BancoDeDadosException(e);
        }
    }

    public Lei buscarLei(int id) {
        try (Connection con = factory.abrirConexao()) {
            Lei l = new LeiDAO(con).buscarPorId(id);
            if (l == null) {
                throw new RecursoNaoEncontradoException("Lei", id);
            }
            return l;
        } catch (SQLException e) {
            throw new BancoDeDadosException(e);
        }
    }

    public ArrayList<PontoAtendimento> listarPontos(String tipo, boolean somenteAcessiveis) {
        String filtroTipo = tipo == null || tipo.isBlank() ? null : tipo.trim().toUpperCase();
        if (filtroTipo != null && !TIPOS_PONTO.contains(filtroTipo)) {
            throw new RegraDeNegocioException("Tipo de ponto invalido. Use: " + TIPOS_PONTO);
        }
        try (Connection con = factory.abrirConexao()) {
            return new PontoAtendimentoDAO(con).listar(filtroTipo, somenteAcessiveis);
        } catch (SQLException e) {
            throw new BancoDeDadosException(e);
        }
    }

    public PontoAtendimento buscarPonto(int id) {
        try (Connection con = factory.abrirConexao()) {
            PontoAtendimento p = new PontoAtendimentoDAO(con).buscarPorId(id);
            if (p == null) {
                throw new RecursoNaoEncontradoException("Ponto de atendimento", id);
            }
            return p;
        } catch (SQLException e) {
            throw new BancoDeDadosException(e);
        }
    }
}
