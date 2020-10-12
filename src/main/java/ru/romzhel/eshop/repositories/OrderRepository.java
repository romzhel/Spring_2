package ru.romzhel.eshop.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.romzhel.eshop.entities.Order;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
}
