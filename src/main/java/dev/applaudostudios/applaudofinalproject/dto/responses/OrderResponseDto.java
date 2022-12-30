package dev.applaudostudios.applaudofinalproject.dto.responses;

import dev.applaudostudios.applaudofinalproject.models.Address;
import dev.applaudostudios.applaudofinalproject.models.OrderDetail;
import dev.applaudostudios.applaudofinalproject.models.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {
    private Long id;
    private Date date;
    private String trackingNum;
    private Payment payment;
    private Address address;
    private List<OrderDetail> orderDetail = new ArrayList<>();
    private boolean status;
}