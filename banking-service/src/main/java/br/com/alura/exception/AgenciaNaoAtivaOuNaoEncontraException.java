package br.com.alura.exception;

public class AgenciaNaoAtivaOuNaoEncontraException extends RuntimeException {

    public AgenciaNaoAtivaOuNaoEncontraException(String message) {
        super(message);
    }
}
