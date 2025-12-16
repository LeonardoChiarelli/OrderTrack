package br.com.OrderTrack.Inventory.application.inventory.mapper;

import br.com.OrderTrack.Order.application.report.mapper.SalesStatisticsMapperRecord;

import java.math.BigDecimal;
import java.util.List;

public record InventoryBillingMapperRecord(BigDecimal totalBilling, List<SalesStatisticsMapperRecord> statistics) {
}
