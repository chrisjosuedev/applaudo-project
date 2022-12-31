package dev.applaudostudios.applaudofinalproject.service;
import dev.applaudostudios.applaudofinalproject.models.Order;;

public interface IOrderDetailsService {
    void createOrders(Order order, String sid);
}
