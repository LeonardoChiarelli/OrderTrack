package br.com.OrderTrack.Payment.domain.controller;

import br.com.OrderTrack.Payment.domain.dto.PaymentMethodDetailsDTO;
import br.com.OrderTrack.Payment.domain.dto.RegistryAPaymentMethodDTO;
import br.com.OrderTrack.Payment.domain.service.PaymentMethodService;
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
@RequestMapping("orderTrack/admin/paymentMethod")
public class PaymentMethodController {

    @Autowired
    private PaymentMethodService service;

    @Autowired
    private HttpServletRequest request;

    @PostMapping("/registry")
    @Transactional
    public ResponseEntity<PaymentMethodDetailsDTO> registryAPaymentMethod(@RequestBody @Valid RegistryAPaymentMethodDTO dto, UriComponentsBuilder builder) {

        var paymentMethod = service.registry(dto);

        var uri = builder.path("/{id}").buildAndExpand(dto.id()).toUri();

        return ResponseEntity.created(uri).body(new PaymentMethodDetailsDTO(paymentMethod));
    }

    @GetMapping()
    public ResponseEntity<Page<PaymentMethodDetailsDTO>> getAllPaymentMethods(Pageable pageable) {
        return ResponseEntity.ok(service.listAllPaymentsMethods(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethodDetailsDTO> getPaymentMethod(@PathVariable Long id) {
        var payment = service.getPaymentMethod(id);
        return ResponseEntity.ok(new PaymentMethodDetailsDTO(payment));
    }


    @PatchMapping({"/{id}/active", "/{id}/deactivate"})
    @Transactional
    public ResponseEntity<PaymentMethodDetailsDTO> changeStatus(@PathVariable Long id) {
        return ResponseEntity.ok(new PaymentMethodDetailsDTO(service.changeStatus(id, request.getRequestURL().toString())));
    }
}
