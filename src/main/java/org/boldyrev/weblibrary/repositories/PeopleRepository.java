package org.boldyrev.weblibrary.repositories;

import java.util.List;
import java.util.Optional;
import org.boldyrev.weblibrary.models.Book;
import org.boldyrev.weblibrary.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByNameIgnoreCase(String name);
}
