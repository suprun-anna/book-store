package mate.academy.bookstore.repository.book;

import java.util.Arrays;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    private static final String SPECIFICATION_KEY = "title";

    @Override
    public String getKey() {
        return SPECIFICATION_KEY;
    }

    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) ->
                root.get(SPECIFICATION_KEY).in(Arrays.stream(params).toArray());
    }
}
