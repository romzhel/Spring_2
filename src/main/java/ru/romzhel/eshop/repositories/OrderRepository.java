package ru.romzhel.eshop.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.romzhel.eshop.entities.Order;
import ru.romzhel.eshop.entities.User;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
    List<Order> getAllByUser(User user);
}
