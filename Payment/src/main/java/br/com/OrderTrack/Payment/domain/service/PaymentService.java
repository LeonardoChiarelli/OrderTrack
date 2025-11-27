package br.com.OrderTrack.Payment.domain.service;

import br.com.OrderTrack.Payment.domain.dto.MakePaymentDTO;
import br.com.OrderTrack.Payment.domain.dto.PaymentDetailDTO;
import br.com.OrderTrack.Payment.domain.model.Payment;
import br.com.OrderTrack.Payment.domain.model.PaymentStatus;
import br.com.OrderTrack.Payment.domain.repository.IOrderRepository;
import br.com.OrderTrack.Payment.domain.repository.IPaymentMethodRepository;
import br.com.OrderTrack.Payment.domain.repository.IPaymentRepository;
import br.com.OrderTrack.Payment.domain.repository.IProductRepository;
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

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IProductRepository productRepository;

    public Payment makePayment(@Valid MakePaymentDTO dtoPayment /*, DTO Recebido AMQP */) {

        /*
        CREATE ORDER, PRODUCT MODELS AND SAVE AT DATABASE
         */

        var existingPayment = paymentRepository.existsByStatusAndOrder(/* DTO AMQP */);
        if (existingPayment) { throw new ValidationException("Payment already exists"); }

        var order = orderRepository.findById(/* DTO AMQP */).orElseThrow(() -> new ValidationException("Order not found"));
        var method = paymentMethodRepository.findById(dtoPayment.paymentMethodId()).orElseThrow(() -> new ValidationException("Payment Method not found"));
        var payment = new Payment(order, dtoPayment, method);
        paymentRepository.save(payment);

        return payment;
    }

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
