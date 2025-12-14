package br.com.OrderTrack.Payment.domain.service;

import br.com.OrderTrack.Payment.domain.dto.MakePaymentDTO;
import br.com.OrderTrack.Payment.domain.dto.PaymentDetailDTO;
import br.com.OrderTrack.Payment.domain.model.Payment;
import br.com.OrderTrack.Payment.domain.model.PaymentStatus;
import br.com.OrderTrack.Payment.domain.repository.IPaymentMethodRepository;
import br.com.OrderTrack.Payment.domain.repository.IPaymentRepository;
import br.com.OrderTrack.Payment.general.infra.exceptions.ValidationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private IPaymentRepository paymentRepository;

    @Autowired
    private IPaymentMethodRepository paymentMethodRepository;

    public Payment changePaymentStatus(Long id, String requestURL){
        var payment = paymentRepository.findById(id).orElseThrow(() -> new ValidationException("Payment not found"));
        if (requestURL.equals("http://localhost:8081/orderTrack/payment/approve")) { payment.changeStatus(PaymentStatus.APPROVED); }
        if (requestURL.equals("http://localhost:8081/orderTrack/payment/reject")) { payment.changeStatus(PaymentStatus.REJECTED); }
        if (requestURL.equals("http://localhost:8081/orderTrack/payment/cancel")) { payment.changeStatus(PaymentStatus.CANCELLED); }

        return payment;
    }

    public Page<PaymentDetailDTO> listaAllPayments(Pageable pageable) {
        return paymentRepository.findAll(pageable).map(PaymentDetailDTO::new);
    }

    public Payment getPayment(Long id) {
        return paymentRepository.findById(id).orElseThrow(() -> new ValidationException("Payment not found"));
    }

    public void deletePaymentsWithStatusCancelled() {
        paymentRepository.deleteAllByStatusCancelled();
    }
}
