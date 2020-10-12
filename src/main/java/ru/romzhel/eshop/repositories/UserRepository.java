package ru.romzhel.eshop.repositories;


import org.springframework.data.repository.CrudRepository;
import ru.romzhel.eshop.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findOneByUserName(String userName);
}
