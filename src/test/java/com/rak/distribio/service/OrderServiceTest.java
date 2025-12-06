package com.rak.distribio.service;

import com.rak.distribio.entity.Order;
import com.rak.distribio.entity.Product;
import com.rak.distribio.entity.Sale;
import com.rak.distribio.repository.OrderRepository;
import com.rak.distribio.repository.ProductRepository;
import com.rak.distribio.repository.SaleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private SaleRepository saleRepository;

	@Mock
	private ProductRepository productRepository;

	@Mock
	private UserService userService;

	@Mock
	private ReferralService referralService;

	@InjectMocks
	private OrderService orderService;

	private Product testProduct;
	private Order testOrder;
	private Sale testSale;

	@BeforeEach
	void setUp() {
		testProduct = new Product();
		testProduct.setId(1L);
		testProduct.setSku("PROD001");
		testProduct.setName("Test Product");
		testProduct.setPrice(new BigDecimal("1000.00"));
		testProduct.setAvailable(true);

		testOrder = new Order();
		testOrder.setOrderId(1L);
		testOrder.setProductId(1L);
		testOrder.setBuyerUserId(2L);
		testOrder.setSellerUserId(1L);
		testOrder.setPaymentProofUrl("TXN123");
		testOrder.setAmount(new BigDecimal("1000.00"));
		testOrder.setStatus("PAID");

		testSale = new Sale();
		testSale.setId(1L);
		testSale.setSellerUserId(1L);
		testSale.setBuyerId(2L);
		testSale.setTotalAmount(new BigDecimal("1000.00"));
	}

	@Test
	void testCreateOrder_Success() {
		when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
		when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
			Order order = invocation.getArgument(0);
			order.setOrderId(1L);
			return order;
		});
		when(saleRepository.save(any(Sale.class))).thenAnswer(invocation -> {
			Sale sale = invocation.getArgument(0);
			sale.setId(1L);
			return sale;
		});
		doNothing().when(userService).updateLastSaleAt(1L);
		when(referralService.calculateAndDistributeCommissions(any(Sale.class)))
			.thenReturn(new java.util.ArrayList<>());

		Order result = orderService.createOrder(1L, 2L, 1L, "TXN123", new BigDecimal("1000.00"), "TEST", "123", "TEST", 1L);

		assertNotNull(result);
		assertEquals("PAID", result.getStatus());
		assertEquals("TXN123", result.getPaymentProofUrl());

		// Verify order was saved twice (PAYMENT_PENDING then PAID)
		verify(orderRepository, times(2)).save(any(Order.class));

		// Verify sale was created
		ArgumentCaptor<Sale> saleCaptor = ArgumentCaptor.forClass(Sale.class);
		verify(saleRepository).save(saleCaptor.capture());
		Sale capturedSale = saleCaptor.getValue();
		assertEquals(1L, capturedSale.getSellerUserId());
		assertEquals(2L, capturedSale.getBuyerId());
		assertEquals(new BigDecimal("1000.00"), capturedSale.getTotalAmount());

		// Verify user service and referral service were called
		verify(userService).updateLastSaleAt(1L);
		verify(referralService).calculateAndDistributeCommissions(any(Sale.class));
	}

	@Test
	void testCreateOrder_ProductNotFound() {
		when(productRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(IllegalArgumentException.class, () -> {
			orderService.createOrder(1L, 2L, 1L, "TXN123", new BigDecimal("1000.00"), "TEST", "123", "TEST", 1L);
		});

		verify(orderRepository, never()).save(any());
		verify(saleRepository, never()).save(any());
	}
}

