package dev.applaudostudios.applaudofinalproject.repository;

import dev.applaudostudios.applaudofinalproject.dto.responses.ICheckoutResponseDto;
import dev.applaudostudios.applaudofinalproject.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    @Query("SELECT c.product.id as productId, p.productName as productName, " +
            "p.unitPrice as unitPrice, (p.unitPrice * c.quantity) as subTotal, c.quantity as quantity " +
            "FROM OrderDetail c JOIN c.product p WHERE c.order.id = ?1")
    List<ICheckoutResponseDto> getOrderDetailInformation(Long id);

    @Query("SELECT SUM(c.price * c.quantity) FROM OrderDetail c WHERE c.order.id = ?1")
    Double getTotal(Long id);
}
