package br.com.OrderTrack.Order.domain.exception;

public class BusinessRule extends RuntimeException {
    public BusinessRule(String message) {
        super(message);
    }
}
