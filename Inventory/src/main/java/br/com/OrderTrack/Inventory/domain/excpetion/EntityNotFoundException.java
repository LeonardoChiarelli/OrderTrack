package br.com.OrderTrack.Inventory.domain.excpetion;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
