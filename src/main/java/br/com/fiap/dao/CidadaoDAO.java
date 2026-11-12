package br.com.fiap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import br.com.fiap.model.Cidadao;

/**
 * Acesso a tabela NRT_CIDADAO.
 */
public class CidadaoDAO {

    private Connection con;

    public CidadaoDAO(Connection con) {
        this.con = con;
    }

    public Connection getCon() {
        return con;
    }

    public int proximoId() throws SQLException {
        return JdbcUtil.proximoId(con, "nrt_cidadao", "id_cidadao");
    }

    public void inserir(Cidadao c) throws SQLException {
        String sql = "INSERT INTO nrt_cidadao (id_cidadao, id_bairro, nm_cidadao, dt_nascimento, ds_email, "
                + "nr_celular, st_pcd, dt_cadastro, dt_aceite_termos) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, c.getIdCidadao());
            ps.setInt(2, c.getIdBairro());
            ps.setString(3, c.getNome());
            JdbcUtil.setData(ps, 4, c.getDataNascimento());
            ps.setString(5, c.getEmail());
            ps.setString(6, c.getCelular());
            ps.setString(7, JdbcUtil.paraSN(c.isPcd()));
            JdbcUtil.setDataHora(ps, 8, c.getDataCadastro());
            JdbcUtil.setDataHora(ps, 9, c.getDataAceiteTermos());
            ps.executeUpdate();
        }
    }

    public boolean alterar(Cidadao c) throws SQLException {
        String sql = "UPDATE nrt_cidadao SET id_bairro = ?, nm_cidadao = ?, dt_nascimento = ?, ds_email = ?, "
                + "nr_celular = ?, st_pcd = ? WHERE id_cidadao = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, c.getIdBairro());
            ps.setString(2, c.getNome());
            JdbcUtil.setData(ps, 3, c.getDataNascimento());
            ps.setString(4, c.getEmail());
            ps.setString(5, c.getCelular());
            ps.setString(6, JdbcUtil.paraSN(c.isPcd()));
            ps.setInt(7, c.getIdCidadao());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean excluir(int idCidadao) throws SQLException {
        String sql = "DELETE FROM nrt_cidadao WHERE id_cidadao = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCidadao);
            return ps.executeUpdate() > 0;
        }
    }

    public Cidadao buscarPorId(int idCidadao) throws SQLException {
        String sql = "SELECT * FROM nrt_cidadao WHERE id_cidadao = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCidadao);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? montar(rs) : null;
            }
        }
    }

    public ArrayList<Cidadao> listarTodos() throws SQLException {
        String sql = "SELECT * FROM nrt_cidadao ORDER BY id_cidadao";
        ArrayList<Cidadao> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(montar(rs));
            }
        }
        return lista;
    }

    public boolean emailEmUso(String email, int ignorarId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM nrt_cidadao WHERE LOWER(ds_email) = LOWER(?) AND id_cidadao <> ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setInt(2, ignorarId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    private Cidadao montar(ResultSet rs) throws SQLException {
        Cidadao c = new Cidadao();
        c.setIdCidadao(rs.getInt("id_cidadao"));
        c.setIdBairro(rs.getInt("id_bairro"));
        c.setNome(rs.getString("nm_cidadao"));
        c.setDataNascimento(JdbcUtil.getData(rs, "dt_nascimento"));
        c.setEmail(rs.getString("ds_email"));
        c.setCelular(rs.getString("nr_celular"));
        c.setPcd(JdbcUtil.deSN(rs.getString("st_pcd")));
        c.setDataCadastro(JdbcUtil.getDataHora(rs, "dt_cadastro"));
        c.setDataAceiteTermos(JdbcUtil.getDataHora(rs, "dt_aceite_termos"));
        return c;
    }
}
