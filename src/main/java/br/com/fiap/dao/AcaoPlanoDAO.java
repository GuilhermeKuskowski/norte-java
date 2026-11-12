package br.com.fiap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import br.com.fiap.model.AcaoPlano;

/**
 * Acesso a tabela NRT_ACAO_PLANO (passos do plano).
 */
public class AcaoPlanoDAO {

    private Connection con;

    public AcaoPlanoDAO(Connection con) {
        this.con = con;
    }

    public Connection getCon() {
        return con;
    }

    public int proximoId() throws SQLException {
        return JdbcUtil.proximoId(con, "nrt_acao_plano", "id_acao");
    }

    public void inserir(AcaoPlano a) throws SQLException {
        String sql = "INSERT INTO nrt_acao_plano (id_acao, id_plano, id_lei, nr_ordem, ds_acao, dt_prazo, "
                + "tp_semaforo, st_concluida) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, a.getIdAcao());
            ps.setInt(2, a.getIdPlano());
            JdbcUtil.setInteiro(ps, 3, a.getIdLei());
            ps.setInt(4, a.getOrdem());
            ps.setString(5, a.getDescricao());
            JdbcUtil.setData(ps, 6, a.getPrazo());
            ps.setString(7, a.getSemaforo());
            ps.setString(8, JdbcUtil.paraSN(a.isConcluida()));
            ps.executeUpdate();
        }
    }

    public boolean alterar(AcaoPlano a) throws SQLException {
        String sql = "UPDATE nrt_acao_plano SET id_lei = ?, nr_ordem = ?, ds_acao = ?, dt_prazo = ?, "
                + "tp_semaforo = ?, st_concluida = ? WHERE id_acao = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            JdbcUtil.setInteiro(ps, 1, a.getIdLei());
            ps.setInt(2, a.getOrdem());
            ps.setString(3, a.getDescricao());
            JdbcUtil.setData(ps, 4, a.getPrazo());
            ps.setString(5, a.getSemaforo());
            ps.setString(6, JdbcUtil.paraSN(a.isConcluida()));
            ps.setInt(7, a.getIdAcao());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean excluir(int idAcao) throws SQLException {
        String sql = "DELETE FROM nrt_acao_plano WHERE id_acao = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idAcao);
            return ps.executeUpdate() > 0;
        }
    }

    public int excluirPorPlano(int idPlano) throws SQLException {
        String sql = "DELETE FROM nrt_acao_plano WHERE id_plano = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPlano);
            return ps.executeUpdate();
        }
    }

    public AcaoPlano buscarPorId(int idAcao) throws SQLException {
        String sql = "SELECT * FROM nrt_acao_plano WHERE id_acao = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idAcao);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? montar(rs) : null;
            }
        }
    }

    public ArrayList<AcaoPlano> listarPorPlano(int idPlano) throws SQLException {
        String sql = "SELECT * FROM nrt_acao_plano WHERE id_plano = ? ORDER BY nr_ordem";
        ArrayList<AcaoPlano> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPlano);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(montar(rs));
                }
            }
        }
        return lista;
    }

    public boolean ordemEmUso(int idPlano, int ordem, int ignorarIdAcao) throws SQLException {
        String sql = "SELECT COUNT(*) FROM nrt_acao_plano WHERE id_plano = ? AND nr_ordem = ? AND id_acao <> ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPlano);
            ps.setInt(2, ordem);
            ps.setInt(3, ignorarIdAcao);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    private AcaoPlano montar(ResultSet rs) throws SQLException {
        AcaoPlano a = new AcaoPlano();
        a.setIdAcao(rs.getInt("id_acao"));
        a.setIdPlano(rs.getInt("id_plano"));
        a.setIdLei(JdbcUtil.getInteiro(rs, "id_lei"));
        a.setOrdem(rs.getInt("nr_ordem"));
        a.setDescricao(rs.getString("ds_acao"));
        a.setPrazo(JdbcUtil.getData(rs, "dt_prazo"));
        a.setSemaforo(rs.getString("tp_semaforo"));
        a.setConcluida(JdbcUtil.deSN(rs.getString("st_concluida")));
        return a;
    }
}
