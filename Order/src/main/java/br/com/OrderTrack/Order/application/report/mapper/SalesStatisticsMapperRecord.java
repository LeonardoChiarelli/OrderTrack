package br.com.OrderTrack.Order.application.report.mapper;

import java.math.BigDecimal;

public record SalesStatisticsMapperRecord(String category, Long salesQuantity, BigDecimal billing) {
}
