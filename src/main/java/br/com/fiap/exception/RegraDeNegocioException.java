package br.com.fiap.exception;

/**
 * Dado recebido fere uma regra de negocio do Norte (HTTP 422).
 * Ex.: relato curto demais, data de agendamento no passado, ponto sem acessibilidade para pessoa com deficiencia.
 */
public class RegraDeNegocioException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RegraDeNegocioException(String mensagem) {
        super(mensagem);
    }
}
