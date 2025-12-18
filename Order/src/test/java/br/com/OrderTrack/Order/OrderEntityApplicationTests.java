package br.com.OrderTrack.Order;

import br.com.OrderTrack.Order.domain.exception.InvalidOrderStateException;
import br.com.OrderTrack.Order.domain.model.Order;
import br.com.OrderTrack.Order.domain.model.OrderItem;
import br.com.OrderTrack.Order.domain.model.OrderStatus;
import br.com.OrderTrack.Order.domain.model.valueObject.Address;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderEntityApplicationTests {

	@Test
	@DisplayName("Should create order with pending stock status")
	void createOrderSuccess() {
		var address = Address.builder()
				.street("Rua A").number("10").neighborhood("Bairro").city("Cidade").state("SP").postalCode("12345678")
				.build();

		var item = OrderItem.builder().productId(UUID.randomUUID()).quantity(1).unitPrice(BigDecimal.TEN).build();

		var order = Order.builder()
				.consumerName("John Doe")
				.consumerEmail("john@email.com")
				.shippingAddress(address)
				.items(List.of(item))
				.totalPrice(BigDecimal.TEN)
				.build();

		assertNotNull(order.getId());
		assertEquals(OrderStatus.PENDING_STOCK, order.getStatus());
	}

	@Test
	@DisplayName("Should throw exception when marking as paid if status is incorrect")
	void markAsPaidError() {
		var address = Address.builder()
				.street("Rua A").number("10").neighborhood("Bairro").city("Cidade").state("SP").postalCode("12345678")
				.build();
		var item = OrderItem.builder().productId(UUID.randomUUID()).quantity(1).unitPrice(BigDecimal.TEN).build();

		var order = Order.builder()
				.consumerName("John").consumerEmail("john@email.com").shippingAddress(address)
				.items(List.of(item)).totalPrice(BigDecimal.TEN).build();

		// Status inicial é PENDING_STOCK, não pode ir direto para PAID (precisa ser PENDING_PAYMENT antes)
		assertThrows(InvalidOrderStateException.class, order::markAsPaid);
	}

}
