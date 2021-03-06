package ru.romzhel.eshop.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.romzhel.eshop.entities.Category;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {
    Category getByTitle(String title);
}
