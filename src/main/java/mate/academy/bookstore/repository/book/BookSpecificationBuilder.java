package mate.academy.bookstore.repository.book;

import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.book.BookSearchParameters;
import mate.academy.bookstore.model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private static final String AUTHOR_KEY = "author";
    private static final String ISBN_KEY = "isbn";
    private static final String PRICE_KEY = "price";
    private static final String TITLE_KEY = "title";
    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters searchParams) {
        Specification<Book> bookSpecification = Specification.where(null);
        bookSpecification = addAuthorsSpecification(searchParams, bookSpecification);
        bookSpecification = addTitlesSpecification(searchParams, bookSpecification);
        bookSpecification = addIsbnsSpecification(searchParams, bookSpecification);
        bookSpecification = addPriceSpecification(searchParams, bookSpecification);
        return bookSpecification;
    }

    private Specification<Book> addSpecification(BookSearchParameters searchParams,
                                                 Specification<Book> bookSpecification,
                                                 String key,
                                                 Function<BookSearchParameters, String[]> accessor
    ) {
        if (accessor.apply(searchParams) != null && accessor.apply(searchParams).length > 0) {
            return bookSpecification.and(
                    bookSpecificationProviderManager.getSpecificationProvider(key)
                            .getSpecification(accessor.apply(searchParams)));
        }
        return bookSpecification;
    }

    private Specification<Book> addAuthorsSpecification(BookSearchParameters searchParams,
                                                        Specification<Book> bookSpecification) {
        return addSpecification(searchParams, bookSpecification,
                AUTHOR_KEY, BookSearchParameters::authors);
    }

    private Specification<Book> addTitlesSpecification(BookSearchParameters searchParams,
                                                       Specification<Book> bookSpecification) {
        return addSpecification(searchParams, bookSpecification,
                TITLE_KEY, BookSearchParameters::titles);
    }

    private Specification<Book> addIsbnsSpecification(BookSearchParameters searchParams,
                                                      Specification<Book> bookSpecification) {
        return addSpecification(searchParams, bookSpecification,
                ISBN_KEY, BookSearchParameters::isbns);
    }

    private Specification<Book> addPriceSpecification(BookSearchParameters searchParams,
                                                      Specification<Book> bookSpecification) {

        if (searchParams.priceFrom() != null) {
            return bookSpecification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get(PRICE_KEY),
                            searchParams.priceFrom()));
        } else if (searchParams.priceTo() != null) {
            return bookSpecification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get(PRICE_KEY),
                            searchParams.priceTo()));
        }
        return bookSpecification;
    }
}
