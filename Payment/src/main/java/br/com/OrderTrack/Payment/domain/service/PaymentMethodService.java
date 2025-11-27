package br.com.OrderTrack.Payment.domain.service;

import br.com.OrderTrack.Payment.domain.dto.PaymentMethodDetailsDTO;
import br.com.OrderTrack.Payment.domain.dto.RegistryAPaymentMethodDTO;
import br.com.OrderTrack.Payment.domain.model.PaymentMethod;
import br.com.OrderTrack.Payment.domain.repository.IPaymentMethodRepository;
import br.com.OrderTrack.Payment.general.infra.exceptions.ValidationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PaymentMethodService {

    @Autowired
    private IPaymentMethodRepository repository;

    public PaymentMethod registry(@Valid RegistryAPaymentMethodDTO dto) {
        var existingPaymentMethod = repository.existsByName(dto.name());
        if (existingPaymentMethod) { throw new ValidationException("PaymentMethod already exists"); }

        var paymentMethod = new PaymentMethod(dto);
        repository.save(paymentMethod);

        return paymentMethod;
    }

    public PaymentMethod changeStatus(Long id, String requestURL) {
        var paymentMethod = repository.findById(id).orElseThrow(() -> new ValidationException("PaymentMethod not found"));

        if (requestURL.equals("http://localhost:8081/orderTrack/paymentMethod/active")) {
            if (paymentMethod.getActive()) { throw new ValidationException("Payment method was already active"); }
            paymentMethod.changeStatus(true);
        }
        if (requestURL.equals("http://localhost:8081/orderTrack/paymentMethod/deactivate")) {
            if (!paymentMethod.getActive()) { throw new ValidationException("Payment method was already deactivate"); }
            paymentMethod.changeStatus(false);
        }
        return paymentMethod;
    }

    public Page<PaymentMethodDetailsDTO> listAllPaymentsMethods(Pageable pageable) {
        return repository.findAll(pageable).map(PaymentMethodDetailsDTO::new);
    }

    public PaymentMethod getPaymentMethod(Long id) {
        return repository.findById(id).orElseThrow(() -> new ValidationException("Payment Method not found"));
    }
}
