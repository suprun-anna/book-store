package mate.academy.bookstore.mapper;

import mate.academy.bookstore.config.MapperConfig;
import mate.academy.bookstore.dto.shoppingcart.ShoppingCartDto;
import mate.academy.bookstore.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mappings({
            @Mapping(source = "user.id", target = "userId"),
            @Mapping(source = "cartItems", target = "cartItems")
    })
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    @Mappings({
            @Mapping(source = "userId", target = "user.id"),
            @Mapping(source = "cartItems", target = "cartItems")
    })
    ShoppingCart toModel(ShoppingCartDto shoppingCartResponseDto);
}
