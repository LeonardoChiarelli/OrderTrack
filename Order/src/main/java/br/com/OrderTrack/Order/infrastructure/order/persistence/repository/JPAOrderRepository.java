package br.com.OrderTrack.Order.infrastructure.order.persistence.repository;

import br.com.OrderTrack.Order.application.report.mapper.SalesStatisticsMapperRecord;
import br.com.OrderTrack.Order.infrastructure.order.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface JPAOrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query("""
            SELECT SUM(i.unitPrice * i.quantity)
            FROM Order o
            JOIN o.items i
            WHERE o.orderDate = :yestedayDate
            """)
    BigDecimal getTotalYesterdayBilling(LocalDate yesterdayDate);

    @Query("""
            SELECT NEW  br.com.OrderTrack.Order.domain.mapper.SalesStatisticsMapperRecord(
            productEntity.category,
            SUM(i.quantity),
            SUM(i.unitPrice * i.quantity)
            )
            FROM Order o
            JOIN o.items i
            JOIN i.productEntity productEntity
            WHERE o.orderDate = :yesterdayDate
            GROUP BY productEntity.category
""")
    List<SalesStatisticsMapperRecord> getTotalYesterdayBillingByCategory(LocalDate yesterdayDate);
}
