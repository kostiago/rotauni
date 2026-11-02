package br.com.rotauni.api.comum;

import org.springframework.http.HttpHeaders;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class TratadordeErros extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(TratadordeErros.class);

    public record CampoInvalido(String campo, String mensagem) {
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ProblemDetail tratarNaoEncontrado(RecursoNaoEncontradoException ex) {
        var problema = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problema.setTitle("Recurso não encontrado");

        return problema;
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ProblemDetail tratarRegraNegocio(RegraNegocioException ex) {
        var problema = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage());
        problema.setTitle("Regra de negócio violada");
        return problema;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail tratarIntegridade(DataIntegrityViolationException ex) {
        log.warn("Violação de integridade no banco", ex);
        var problema = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
                "A operação conflita com dados já existentes");
        problema.setTitle("Conflito de Dados");
        return problema;

    }

    public ProblemDetail tratarInesperado(Exception ex) {
        log.error("Erro inesperado", ex);
        var problema = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocorreu um erro inesperado. Tente novamente mais tarde.");
        problema.setTitle("Erro Interno");
        return problema;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<CampoInvalido> erros = ex.getBindingResult().getFieldErrors().stream()
                .map(erro -> new CampoInvalido(erro.getField(), erro.getDefaultMessage())).toList();
        var problema = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Um ou mais campos estão inválidos");
        problema.setTitle("Dados inválidos");
        problema.setProperty("erros", erros);

        return ResponseEntity.badRequest().body(problema);
    }


    protected ResponseEntity<Object> handleHttpMessageNotReadable(
        HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request
    ){
        var problema = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "O corpo da requisição não é um JSON válido");
        problema.setTitle("Requisição malformada");
        return ResponseEntity.badRequest().body(problema);
    }

}
