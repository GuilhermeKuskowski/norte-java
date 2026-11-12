package br.com.fiap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import br.com.fiap.model.Encaminhamento;

/**
 * Acesso a tabela NRT_ENCAMINHAMENTO.
 */
public class EncaminhamentoDAO {

    private Connection con;

    public EncaminhamentoDAO(Connection con) {
        this.con = con;
    }

    public Connection getCon() {
        return con;
    }

    public int proximoId() throws SQLException {
        return JdbcUtil.proximoId(con, "nrt_encaminhamento", "id_encaminhamento");
    }

    public void inserir(Encaminhamento e) throws SQLException {
        String sql = "INSERT INTO nrt_encaminhamento (id_encaminhamento, id_solicitacao, id_ponto, "
                + "dt_encaminhamento, dt_agendada, st_encaminhamento) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, e.getIdEncaminhamento());
            ps.setInt(2, e.getIdSolicitacao());
            ps.setInt(3, e.getIdPonto());
            JdbcUtil.setDataHora(ps, 4, e.getDataEncaminhamento());
            JdbcUtil.setDataHora(ps, 5, e.getDataAgendada());
            ps.setString(6, e.getStatus());
            ps.executeUpdate();
        }
    }

    public boolean alterar(Encaminhamento e) throws SQLException {
        String sql = "UPDATE nrt_encaminhamento SET id_ponto = ?, dt_agendada = ?, st_encaminhamento = ? "
                + "WHERE id_encaminhamento = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, e.getIdPonto());
            JdbcUtil.setDataHora(ps, 2, e.getDataAgendada());
            ps.setString(3, e.getStatus());
            ps.setInt(4, e.getIdEncaminhamento());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean excluir(int idEncaminhamento) throws SQLException {
        String sql = "DELETE FROM nrt_encaminhamento WHERE id_encaminhamento = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEncaminhamento);
            return ps.executeUpdate() > 0;
        }
    }

    public Encaminhamento buscarPorId(int idEncaminhamento) throws SQLException {
        String sql = "SELECT * FROM nrt_encaminhamento WHERE id_encaminhamento = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEncaminhamento);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? montar(rs) : null;
            }
        }
    }

    /** Lista todos, com filtro opcional por solicitacao (null = todos). */
    public ArrayList<Encaminhamento> listar(Integer idSolicitacao) throws SQLException {
        String sql = "SELECT * FROM nrt_encaminhamento WHERE (? IS NULL OR id_solicitacao = ?) "
                + "ORDER BY dt_encaminhamento DESC, id_encaminhamento";
        ArrayList<Encaminhamento> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            JdbcUtil.setInteiro(ps, 1, idSolicitacao);
            JdbcUtil.setInteiro(ps, 2, idSolicitacao);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(montar(rs));
                }
            }
        }
        return lista;
    }

    public boolean jaEncaminhado(int idSolicitacao, int idPonto, int ignorarId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM nrt_encaminhamento WHERE id_solicitacao = ? AND id_ponto = ? "
                + "AND id_encaminhamento <> ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSolicitacao);
            ps.setInt(2, idPonto);
            ps.setInt(3, ignorarId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    public boolean solicitacaoTemEncaminhamentos(int idSolicitacao) throws SQLException {
        return JdbcUtil.existe(con, "nrt_encaminhamento", "id_solicitacao", idSolicitacao);
    }

    private Encaminhamento montar(ResultSet rs) throws SQLException {
        Encaminhamento e = new Encaminhamento();
        e.setIdEncaminhamento(rs.getInt("id_encaminhamento"));
        e.setIdSolicitacao(rs.getInt("id_solicitacao"));
        e.setIdPonto(rs.getInt("id_ponto"));
        e.setDataEncaminhamento(JdbcUtil.getDataHora(rs, "dt_encaminhamento"));
        e.setDataAgendada(JdbcUtil.getDataHora(rs, "dt_agendada"));
        e.setStatus(rs.getString("st_encaminhamento"));
        return e;
    }
}
