package br.com.OrderTrack.Order.application.exception;

public class InventoryException extends RuntimeException {
    public InventoryException(String message) {
        super(message);
    }
}
