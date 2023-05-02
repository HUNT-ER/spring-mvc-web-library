package org.boldyrev.weblibrary.controllers;

import java.util.List;
import javax.validation.Valid;
import org.boldyrev.weblibrary.models.Book;
import org.boldyrev.weblibrary.models.Person;
import org.boldyrev.weblibrary.services.BooksService;
import org.boldyrev.weblibrary.services.PeopleService;
import org.boldyrev.weblibrary.util.validator.PersonValidator;
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

@Component
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;
    private final BooksService booksService;
    private final PersonValidator personValidator;

    @Autowired
    public PeopleController(PeopleService peopleService, BooksService booksService,
        PersonValidator personValidator) {
        this.peopleService = peopleService;
        this.booksService = booksService;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String showAll(Model model) {
        model.addAttribute("people", peopleService.findAll());

        return "people/index";
    }

    @GetMapping("/new")
    public String addNewPerson(@ModelAttribute("person") Person person) {
        return "/people/new";
    }

    @PostMapping()
    public String saveNewPerson(@ModelAttribute("person") @Valid Person person,
        BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            return "/people/new";
        }
        peopleService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}")
    public String showPerson(@PathVariable("id") int id, Model model) {
        Person person = peopleService.findById(id).get();
        model.addAttribute("person", person);
        model.addAttribute("books", booksService.findAllByCurrentOwner(person, Sort.by("title")));
        return "/people/show";
    }

    @GetMapping("/{id}/edit")
    public String editPerson(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", peopleService.findById(id).get());
        return "/people/edit";
    }

    @PatchMapping("/{id}")
    public String saveEditedPerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
        @PathVariable("id") int id) {
        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            return "/people/edit";
        }

        peopleService.update(id, person);
        return "redirect:/people/{id}";
    }

    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable("id") int id) {
        peopleService.delete(id);

        return "redirect:/people";
    }

}
