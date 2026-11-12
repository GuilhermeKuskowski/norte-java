package br.com.fiap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import br.com.fiap.model.ItemChecklist;
import br.com.fiap.model.PlanoAcao;

/**
 * Acesso as tabelas NRT_PLANO_ACAO e NRT_PLANO_DOCUMENTO (checklist do plano).
 */
public class PlanoAcaoDAO {

    private Connection con;

    public PlanoAcaoDAO(Connection con) {
        this.con = con;
    }

    public Connection getCon() {
        return con;
    }

    public int proximoId() throws SQLException {
        return JdbcUtil.proximoId(con, "nrt_plano_acao", "id_plano");
    }

    public void inserir(PlanoAcao p) throws SQLException {
        String sql = "INSERT INTO nrt_plano_acao (id_plano, id_solicitacao, dt_geracao, st_precisa_advogado, ds_resumo) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.getIdPlano());
            ps.setInt(2, p.getIdSolicitacao());
            JdbcUtil.setDataHora(ps, 3, p.getDataGeracao());
            ps.setString(4, JdbcUtil.paraSN(p.isPrecisaAdvogado()));
            ps.setString(5, p.getResumo());
            ps.executeUpdate();
        }
    }

    public boolean alterar(PlanoAcao p) throws SQLException {
        String sql = "UPDATE nrt_plano_acao SET st_precisa_advogado = ?, ds_resumo = ? WHERE id_plano = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, JdbcUtil.paraSN(p.isPrecisaAdvogado()));
            ps.setString(2, p.getResumo());
            ps.setInt(3, p.getIdPlano());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean excluir(int idPlano) throws SQLException {
        String sql = "DELETE FROM nrt_plano_acao WHERE id_plano = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPlano);
            return ps.executeUpdate() > 0;
        }
    }

    public PlanoAcao buscarPorId(int idPlano) throws SQLException {
        String sql = "SELECT * FROM nrt_plano_acao WHERE id_plano = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPlano);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? montar(rs) : null;
            }
        }
    }

    public PlanoAcao buscarPorSolicitacao(int idSolicitacao) throws SQLException {
        String sql = "SELECT * FROM nrt_plano_acao WHERE id_solicitacao = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSolicitacao);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? montar(rs) : null;
            }
        }
    }

    public ArrayList<PlanoAcao> listarTodos() throws SQLException {
        String sql = "SELECT * FROM nrt_plano_acao ORDER BY id_plano";
        ArrayList<PlanoAcao> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(montar(rs));
            }
        }
        return lista;
    }

    // ---------------- checklist (NRT_PLANO_DOCUMENTO) ----------------

    public void inserirItemChecklist(int idPlano, ItemChecklist item) throws SQLException {
        String sql = "INSERT INTO nrt_plano_documento (id_plano, id_documento, st_possui) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPlano);
            ps.setInt(2, item.getIdDocumento());
            ps.setString(3, JdbcUtil.paraSN(item.isPossui()));
            ps.executeUpdate();
        }
    }

    public int excluirChecklist(int idPlano) throws SQLException {
        String sql = "DELETE FROM nrt_plano_documento WHERE id_plano = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPlano);
            return ps.executeUpdate();
        }
    }

    public ArrayList<ItemChecklist> listarChecklist(int idPlano) throws SQLException {
        String sql = "SELECT d.id_documento, d.nm_documento, d.ds_orientacao, pd.st_possui "
                + "FROM nrt_plano_documento pd "
                + "INNER JOIN nrt_documento d ON d.id_documento = pd.id_documento "
                + "WHERE pd.id_plano = ? ORDER BY d.id_documento";
        ArrayList<ItemChecklist> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPlano);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new ItemChecklist(rs.getInt("id_documento"), rs.getString("nm_documento"),
                            rs.getString("ds_orientacao"), JdbcUtil.deSN(rs.getString("st_possui"))));
                }
            }
        }
        return lista;
    }

    public boolean documentoExiste(int idDocumento) throws SQLException {
        return JdbcUtil.existe(con, "nrt_documento", "id_documento", idDocumento);
    }

    private PlanoAcao montar(ResultSet rs) throws SQLException {
        PlanoAcao p = new PlanoAcao();
        p.setIdPlano(rs.getInt("id_plano"));
        p.setIdSolicitacao(rs.getInt("id_solicitacao"));
        p.setDataGeracao(JdbcUtil.getDataHora(rs, "dt_geracao"));
        p.setPrecisaAdvogado(JdbcUtil.deSN(rs.getString("st_precisa_advogado")));
        p.setResumo(rs.getString("ds_resumo"));
        return p;
    }
}
