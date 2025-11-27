package br.com.OrderTrack.Payment.domain.controller;

import br.com.OrderTrack.Payment.domain.dto.MakePaymentDTO;
import br.com.OrderTrack.Payment.domain.dto.PaymentDetailDTO;
import br.com.OrderTrack.Payment.domain.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("orderTrack/payment")
public class PaymentController {

    @Autowired
    private PaymentService service;

    @Autowired
    private HttpServletRequest request;

    @PostMapping
    @Transactional
    public ResponseEntity<PaymentDetailDTO> realizePayment(@RequestBody @Valid MakePaymentDTO dto, UriComponentsBuilder builder /* DTO QUE RECEBE AS MENSAGENS */) {
        var payment = service.makePayment(dto);

        var uri = builder.path("/{id}").buildAndExpand(payment.getId()).toUri();

        return ResponseEntity.created(uri).body(new PaymentDetailDTO(payment));
    }

    @GetMapping("/admin/listAll")
    public ResponseEntity<Page<PaymentDetailDTO>> listAllPayments(Pageable pageable) {
        return ResponseEntity.ok(service.listaAllPayments(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDetailDTO> getPayment(@PathVariable Long id) {
        return ResponseEntity.ok(new PaymentDetailDTO(service.getPayment(id)));
    }

    @PatchMapping({"/{id}/approve", "/{id}/reject", "/{id}/cancel"})
    @Transactional
    public ResponseEntity<PaymentDetailDTO> changePaymentStatus(@PathVariable Long id){
        var payment = service.changePaymentStatus(id, request.getRequestURL().toString());

        return ResponseEntity.ok(new PaymentDetailDTO(payment));
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity<String> deletePaymentWithStatusCanceled(){

        service.deletePaymentsWithStatusCancelled();

        return  ResponseEntity.ok("Delete all payments with status canceled");
    }
}
