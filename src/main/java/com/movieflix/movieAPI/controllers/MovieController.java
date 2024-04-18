package com.movieflix.movieAPI.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movieflix.movieAPI.dto.*;
import com.movieflix.movieAPI.exceptions.EmptyFileException;
import com.movieflix.movieAPI.service.*;
//import com.movieflix.movieAPI.service.MovieServiceImpl;
import com.movieflix.movieAPI.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.plaf.multi.MultiPanelUI;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MovieController {
    private final MovieService movieService;
    MovieController(MovieService movieService){
        this.movieService=movieService;
    }

    @PostMapping("/add-movie")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<MovieDto> addMovieHandler(@RequestPart("posterImage") MultipartFile file, @RequestPart String movieDto) throws IOException, EmptyFileException {
        if(file.isEmpty()) {
            throw new EmptyFileException("File is empty! Please upload another one");
        }
        MovieDto dto=convertToMovieDto(movieDto);
        return new ResponseEntity<>(movieService.addMovie(dto,file), HttpStatus.CREATED);
    }

    private MovieDto convertToMovieDto(String movieDtoObj) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(movieDtoObj,MovieDto.class);
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable Integer movieId){
        return ResponseEntity.ok(movieService.getMovie(movieId));
    }

    @GetMapping("/allmovie")
    public ResponseEntity<List<MovieDto>> getAllMovie(){
        return ResponseEntity.ok(movieService.getAllMovies());
    }


    @PutMapping("/update/{movieId}")
    public ResponseEntity<MovieDto> updateMovieHandler(@PathVariable Integer movieId,@RequestPart MultipartFile file ,@RequestPart String movieDtoOb) throws IOException {
        if(file.isEmpty())file=null;
        MovieDto movieDto=convertToMovieDto(movieDtoOb);
        return ResponseEntity.ok(movieService.updateMovie(movieId,movieDto,file));
    }

    @DeleteMapping("/delete/{movieId}")
    public ResponseEntity<String> deleteMovieHandler(@PathVariable("movieId") Integer id) throws IOException {
        String s=movieService.deleteMovie(id);
        return ResponseEntity.ok(s);
    }

    // get all movies with pagination
    @GetMapping("/allMoviesPage")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<MoviePageResponse> getMovieswithPagination(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize
    ){
        return ResponseEntity.ok(movieService.getAllMoviesWithPagination(pageNumber,pageSize));
    }

    // get all movies with pagination and sort
    @GetMapping("/allMoviesPageSort")
    public ResponseEntity<MoviePageResponse> getMoviesWithPaginationAndSorting(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(defaultValue = AppConstants.SORT_BY,required = false) String sortBy,
            @RequestParam(defaultValue = AppConstants.SORT_DIR, required = false) String sortDir
    ){
        return ResponseEntity.ok(movieService.getAllMoviesWithPaginationAndSorting(pageNumber,pageSize,sortBy,sortDir));
    }
}
