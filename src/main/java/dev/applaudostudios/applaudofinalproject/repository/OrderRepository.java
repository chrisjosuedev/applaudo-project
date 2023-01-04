package dev.applaudostudios.applaudofinalproject.repository;

import dev.applaudostudios.applaudofinalproject.dto.responses.IGeneralOrderResponse;
import dev.applaudostudios.applaudofinalproject.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT c.id as orderId, c.date as date, " +
            "c.trackNum as trackingNum, c.status as status," +
            "SUM(p.price * p.quantity) as total FROM Order c JOIN c.orderDetail p " +
            "WHERE c.user.sid = ?1 " +
            "GROUP BY c.id")
    List<IGeneralOrderResponse> findAllOrdersByUserSid(String sid);
    Optional<Order> findByIdAndUserSid(Long id, String sid);
    Optional<Order> findByTrackNumAndUserSid(String trackNum, String sid);
}
