package ru.romzhel.eshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.romzhel.eshop.entities.Role;
import ru.romzhel.eshop.repositories.RoleRepository;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    Role getRoleByRoleName(String roleName) {
        return roleRepository.findOneByName(roleName);
    }
}
