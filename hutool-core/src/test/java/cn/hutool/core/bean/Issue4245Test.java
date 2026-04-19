package cn.hutool.core.bean;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * https://github.com/chinabugotech/hutool/issues/4245
 */
public class Issue4245Test {
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	static class Order {
		private Long orderId;
		private String orderNo;
		private List<OrderItem> orderItemList;
	}

	@Getter
	@Setter(AccessLevel.PROTECTED)
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	@NoArgsConstructor
	static class OrderItem {
		private Long itemId;
		private String productName;
		private Integer quantity;
	}

	@Data
	static class OrderDto {
		private Long orderId;
		private String orderNo;
		private List<OrderItemDto> orderItemList;
	}

	@Data
	static class OrderItemDto {
		private Long itemId;
		private String productName;
		private Integer quantity;
	}

	@Test
	public void testCopyPropertiesWithProtectedSetter() {
		Order order = new Order(1L, "01", new ArrayList<>());
		order.getOrderItemList().add(new OrderItem(1L, "aa", 1));

		OrderDto dto = BeanUtil.copyProperties(order, OrderDto.class);

		Assertions.assertNotNull(dto);
		Assertions.assertEquals(Long.valueOf(1L), dto.getOrderId());
		Assertions.assertEquals("01", dto.getOrderNo());
		Assertions.assertNotNull(dto.getOrderItemList());
	}
}
