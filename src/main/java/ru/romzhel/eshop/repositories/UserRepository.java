package ru.romzhel.eshop.repositories;


import org.springframework.data.repository.CrudRepository;
import ru.romzhel.eshop.entities.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    User findOneByUserName(String userName);

    List<User> getAllBy();
}
