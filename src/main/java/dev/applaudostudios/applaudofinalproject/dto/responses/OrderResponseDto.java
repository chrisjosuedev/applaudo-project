package dev.applaudostudios.applaudofinalproject.dto.responses;

import dev.applaudostudios.applaudofinalproject.models.Address;
import dev.applaudostudios.applaudofinalproject.models.payments.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private UserResDto userInfo;
    private Payment payment;
    private Address shippingAddress;
    private List<ICheckoutResponseDto> checkoutDetails;
    private String status;
    private Double total;
}