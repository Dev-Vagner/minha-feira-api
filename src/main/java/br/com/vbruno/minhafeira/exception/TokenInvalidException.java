package br.com.vbruno.minhafeira.exception;

public class TokenInvalidException extends RuntimeException {
    public TokenInvalidException() {
        super("Token inválido");
    }
}
