package xyz.notagardener.common.error.exception;

public class GardenerNotInSessionException extends RuntimeException{
    public GardenerNotInSessionException(String message) {
        super(message);
    }
}
