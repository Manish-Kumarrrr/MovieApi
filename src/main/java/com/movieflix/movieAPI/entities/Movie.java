package com.movieflix.movieAPI.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer movieId;

    @Column(nullable = false,length =200) // database level validation
    @NotBlank(message = "Please provide movie's title!") // application level validation
    private String title;

    @Column(nullable = false)
    @NotBlank(message =  "Please provide movie's director")
    private String director;

    @Column(nullable = false)
    @NotBlank(message = "Please provide movie's studio")
    private String studio;

    @ElementCollection
    @CollectionTable(name = "movie_cast")
    private Set<String> movieCast;

    @Column(nullable = false)
    private Integer releaseYear;

    @Column(nullable = false)
    @NotBlank(message = "Please provide movie poster")
    private String poster;
}
