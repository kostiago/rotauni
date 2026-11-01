package br.com.rotauni.api.comum;

public class RecursoNaoEncontradoException extends RuntimeException {
    public RecursoNaoEncontradoException (String mensagem) {
        super (mensagem);
    }
}
