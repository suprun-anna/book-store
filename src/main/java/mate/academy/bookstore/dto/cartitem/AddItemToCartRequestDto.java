package mate.academy.bookstore.dto.cartitem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

public record AddItemToCartRequestDto(
        Long bookId,
        @Min(1)
        @Positive
        int quantity
) {
}
