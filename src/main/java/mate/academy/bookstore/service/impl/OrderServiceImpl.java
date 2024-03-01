package mate.academy.bookstore.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.order.CreateOrderRequestDto;
import mate.academy.bookstore.dto.order.OrderDto;
import mate.academy.bookstore.dto.order.UpdateOrderStatusRequestDto;
import mate.academy.bookstore.dto.orderitem.OrderItemDto;
import mate.academy.bookstore.mapper.OrderItemMapper;
import mate.academy.bookstore.mapper.OrderMapper;
import mate.academy.bookstore.model.Order;
import mate.academy.bookstore.model.OrderItem;
import mate.academy.bookstore.model.ShoppingCart;
import mate.academy.bookstore.model.Status;
import mate.academy.bookstore.repository.order.OrderRepository;
import mate.academy.bookstore.repository.orderitem.OrderItemRepository;
import mate.academy.bookstore.service.OrderService;
import mate.academy.bookstore.service.ShoppingCartService;
import mate.academy.bookstore.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartService shoppingCartService;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    @Override
    public OrderDto save(Long userId, CreateOrderRequestDto requestDto) {
        ShoppingCart cartFromDb = shoppingCartService.getCartByUserId(userId);
        if (cartFromDb.getCartItems().size() == 0) {
            throw new IllegalStateException("Cannot proceed with an empty shopping cart.");
        }

        Order order = new Order();
        order.setUser(userService.getById(userId));
        order.setStatus(Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(requestDto.shippingAddress());
        order.setTotal(BigDecimal.ZERO);

        final Order orderFromDb = orderRepository.save(order);

        Set<OrderItem> orderItems = cartFromDb.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = orderItemMapper.toOrderItemModel(cartItem);
                    orderItem.setOrder(orderFromDb);
                    return orderItem;
                })
                .peek(orderItemRepository::save)
                .collect(Collectors.toSet());

        order = orderFromDb;
        order.setOrderItems(orderItems);

        order.setTotal(order.getOrderItems().stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        shoppingCartService.clearShoppingCart(cartFromDb.getId());
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderDto> findAll(Long userId) {
        return orderRepository.findAllByUserId(userId).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public List<OrderItemDto> findAllOrderItemsByOrderId(Long userId, Long orderId) {
        Order orderFromDb = getOrderFromDbByUserIdAndOrderId(userId, orderId);
        return orderFromDb.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemDto findOrderItemById(Long userId, Long itemId, Long orderId) {
        Order orderFromDb = getOrderFromDbByUserIdAndOrderId(userId, orderId);
        return orderFromDb.getOrderItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .map(orderItemMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find item by itemId=" + itemId));
    }

    @Override
    public OrderDto update(UpdateOrderStatusRequestDto requestDto, Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(requestDto.status());
            return orderMapper.toDto(order);
        } else {
            throw new EntityNotFoundException("Can't find order by orderId=" + orderId);
        }
    }

    private Order getOrderFromDbByUserIdAndOrderId(Long userId, Long orderId) {
        return orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find order by userId=" + userId + " and orderId=" + orderId));
    }
}
