package mate.academy.bookstore.service;

import java.util.List;
import mate.academy.bookstore.dto.order.CreateOrderRequestDto;
import mate.academy.bookstore.dto.order.OrderDto;
import mate.academy.bookstore.dto.order.UpdateOrderStatusRequestDto;
import mate.academy.bookstore.dto.orderitem.OrderItemDto;

public interface OrderService {
    OrderDto save(Long userId, CreateOrderRequestDto requestDto);

    List<OrderDto> findAll(Long userId);

    List<OrderItemDto> findAllOrderItemsByOrderId(Long userId, Long orderId);

    OrderItemDto findOrderItemById(Long userId, Long itemId, Long orderId);

    OrderDto update(UpdateOrderStatusRequestDto requestDto, Long orderId);
}
