package org.boldyrev.weblibrary.services;

import java.util.List;
import java.util.Optional;
import org.boldyrev.weblibrary.models.Book;
import org.boldyrev.weblibrary.repositories.BooksRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BooksService {

    private final BooksRepository booksRepository;

    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    @Transactional(readOnly = true)
    public List<Book> finAll() {
        return booksRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Book> findById(int id) {
       return booksRepository.findById(id);
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

}
