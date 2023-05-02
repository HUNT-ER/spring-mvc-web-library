package org.boldyrev.weblibrary.repositories;

import java.util.List;
import org.boldyrev.weblibrary.models.Book;
import org.boldyrev.weblibrary.models.Person;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    List<Book> findAllByCurrentOwner(Person person);
    List<Book> findAllByCurrentOwner(Person person, Sort sort);

}
