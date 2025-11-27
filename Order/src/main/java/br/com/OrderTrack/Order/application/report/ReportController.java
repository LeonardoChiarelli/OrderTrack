package br.com.OrderTrack.Order.application.report;

import br.com.OrderTrack.Order.application.inventory.mapper.InventoryBillingMapperRecord;
import br.com.OrderTrack.Order.application.inventory.mapper.InventoryReportMapperRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/orderTrack/admin/report")
public class ReportController {

    @Autowired
    private ReportService service;

    @GetMapping("/inventory")
    public ResponseEntity<CompletableFuture<InventoryReportMapperRecord>> getInventoryReport(@PageableDefault(sort = {"quantity"}) Pageable pageable) { return ResponseEntity.ok(service.inventoryData(pageable)); }

    @GetMapping("/billing")
    public ResponseEntity<CompletableFuture<InventoryBillingMapperRecord>> getYesterdayBilling() {
        return ResponseEntity.ok(service.getBilling());
    }
}
