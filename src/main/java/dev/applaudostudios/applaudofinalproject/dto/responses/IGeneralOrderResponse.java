package dev.applaudostudios.applaudofinalproject.dto.responses;

import java.util.Date;

public interface IGeneralOrderResponse {
    Long getOrderId();
    Date getDate();
    Boolean getStatus();
    String getTrackingNum();
    double getTotal();
}
