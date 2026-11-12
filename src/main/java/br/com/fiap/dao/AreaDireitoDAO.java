package br.com.fiap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import br.com.fiap.model.AreaDireito;

/**
 * Acesso a tabela NRT_AREA_DIREITO (consulta).
 */
public class AreaDireitoDAO {

    private Connection con;

    public AreaDireitoDAO(Connection con) {
        this.con = con;
    }

    public Connection getCon() {
        return con;
    }

    public AreaDireito buscarPorId(int idArea) throws SQLException {
        String sql = "SELECT * FROM nrt_area_direito WHERE id_area = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idArea);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? montar(rs) : null;
            }
        }
    }

    public ArrayList<AreaDireito> listarTodas() throws SQLException {
        String sql = "SELECT * FROM nrt_area_direito ORDER BY id_area";
        ArrayList<AreaDireito> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(montar(rs));
            }
        }
        return lista;
    }

    private AreaDireito montar(ResultSet rs) throws SQLException {
        return new AreaDireito(rs.getInt("id_area"), rs.getString("nm_area"), rs.getString("ds_area"),
                JdbcUtil.deSN(rs.getString("st_modo_discreto")), JdbcUtil.deSN(rs.getString("st_trilha_calculo")));
    }
}
