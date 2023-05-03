package org.boldyrev.weblibrary.services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.boldyrev.weblibrary.models.Book;
import org.boldyrev.weblibrary.models.Person;
import org.boldyrev.weblibrary.repositories.BooksRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BooksService {

    private final BooksRepository booksRepository;

    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Book> findAllByCurrentOwner(Person person) {
        return booksRepository.findAllByCurrentOwner(person);
    }

    @Transactional(readOnly = true)
    public List<Book> findAllByCurrentOwner(Person person, Sort sort) {
        return booksRepository.findAllByCurrentOwner(person, sort).stream()
            .peek(book -> setExpired(book)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Book> findAllSorted(Sort sort) {
        return booksRepository.findAll(sort);
    }

    @Transactional(readOnly = true)
    public List<Book> findAll(int page, int size, String sort) {
        int countRows = (int) booksRepository.count();
        if (size <= 0) {
            size = countRows;
        }
        if (page <= 0) {
            page = 1;
        }
        List<Book> books = booksRepository.findAll(
            PageRequest.of(page - 1, size, Sort.by(sort))).getContent();
        return books;
    }

    @Transactional(readOnly = true)
    public List<Book> searchBookByTitle(String title) {
        return booksRepository.findBooksByTitleContainingIgnoreCase(title);
    }


    @Transactional(readOnly = true)
    public Optional<Book> findById(int id) {
        Optional<Book> book = booksRepository.findById(id);
        setExpired(book.get());
        return book;
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        updatedBook.setId(id);
        booksRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    @Transactional
    public void releaseBook(int id) {
        Book book = booksRepository.findById(id).get();
        book.setCurrentOwner(null);
        book.setAssignationDate(null);
    }

    private void setExpired(Book book) {
        if (book.getAssignationDate() != null) {
            LocalDate assignation = LocalDate.ofInstant(book.getAssignationDate().toInstant(),
                ZoneId.systemDefault());
            LocalDate now = LocalDate.now();

            if (assignation.until(now, ChronoUnit.DAYS) > 10) {
                book.setExpired(true);
            }
        }
    }

}
