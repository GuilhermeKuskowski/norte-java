package br.com.fiap.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Conversoes entre tipos Java e tipos do Oracle usadas pelos DAOs.
 * No banco, indicadores S/N sao CHAR(1); no Java viram boolean.
 */
public final class JdbcUtil {

    private JdbcUtil() {
    }

    public static String paraSN(boolean valor) {
        return valor ? "S" : "N";
    }

    public static boolean deSN(String valor) {
        return "S".equals(valor);
    }

    public static void setData(PreparedStatement ps, int indice, LocalDate data) throws SQLException {
        if (data == null) {
            ps.setNull(indice, Types.DATE);
        } else {
            ps.setDate(indice, Date.valueOf(data));
        }
    }

    public static void setDataHora(PreparedStatement ps, int indice, LocalDateTime dataHora) throws SQLException {
        if (dataHora == null) {
            ps.setNull(indice, Types.TIMESTAMP);
        } else {
            ps.setTimestamp(indice, Timestamp.valueOf(dataHora));
        }
    }

    public static void setInteiro(PreparedStatement ps, int indice, Integer valor) throws SQLException {
        if (valor == null) {
            ps.setNull(indice, Types.NUMERIC);
        } else {
            ps.setInt(indice, valor);
        }
    }

    public static void setDecimal(PreparedStatement ps, int indice, Double valor) throws SQLException {
        if (valor == null) {
            ps.setNull(indice, Types.NUMERIC);
        } else {
            ps.setDouble(indice, valor);
        }
    }

    public static LocalDate getData(ResultSet rs, String coluna) throws SQLException {
        Timestamp ts = rs.getTimestamp(coluna);
        return ts == null ? null : ts.toLocalDateTime().toLocalDate();
    }

    public static LocalDateTime getDataHora(ResultSet rs, String coluna) throws SQLException {
        Timestamp ts = rs.getTimestamp(coluna);
        return ts == null ? null : ts.toLocalDateTime();
    }

    public static Integer getInteiro(ResultSet rs, String coluna) throws SQLException {
        int valor = rs.getInt(coluna);
        return rs.wasNull() ? null : valor;
    }

    public static Double getDecimal(ResultSet rs, String coluna) throws SQLException {
        double valor = rs.getDouble(coluna);
        return rs.wasNull() ? null : valor;
    }

    /** Proximo id da tabela (MAX + 1). Simples e suficiente para o volume do projeto. */
    public static int proximoId(java.sql.Connection con, String tabela, String colunaId) throws SQLException {
        String sql = "SELECT NVL(MAX(" + colunaId + "), 0) + 1 FROM " + tabela;
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }

    /** Verifica se existe ao menos uma linha com o valor informado na coluna. */
    public static boolean existe(java.sql.Connection con, String tabela, String coluna, int valor) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + tabela + " WHERE " + coluna + " = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, valor);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }
}
