package mate.academy.bookstore.service;

import mate.academy.bookstore.dto.cartitem.AddItemToCartRequestDto;
import mate.academy.bookstore.dto.cartitem.UpdateCartItemRequestDto;
import mate.academy.bookstore.dto.shoppingcart.ShoppingCartDto;
import mate.academy.bookstore.model.ShoppingCart;
import mate.academy.bookstore.model.User;

public interface ShoppingCartService {
    ShoppingCart createShoppingCart(User user);

    ShoppingCartDto addBooksToCartByUserId(Long userId, AddItemToCartRequestDto requestDto);

    ShoppingCartDto getCartDtoByUserId(Long id);

    ShoppingCart getCartByUserId(Long id);

    ShoppingCartDto updateCartItemById(
            Long userId, Long cartItemId, UpdateCartItemRequestDto requestDto);

    ShoppingCartDto delete(Long userId, Long cartItemId);

    void clearShoppingCart(Long cartId);
}
