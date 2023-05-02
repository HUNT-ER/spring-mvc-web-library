package org.boldyrev.weblibrary.models;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "person")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Person {

    @Id
    @Column(name = "person_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @Column(name = "name")
    @Size(message = "Name size should be less than 100 chars", max = 100)
    @Pattern(message = "Name should be like ''Name Surname''", regexp = "[A-Z][a-z]+\\s[A-Z][a-z]+")
    private String name;

    @NonNull
    @Column(name = "age")
    @Min(message = "Age should be greater than 5", value = 6)
    private int age;

    @OneToMany(mappedBy = "currentOwner")
    private List<Book> books;
}
