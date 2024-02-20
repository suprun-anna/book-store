package mate.academy.bookstore.dto.book;

import java.math.BigDecimal;

public record BookSearchParameters(String[] titles, String[] authors, String[] isbns,
                                   BigDecimal priceFrom, BigDecimal priceTo) {

}
