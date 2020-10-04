package ru.romzhel.eshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.romzhel.eshop.entities.Role;
import ru.romzhel.eshop.repositories.RoleRepository;

@Service
public class RoleService {
    private RoleRepository roleRepository;

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getRoleById(Long id) {
        return roleRepository.findRoleById(id);
    }

    public Iterable<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
