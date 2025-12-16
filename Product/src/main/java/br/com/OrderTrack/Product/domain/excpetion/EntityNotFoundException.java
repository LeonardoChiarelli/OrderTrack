package br.com.OrderTrack.Product.domain.excpetion;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
