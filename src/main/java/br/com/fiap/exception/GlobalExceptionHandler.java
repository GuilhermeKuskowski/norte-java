package br.com.fiap.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Camada de tratamento de excecoes: transforma cada excecao em uma resposta JSON
 * com o codigo HTTP correto, em vez de devolver a pilha de erro do Java.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResposta> naoEncontrado(RecursoNaoEncontradoException e, HttpServletRequest req) {
        return montar(HttpStatus.NOT_FOUND, e.getMessage(), req);
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ErroResposta> regraDeNegocio(RegraDeNegocioException e, HttpServletRequest req) {
        return montar(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), req);
    }

    @ExceptionHandler(ConflitoException.class)
    public ResponseEntity<ErroResposta> conflito(ConflitoException e, HttpServletRequest req) {
        return montar(HttpStatus.CONFLICT, e.getMessage(), req);
    }

    @ExceptionHandler(BancoDeDadosException.class)
    public ResponseEntity<ErroResposta> bancoDeDados(BancoDeDadosException e, HttpServletRequest req) {
        // Traduz os erros ORA- mais comuns para mensagens claras
        switch (e.getCodigoOracle()) {
            case 1:    // ORA-00001: unique constraint violated
                return montar(HttpStatus.CONFLICT, "Registro duplicado: ja existe um registro com esses dados.", req);
            case 2291: // ORA-02291: integrity constraint violated - parent key not found
                return montar(HttpStatus.UNPROCESSABLE_ENTITY, "Registro relacionado (chave estrangeira) nao existe.", req);
            case 2292: // ORA-02292: integrity constraint violated - child record found
                return montar(HttpStatus.CONFLICT, "Nao e possivel excluir: existem registros ligados a este.", req);
            case 2290: // ORA-02290: check constraint violated
                return montar(HttpStatus.UNPROCESSABLE_ENTITY, "Valor fora do permitido pelo banco (CHECK).", req);
            default:
                return montar(HttpStatus.SERVICE_UNAVAILABLE, "Erro ao acessar o banco de dados. Tente novamente.", req);
        }
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResposta> jsonInvalido(HttpMessageNotReadableException e, HttpServletRequest req) {
        return montar(HttpStatus.BAD_REQUEST, "JSON invalido ou com campo em formato errado (datas: AAAA-MM-DD ou AAAA-MM-DDTHH:MM:SS).", req);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResposta> parametroInvalido(MethodArgumentTypeMismatchException e, HttpServletRequest req) {
        return montar(HttpStatus.BAD_REQUEST, "Parametro '" + e.getName() + "' com valor invalido: " + e.getValue(), req);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErroResposta> metodoNaoSuportado(HttpRequestMethodNotSupportedException e, HttpServletRequest req) {
        return montar(HttpStatus.METHOD_NOT_ALLOWED, "Metodo " + e.getMethod() + " nao suportado neste endereco.", req);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErroResposta> rotaInexistente(NoResourceFoundException e, HttpServletRequest req) {
        return montar(HttpStatus.NOT_FOUND, "Endereco nao encontrado.", req);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResposta> erroGenerico(Exception e, HttpServletRequest req) {
        e.printStackTrace();
        return montar(HttpStatus.INTERNAL_SERVER_ERROR, "Erro inesperado no servidor.", req);
    }

    private ResponseEntity<ErroResposta> montar(HttpStatus status, String mensagem, HttpServletRequest req) {
        ErroResposta corpo = new ErroResposta(status.value(), status.getReasonPhrase(), mensagem, req.getRequestURI());
        return ResponseEntity.status(status).body(corpo);
    }
}
