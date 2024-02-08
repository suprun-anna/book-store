package mate.academy.bookstore;

import java.math.BigDecimal;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book = new Book();
            book.setTitle("The Hound of the Baskervilles");
            book.setAuthor("\tArthur Conan Doyle");
            book.setIsbn("978-3-16-148410-0");
            book.setPrice(new BigDecimal("29.99"));
            book.setDescription("Detective fiction, Gothic fiction.");
            book.setCoverImage("cover_image.jpg");
            System.out.println(bookService.save(book));
            System.out.println(bookService.findAll());
        };
    }
}
