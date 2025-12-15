package br.com.OrderTrack.Order.application.report;

import br.com.OrderTrack.Order.application.inventory.mapper.InventoryBillingMapperRecord;
import br.com.OrderTrack.Order.application.inventory.mapper.InventoryReportMapperRecord;
import br.com.OrderTrack.Order.application.product.mapper.ProductMapperRecord;
import br.com.OrderTrack.Order.infrastructure.inventory.IInventoryRepository;
import br.com.OrderTrack.Order.infrastructure.order.persistence.repository.JPAOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

@Service
public class ReportService {

    @Autowired
    private IInventoryRepository inventoryRepository;

    @Autowired
    private JPAOrderRepository orderRepository;

    @Async
    public CompletableFuture<InventoryReportMapperRecord> inventoryData(Pageable pageable) {

        var products = inventoryRepository.findAllProducts(pageable).map(ProductMapperRecord::new);
        var inventoryReport = new InventoryReportMapperRecord(products);

        return CompletableFuture.completedFuture(inventoryReport);
    }

    @Async
    public CompletableFuture<InventoryBillingMapperRecord> getBilling() {
        var yesterdayDate = LocalDate.now().minusDays(1);
        var totalBilling = orderRepository.getTotalYesterdayBilling(yesterdayDate);

        var statistics = orderRepository.getTotalYesterdayBillingByCategory(yesterdayDate);

        return CompletableFuture.completedFuture(new InventoryBillingMapperRecord(totalBilling, statistics));
    }
}
