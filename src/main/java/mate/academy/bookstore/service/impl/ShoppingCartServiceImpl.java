package mate.academy.bookstore.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.HashSet;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.cartitem.AddItemToCartRequestDto;
import mate.academy.bookstore.dto.cartitem.UpdateCartItemRequestDto;
import mate.academy.bookstore.dto.shoppingcart.ShoppingCartDto;
import mate.academy.bookstore.mapper.CartItemMapper;
import mate.academy.bookstore.mapper.ShoppingCartMapper;
import mate.academy.bookstore.model.CartItem;
import mate.academy.bookstore.model.ShoppingCart;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.repository.cartitem.CartItemRepository;
import mate.academy.bookstore.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.bookstore.repository.user.UserRepository;
import mate.academy.bookstore.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final CartItemMapper cartItemMapper;
    private final ShoppingCartMapper cartMapper;

    @Override
    public ShoppingCart createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCart.setCartItems(new HashSet<>());
        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCartDto addBooksToCartByUserId(Long userId, AddItemToCartRequestDto requestDto) {
        ShoppingCart cartFromDB = getCartFromDB(userId);
        CartItem requestedItem = cartItemMapper.toModel(requestDto);
        requestedItem.setShoppingCart(cartFromDB);
        cartItemRepository.save(requestedItem);
        return cartMapper.toDto(getCartFromDB(userId));
    }

    @Override
    public ShoppingCartDto getCartByUserId(Long id) {
        return cartMapper.toDto(getCartFromDB(id));
    }

    @Override
    public ShoppingCartDto updateCartItemById(
            Long userId, Long cartItemId, UpdateCartItemRequestDto requestDto) {
        CartItem itemFromDb = getItemFromDb(cartItemId);
        cartItemMapper.updateFromDto(requestDto, itemFromDb);
        cartItemRepository.save(itemFromDb);
        return cartMapper.toDto(getCartFromDB(userId));
    }

    @Override
    public ShoppingCartDto delete(Long userId, Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
        return cartMapper.toDto(getCartFromDB(userId));
    }

    private CartItem getItemFromDb(Long id) {
        return cartItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find item by id=" + id));
    }

    private ShoppingCart getCartFromDB(Long userId) {
        return shoppingCartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    ShoppingCart shoppingCart = new ShoppingCart();
                    User userFromDb = userRepository.findById(userId)
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "Can't find user by id=" + userId));
                    shoppingCart.setUser(userFromDb);
                    return shoppingCartRepository.save(shoppingCart);
                });
    }
}
