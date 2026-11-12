package br.com.fiap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import br.com.fiap.model.PontoAtendimento;

/**
 * Acesso a tabela NRT_PONTO_ATENDIMENTO (consulta).
 */
public class PontoAtendimentoDAO {

    private Connection con;

    public PontoAtendimentoDAO(Connection con) {
        this.con = con;
    }

    public Connection getCon() {
        return con;
    }

    public PontoAtendimento buscarPorId(int idPonto) throws SQLException {
        String sql = "SELECT * FROM nrt_ponto_atendimento WHERE id_ponto = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPonto);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? montar(rs) : null;
            }
        }
    }

    /** Lista pontos gratuitos, com filtros opcionais: tipo (ex.: PROCON) e somente acessiveis. */
    public ArrayList<PontoAtendimento> listar(String tipo, boolean somenteAcessiveis) throws SQLException {
        String sql = "SELECT * FROM nrt_ponto_atendimento "
                + "WHERE st_gratuito = 'S' "
                + "AND (? IS NULL OR tp_ponto = ?) "
                + "AND (? = 'N' OR st_acessivel = 'S') "
                + "ORDER BY tp_ponto, nm_ponto";
        ArrayList<PontoAtendimento> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tipo);
            ps.setString(2, tipo);
            ps.setString(3, JdbcUtil.paraSN(somenteAcessiveis));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(montar(rs));
                }
            }
        }
        return lista;
    }

    private PontoAtendimento montar(ResultSet rs) throws SQLException {
        return new PontoAtendimento(rs.getInt("id_ponto"), rs.getInt("id_bairro"), rs.getString("nm_ponto"),
                rs.getString("tp_ponto"), rs.getString("ds_endereco"), rs.getString("nr_telefone"),
                JdbcUtil.deSN(rs.getString("st_acessivel")), JdbcUtil.deSN(rs.getString("st_gratuito")));
    }
}
