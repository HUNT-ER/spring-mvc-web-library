package org.boldyrev.weblibrary.controllers;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import org.boldyrev.weblibrary.models.Book;
import org.boldyrev.weblibrary.models.Person;
import org.boldyrev.weblibrary.services.BooksService;
import org.boldyrev.weblibrary.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@RequestMapping("/books")
public class BooksController {

    private final BooksService booksService;
    private final PeopleService peopleService;

    @Autowired
    public BooksController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping
    public String showBooks(@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(name = "books_per_page", required = false, defaultValue = "0") Integer booksPerPage,
        @RequestParam(name = "sort_by", required = false, defaultValue = "title") String sortBy, Model model) {

        List<Book> books = booksService.findAll(page, booksPerPage, sortBy);
        model.addAttribute("books", books);

        return "/books/index";
    }

    @GetMapping("/new")
    public String addNewBook(@ModelAttribute("book") Book book) {
        return "/books/new";
    }

    @PostMapping
    public String saveNewBook(@ModelAttribute("book") @Valid Book book,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/books/new";
        }

        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String showBook(@PathVariable("id") int id, @ModelAttribute("owner") Person owner,
        Model model) {
        Book book = booksService.findById(id).get();
        model.addAttribute("book", book);
        if (book.getCurrentOwner() == null) {
            model.addAttribute("people", peopleService.findAllSorted(Sort.by("name")));
        } else {
            model.addAttribute("currentOwner", book.getCurrentOwner());
        }
        return "/books/show";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", booksService.findById(id).get());
        return "/books/edit";
    }

    @PatchMapping("/{id}")
    public String saveEditedBook(@ModelAttribute("book") @Valid Book book,
        BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "/books/edit";
        }

        booksService.update(id, book);
        return "redirect:/books/{id}";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        booksService.delete(id);

        return "redirect:/books";
    }

    @PatchMapping("/{id}/assign")
    public String assignBook(@ModelAttribute("owner") Person newOwner, @PathVariable("id") int id) {
        Book book = booksService.findById(id).get();
        book.setCurrentOwner(newOwner);
        book.setAssignationDate(new Date());
        booksService.update(id, book);

        return "redirect:/books/{id}";
    }

    @PatchMapping("/{id}/release")
    public String releaseBook(@PathVariable("id") int id) {
        booksService.releaseBook(id);
        return "redirect:/books/{id}";
    }

    @GetMapping("/search")
    public String searchBook(@RequestParam(name = "book_title", required = false) String title, Model model) {
        if (title == null) {
            return "/books/search";
        }
        model.addAttribute("books", booksService.searchBookByTitle(title));
        return "/books/search";
    }
}
