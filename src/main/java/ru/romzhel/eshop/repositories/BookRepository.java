package ru.romzhel.eshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.romzhel.eshop.entities.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Book getFirstById(Long id);
}
