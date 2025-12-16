package br.com.OrderTrack.Product.domain.excpetion;

public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
}
