package ru.romzhel.eshop.services;


import org.springframework.security.core.userdetails.UserDetailsService;
import ru.romzhel.eshop.entities.SystemUser;
import ru.romzhel.eshop.entities.User;

public interface UserService extends UserDetailsService {
    User findByUserName(String userName);

    void save(SystemUser systemUser);
}
