package org.boldyrev.weblibrary.models;

import java.time.LocalDate;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "book")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Book {

    @Id
    @Column(name = "book_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @Column(name = "title")
    @NotEmpty(message = "Title should not be empty")
    @Size(message = "Title should be less than 100 chars", max = 100)
    private String title;

    @NonNull
    @Column(name = "author")
    @NotEmpty(message = "Author should not be empty")
    @Size(message = "Author name should be less than 100 chars", max = 100)
    private String author;

    @NonNull
    @Column(name = "year")
    @Min(message = "Year should be greater then 1900", value = 1900)
    private int year;

    @Column(name = "assignation_date")
    @Temporal(TemporalType.DATE)
    private Date assignationDate;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    private Person currentOwner;

}
