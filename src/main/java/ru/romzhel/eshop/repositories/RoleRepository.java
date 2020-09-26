package ru.romzhel.eshop.repositories;


import org.springframework.data.repository.CrudRepository;
import ru.romzhel.eshop.entities.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findOneByName(String theRoleName);
}
