package mate.academy.bookstore.mapper;

import mate.academy.bookstore.config.MapperConfig;
import mate.academy.bookstore.dto.cartitem.AddItemToCartRequestDto;
import mate.academy.bookstore.dto.cartitem.CartItemDto;
import mate.academy.bookstore.dto.cartitem.UpdateCartItemRequestDto;
import mate.academy.bookstore.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartItemMapper {
    @Mappings({
            @Mapping(target = "bookId", source = "book.id"),
            @Mapping(target = "bookTitle", source = "book.title")
    })
    CartItemDto toDto(CartItem cartItem);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookFromId")
    CartItem toModel(AddItemToCartRequestDto requestDto);

    @Mapping(target = "book", ignore = true)
    void updateFromDto(UpdateCartItemRequestDto requestDto, @MappingTarget CartItem itemFromDb);
}

