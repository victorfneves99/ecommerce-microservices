package com.ecommerce.order.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ecommerce.order.dto.OrderItemDTO;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OrderItem;
import com.ecommerce.order.model.OrderStatus;
import com.ecommerce.order.repository.OrdersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrdersRepository ordersRepository;

    private final CartService cartService;

    // public List<OrderResponse> findAllOrders() {
    // var orders = ordersRepository.findAll();
    // return orders.stream()
    // .map(OrderResponse::from)
    // .collect(Collectors.toList());
    // }

    // public OrderResponse findById(Long id) {
    // var order = ordersRepository.findById(id).orElseThrow();
    // return OrderResponse.from(order);
    // }

    public Optional<OrderResponse> createOrder(String userId) {

        // validate for cart items

        List<CartItem> cartItems = cartService.findAllItemsByUser(userId);

        if (cartItems.isEmpty()) {
            return Optional.empty();

        }

        // validate for user

        // var userOpt = userRepository.findById(Long.valueOf(userId));
        // if (userOpt.isEmpty()) {
        // return Optional.empty();

        // }
        // var user = userOpt.get();

        // calculate for total price

        var totalPrice = cartItems.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // create order

        var order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(totalPrice);
        order.setStatus(OrderStatus.CONFIRMED);
        var orderItems = cartItems.stream()
                .map(item -> new OrderItem(null,item.getProductId(), item.getQuantity(), item.getPrice(),order))
                .toList();

        order.setItems(orderItems);

        var savedOrder = ordersRepository.save(order);

        // clear cart

        cartService.clearCart(userId);

        return Optional.of(mapToOrderResponse(savedOrder));

    }

    private OrderResponse mapToOrderResponse(Order saveOrderRepository) {
        return new OrderResponse(saveOrderRepository.getId(),
                saveOrderRepository.getTotalAmount(),
                saveOrderRepository.getStatus(),
                saveOrderRepository.getItems().stream().map(item -> new OrderItemDTO(item.getId(),
                        item.getProductId(), item.getQuantity(), item.getPrice(),
                        item.getPrice().multiply(new BigDecimal(item.getQuantity())))).toList(),
                saveOrderRepository.getCreatedAt());
    }

    // public OrderResponse updateOrder(Long id, OrderRequest orderRequest) {
    // var order = ordersRepository.findById(id).orElseThrow();
    // order.setUser(orderRequest.getUser());
    // order.setTotalAmount(orderRequest.getTotalAmount());
    // order.setStatus(orderRequest.getStatus());
    // var updatedOrder = ordersRepository.save(order);
    // return OrderResponse.from(updatedOrder);
    // }

    // public void deleteOrder(Long id) {
    // ordersRepository.deleteById(id);
    // }
}