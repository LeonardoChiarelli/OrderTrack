package br.com.OrderTrack.Order.application.exception;

public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
}
