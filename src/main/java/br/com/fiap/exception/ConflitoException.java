package br.com.fiap.exception;

/**
 * Operacao entra em conflito com dados ja existentes (HTTP 409).
 * Ex.: segundo plano para a mesma solicitacao, excluir cidadao que tem solicitacoes.
 */
public class ConflitoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ConflitoException(String mensagem) {
        super(mensagem);
    }
}
