package br.com.OrderTrack.Payment.domain.model;

import br.com.OrderTrack.Payment.domain.exception.DomainException;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public record PaymentDetails(
        String holderName,
        String cardNumber,
        String expirationDate,
        String securityCode
) {
    public PaymentDetails {
        if (holderName == null || holderName.isBlank()) {
            throw new DomainException("Card holder name is required.");
        }
        if (cardNumber == null || !Pattern.matches("\\d{13,19}", cardNumber.replaceAll("\\s+", ""))) {
            throw new DomainException("Invalid card number format.");
        }
        if (securityCode == null || !Pattern.matches("\\d{3,4}", securityCode)) {
            throw new DomainException("Invalid security code.");
        }
        validateExpiration(expirationDate);
    }

    private void validateExpiration(String expirationDate) {
        try {
            var formatter = DateTimeFormatter.ofPattern("MM/yy");
            var expiry = YearMonth.parse(expirationDate, formatter);
            if (expiry.isBefore(YearMonth.now())) {
                throw new DomainException("Credit card is expired.");
            }
        } catch (Exception e) {
            throw new DomainException("Invalid expiration date format. Use MM/yy.");
        }
    }

    public String getMaskedNumber() {
        var cleanNumber = cardNumber.replaceAll("\\s+", "");
        int length = cleanNumber.length();
        return "*".repeat(length - 4) + cleanNumber.substring(length - 4);
    }
}
