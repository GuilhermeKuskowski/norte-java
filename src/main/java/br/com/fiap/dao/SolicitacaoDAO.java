package br.com.fiap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import br.com.fiap.model.Solicitacao;

/**
 * Acesso a tabela NRT_SOLICITACAO.
 */
public class SolicitacaoDAO {

    private Connection con;

    public SolicitacaoDAO(Connection con) {
        this.con = con;
    }

    public Connection getCon() {
        return con;
    }

    public int proximoId() throws SQLException {
        return JdbcUtil.proximoId(con, "nrt_solicitacao", "id_solicitacao");
    }

    public void inserir(Solicitacao s) throws SQLException {
        String sql = "INSERT INTO nrt_solicitacao (id_solicitacao, id_cidadao, id_area, ds_relato, tp_canal, "
                + "dt_abertura, st_solicitacao, vl_estimado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, s.getIdSolicitacao());
            ps.setInt(2, s.getIdCidadao());
            ps.setInt(3, s.getIdArea());
            ps.setString(4, s.getRelato());
            ps.setString(5, s.getCanal());
            JdbcUtil.setDataHora(ps, 6, s.getDataAbertura());
            ps.setString(7, s.getStatus());
            JdbcUtil.setDecimal(ps, 8, s.getValorEstimado());
            ps.executeUpdate();
        }
    }

    public boolean alterar(Solicitacao s) throws SQLException {
        String sql = "UPDATE nrt_solicitacao SET id_area = ?, ds_relato = ?, tp_canal = ?, st_solicitacao = ?, "
                + "vl_estimado = ? WHERE id_solicitacao = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, s.getIdArea());
            ps.setString(2, s.getRelato());
            ps.setString(3, s.getCanal());
            ps.setString(4, s.getStatus());
            JdbcUtil.setDecimal(ps, 5, s.getValorEstimado());
            ps.setInt(6, s.getIdSolicitacao());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean alterarStatus(int idSolicitacao, String status) throws SQLException {
        String sql = "UPDATE nrt_solicitacao SET st_solicitacao = ? WHERE id_solicitacao = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, idSolicitacao);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean excluir(int idSolicitacao) throws SQLException {
        String sql = "DELETE FROM nrt_solicitacao WHERE id_solicitacao = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSolicitacao);
            return ps.executeUpdate() > 0;
        }
    }

    public Solicitacao buscarPorId(int idSolicitacao) throws SQLException {
        String sql = "SELECT * FROM nrt_solicitacao WHERE id_solicitacao = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSolicitacao);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? montar(rs) : null;
            }
        }
    }

    /** Lista todas, com filtros opcionais por cidadao, area e status (null = sem filtro). */
    public ArrayList<Solicitacao> listar(Integer idCidadao, Integer idArea, String status) throws SQLException {
        String sql = "SELECT * FROM nrt_solicitacao "
                + "WHERE (? IS NULL OR id_cidadao = ?) "
                + "AND (? IS NULL OR id_area = ?) "
                + "AND (? IS NULL OR st_solicitacao = ?) "
                + "ORDER BY dt_abertura DESC";
        ArrayList<Solicitacao> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            JdbcUtil.setInteiro(ps, 1, idCidadao);
            JdbcUtil.setInteiro(ps, 2, idCidadao);
            JdbcUtil.setInteiro(ps, 3, idArea);
            JdbcUtil.setInteiro(ps, 4, idArea);
            ps.setString(5, status);
            ps.setString(6, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(montar(rs));
                }
            }
        }
        return lista;
    }

    public boolean cidadaoTemSolicitacoes(int idCidadao) throws SQLException {
        return JdbcUtil.existe(con, "nrt_solicitacao", "id_cidadao", idCidadao);
    }

    private Solicitacao montar(ResultSet rs) throws SQLException {
        Solicitacao s = new Solicitacao();
        s.setIdSolicitacao(rs.getInt("id_solicitacao"));
        s.setIdCidadao(rs.getInt("id_cidadao"));
        s.setIdArea(rs.getInt("id_area"));
        s.setRelato(rs.getString("ds_relato"));
        s.setCanal(rs.getString("tp_canal"));
        s.setDataAbertura(JdbcUtil.getDataHora(rs, "dt_abertura"));
        s.setStatus(rs.getString("st_solicitacao"));
        s.setValorEstimado(JdbcUtil.getDecimal(rs, "vl_estimado"));
        return s;
    }
}
