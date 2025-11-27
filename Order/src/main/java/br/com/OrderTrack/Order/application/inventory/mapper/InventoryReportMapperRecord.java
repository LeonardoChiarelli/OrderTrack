package br.com.OrderTrack.Order.application.inventory.mapper;

import br.com.OrderTrack.Order.application.product.mapper.ProductMapperRecord;
import org.springframework.data.domain.Page;

public record InventoryReportMapperRecord(Page<ProductMapperRecord> productMapperRecordPage){
}
