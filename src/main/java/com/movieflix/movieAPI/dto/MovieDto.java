package com.movieflix.movieAPI.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

//@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MovieDto {
    private Integer movieId;

    @NotBlank(message = "Please provide movie's title!") // application level validation
    private String title;

    @NotBlank(message =  "Please provide movie's director")
    private String director;

    @NotBlank(message = "Please provide movie's studio")
    private String studio;

    private Set<String> movieCast;

    private Integer releaseYear;


    @NotBlank(message = "Please provide movie poster")
    private String poster;

    @NotBlank(message = "Please provide poster's url")
    private String posterUrl;
}
