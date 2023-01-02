package dev.applaudostudios.applaudofinalproject.service;
import dev.applaudostudios.applaudofinalproject.models.CartItemSession;
import dev.applaudostudios.applaudofinalproject.models.Order;;import java.util.List;

public interface IOrderDetailsService {
    void createOrders(Order order, List<CartItemSession> myCart);
}
