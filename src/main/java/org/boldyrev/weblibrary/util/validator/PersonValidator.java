package org.boldyrev.weblibrary.util.validator;

import java.util.Optional;
import org.boldyrev.weblibrary.models.Person;
import org.boldyrev.weblibrary.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PersonValidator(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(Person.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        Optional<Person> foundByNamePerson = peopleRepository.findByNameIgnoreCase(
            person.getName());
        if (foundByNamePerson.isPresent() && foundByNamePerson.get().getId() != person.getId()) {
            errors.rejectValue("name", "existed_name_error", "This person already exists");
        }
    }
}
