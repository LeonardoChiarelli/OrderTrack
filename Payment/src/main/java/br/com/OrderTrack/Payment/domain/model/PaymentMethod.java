package br.com.OrderTrack.Payment.domain.model;

import br.com.OrderTrack.Payment.domain.exception.DomainException;

import java.util.Objects;
import java.util.UUID;

public class PaymentMethod {
    private final UUID id;
    private final String name;
    private final boolean active;

    private PaymentMethod(String name, boolean active) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.active = active;
    }

    private PaymentMethod(UUID id, String name, boolean active) {
        this.id = id;
        this.name = name;
        this.active = active;
    }

    public static PaymentMethodBuilder builder() {
        return new PaymentMethodBuilder();
    }

    public void validateIsActive() {
        if (!this.active) {
            throw new DomainException(String.format("Payment method  %s is currently inactive.", this.name));
        }
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PaymentMethod that = (PaymentMethod) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PaymentMethod{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", active=" + active +
                '}';
    }

    public static class PaymentMethodBuilder {
        private UUID id;
        private String name;
        private boolean active;

        public PaymentMethodBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public PaymentMethodBuilder name(String name) {
            this.name = name;
            return this;
        }

        public PaymentMethodBuilder active(boolean active) {
            this.active = active;
            return this;
        }

        public PaymentMethod build() {
            if (name.isEmpty()) {
                throw new DomainException("Payment method name cannot be empty");
            }
            if (id != null) {
                return  new PaymentMethod(id, name, active);
            }
            return new PaymentMethod(name, active);
        }
    }
}
