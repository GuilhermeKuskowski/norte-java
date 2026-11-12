package br.com.fiap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import br.com.fiap.model.Bairro;

/**
 * Acesso a tabela NRT_BAIRRO (consulta).
 */
public class BairroDAO {

    private Connection con;

    public BairroDAO(Connection con) {
        this.con = con;
    }

    public Connection getCon() {
        return con;
    }

    public Bairro buscarPorId(int idBairro) throws SQLException {
        String sql = "SELECT * FROM nrt_bairro WHERE id_bairro = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idBairro);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? montar(rs) : null;
            }
        }
    }

    public ArrayList<Bairro> listarTodos() throws SQLException {
        String sql = "SELECT * FROM nrt_bairro ORDER BY nm_bairro";
        ArrayList<Bairro> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(montar(rs));
            }
        }
        return lista;
    }

    private Bairro montar(ResultSet rs) throws SQLException {
        return new Bairro(rs.getInt("id_bairro"), rs.getString("nm_bairro"),
                rs.getString("nm_distrito"), rs.getString("nm_subprefeitura"));
    }
}
