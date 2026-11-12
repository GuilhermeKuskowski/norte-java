package br.com.fiap.exception;

/**
 * Registro procurado nao existe no banco (HTTP 404).
 */
public class RecursoNaoEncontradoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RecursoNaoEncontradoException(String recurso, int id) {
        super(recurso + " com id " + id + " nao encontrado(a).");
    }

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
