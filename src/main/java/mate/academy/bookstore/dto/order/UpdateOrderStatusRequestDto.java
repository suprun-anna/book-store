package mate.academy.bookstore.dto.order;

import jakarta.validation.constraints.NotNull;
import mate.academy.bookstore.model.Status;

public record UpdateOrderStatusRequestDto(
        @NotNull
        Status status
) {
}
