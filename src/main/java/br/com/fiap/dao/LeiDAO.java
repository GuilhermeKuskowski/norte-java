package br.com.fiap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import br.com.fiap.model.Lei;

/**
 * Acesso as tabelas NRT_LEI e NRT_AREA_LEI (consulta).
 */
public class LeiDAO {

    private Connection con;

    public LeiDAO(Connection con) {
        this.con = con;
    }

    public Connection getCon() {
        return con;
    }

    public Lei buscarPorId(int idLei) throws SQLException {
        String sql = "SELECT * FROM nrt_lei WHERE id_lei = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idLei);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? montar(rs) : null;
            }
        }
    }

    public ArrayList<Lei> listarVigentes() throws SQLException {
        String sql = "SELECT * FROM nrt_lei WHERE st_revogada = 'N' ORDER BY id_lei";
        ArrayList<Lei> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(montar(rs));
            }
        }
        return lista;
    }

    /** Leis ligadas a uma area (tabela associativa NRT_AREA_LEI); as principais vem primeiro. */
    public ArrayList<Lei> listarPorArea(int idArea) throws SQLException {
        String sql = "SELECT l.* FROM nrt_lei l "
                + "INNER JOIN nrt_area_lei al ON al.id_lei = l.id_lei "
                + "WHERE al.id_area = ? AND l.st_revogada = 'N' "
                + "ORDER BY al.st_principal DESC, l.id_lei";
        ArrayList<Lei> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idArea);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(montar(rs));
                }
            }
        }
        return lista;
    }

    private Lei montar(ResultSet rs) throws SQLException {
        return new Lei(rs.getInt("id_lei"), rs.getString("tp_norma"), rs.getString("nr_norma"),
                JdbcUtil.getData(rs, "dt_norma"), rs.getString("nm_apelido"), rs.getString("ds_resumo"),
                rs.getString("ds_url_fonte"), JdbcUtil.deSN(rs.getString("st_revogada")));
    }
}
