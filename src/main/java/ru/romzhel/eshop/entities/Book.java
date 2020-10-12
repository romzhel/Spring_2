package ru.romzhel.eshop.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "books")
public class Book {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String title;
    @Column(name = "short_description")
    private String shortDescription;
    @Column(name = "release_year")
    private int releaseYear;
}
