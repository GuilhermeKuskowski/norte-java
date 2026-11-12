package br.com.fiap.exception;

import java.sql.SQLException;

/**
 * Falha ao acessar o Oracle (HTTP 503). Guarda o SQLException original
 * para o GlobalExceptionHandler traduzir os codigos ORA- mais comuns.
 */
public class BancoDeDadosException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final int codigoOracle;

    public BancoDeDadosException(SQLException causa) {
        super("Erro de banco de dados: " + causa.getMessage(), causa);
        this.codigoOracle = causa.getErrorCode();
    }

    public int getCodigoOracle() {
        return codigoOracle;
    }
}
