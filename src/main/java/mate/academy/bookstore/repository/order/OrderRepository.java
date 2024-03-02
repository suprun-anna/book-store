package mate.academy.bookstore.repository.order;

import java.util.List;
import java.util.Optional;
import mate.academy.bookstore.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o "
            + "LEFT JOIN FETCH o.user u "
            + "LEFT JOIN FETCH o.orderItems i "
            + "WHERE u.id = :userId")
    List<Order> findAllByUserId(Long userId);

    @Query("SELECT o FROM Order o "
            + "LEFT JOIN FETCH o.user u "
            + "LEFT JOIN FETCH o.orderItems i "
            + "WHERE u.id = :userId "
            + "AND o.id = :orderId")
    Optional<Order> findByIdAndUserId(Long orderId, Long userId);
}
