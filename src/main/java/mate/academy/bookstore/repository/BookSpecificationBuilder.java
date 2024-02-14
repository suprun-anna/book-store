package mate.academy.bookstore.repository;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.BookSearchParameters;
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

        if (searchParams.authors() != null && searchParams.authors().length > 0) {
            bookSpecification = bookSpecification.and(
                    bookSpecificationProviderManager.getSpecificationProvider(AUTHOR_KEY)
                            .getSpecification(searchParams.authors()));
        }

        if (searchParams.titles() != null && searchParams.titles().length > 0) {
            bookSpecification = bookSpecification.and(
                    bookSpecificationProviderManager.getSpecificationProvider(TITLE_KEY)
                            .getSpecification(searchParams.titles()));
        }

        if (searchParams.isbns() != null && searchParams.isbns().length > 0) {
            bookSpecification = bookSpecification.and(
                    bookSpecificationProviderManager.getSpecificationProvider(ISBN_KEY)
                            .getSpecification(searchParams.isbns()));
        }

        if (searchParams.priceFrom() != null && searchParams.priceTo() != null) {
            bookSpecification = bookSpecification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get(PRICE_KEY), searchParams.priceFrom()),
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get(PRICE_KEY), searchParams.priceTo())
                ));
        } else if (searchParams.priceFrom() != null) {
            bookSpecification = bookSpecification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get(PRICE_KEY),
                            searchParams.priceFrom()));
        } else if (searchParams.priceTo() != null) {
            bookSpecification = bookSpecification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get(PRICE_KEY),
                            searchParams.priceTo()));
        }
        return bookSpecification;
    }
}
