package ru.romzhel.eshop.services;


import org.springframework.security.core.userdetails.UserDetailsService;
import ru.romzhel.eshop.entities.SystemUser;
import ru.romzhel.eshop.entities.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    User findByUserName(String userName);

    boolean save(SystemUser systemUser);

    boolean save(User user);

    List<User> getAll();
}
