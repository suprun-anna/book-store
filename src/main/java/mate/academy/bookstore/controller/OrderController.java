package mate.academy.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.order.CreateOrderRequestDto;
import mate.academy.bookstore.dto.order.OrderDto;
import mate.academy.bookstore.dto.order.UpdateOrderStatusRequestDto;
import mate.academy.bookstore.dto.orderitem.OrderItemDto;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order manager", description = "Endpoints for managing orders")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Place order",
            description = "Place order")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    OrderDto placeOrder(Authentication authentication,
                        @RequestBody @Valid CreateOrderRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return orderService.save(user.getId(), requestDto);
    }

    @Operation(summary = "Get all orders",
            description = "Retrieve user's order history")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    List<OrderDto> getAllOrders(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.findAll(user.getId());
    }

    @Operation(summary = "Get all items for a specific order",
            description = "Retrieve all order items for a specific order")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{orderId}/items")
    List<OrderItemDto> getAllOrderItems(Authentication authentication,
                                        @PathVariable Long orderId) {
        User user = (User) authentication.getPrincipal();
        return orderService.findAllOrderItemsByOrderId(user.getId(), orderId);
    }

    @Operation(summary = "Get a specific order item from a specific order",
            description = "Retrieve a specific order item within an order")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("{orderId}/items/{itemId}")
    OrderItemDto getOrderItem(Authentication authentication,
                              @PathVariable Long itemId,
                              @PathVariable Long orderId) {
        User user = (User) authentication.getPrincipal();
        return orderService.findOrderItemById(user.getId(), itemId, orderId);
    }

    @Operation(summary = "Update order status",
            description = "Update the status of an order")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    OrderDto updateOrderStatus(@RequestBody @Valid UpdateOrderStatusRequestDto requestDto,
                               @PathVariable Long id) {
        return orderService.update(requestDto, id);
    }
}
