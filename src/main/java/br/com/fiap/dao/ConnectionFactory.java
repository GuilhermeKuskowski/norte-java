package br.com.fiap.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.stereotype.Component;

/**
 * Fabrica de conexoes com o Oracle.
 * Usa o DataSource configurado no application.properties (DB_URL, DB_USER, DB_PASSWORD),
 * assim nenhuma credencial fica no codigo.
 */
@Component
public class ConnectionFactory {

    private final DataSource dataSource;

    public ConnectionFactory(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection abrirConexao() throws SQLException {
        return dataSource.getConnection();
    }

    public void fecharConexao(Connection con) {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao fechar a conexao: " + e.getMessage());
        }
    }
}
