package br.com.OrderTrack.Inventory.domain.excpetion;

public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
}
