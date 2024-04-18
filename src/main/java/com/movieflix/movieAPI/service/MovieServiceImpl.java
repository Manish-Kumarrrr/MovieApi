package com.movieflix.movieAPI.service;

import com.movieflix.movieAPI.dto.*;
import com.movieflix.movieAPI.entities.Movie;
import com.movieflix.movieAPI.exceptions.FileExistsException;
import com.movieflix.movieAPI.exceptions.MovieNotFoundException;
import com.movieflix.movieAPI.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService{

    private final MovieRepository movieRepository;
    private final FileService fileService;

    public  MovieServiceImpl(MovieRepository movieRepository, FileService fileService){
        this.movieRepository=movieRepository;
        this.fileService=fileService;
    }

    @Value("${project.poster}")
    String path;

    @Value("${base.url}")
    String baseUrl;

    @Override
    public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException {
        //1. upload the file
        if(Files.exists(Paths.get(path+File.separator+file.getOriginalFilename()))){
            throw new FileExistsException("File already exists! Please submit poster with another name");
        }
        String uploadFilename = fileService.uploadFile(path,file);

        //2. set the value of field 'poster' as filename
        movieDto.setPoster(uploadFilename);

        //3. map dto to Movie object
        Movie tempMovie=new Movie(
                null , // (if you are giving a primary which already not exist then it will insert data with auto incremented pk (strategy chosen by you)    //movieDto.getMovieId(),
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );

        //4. save the movie object -> saved Movie object
        Movie savedMovie = movieRepository.save(tempMovie);

        //5. generate the posterUrl
        String posterUrl= baseUrl+"/file/" + uploadFilename;

        //6. map Movie object to Dto object and return it
        MovieDto response=new MovieDto(
                savedMovie.getMovieId(),
                savedMovie.getTitle(),
                savedMovie.getDirector(),
                savedMovie.getStudio(),
                savedMovie.getMovieCast(),
                savedMovie.getReleaseYear(),
                savedMovie.getPoster(),
                posterUrl
        );
        //7. return reponse
        return response;

    }

    @Override
    public MovieDto getMovie(Integer movieId) {
        // 1.check the data in dp and if exists , fetch the data of given ID
        Movie movie = movieRepository.findById(movieId).orElseThrow(()-> new MovieNotFoundException("Movie Not Found"));
        // 2. generate posterUrl
        String posterUrl=baseUrl+"/file/" + movie.getPoster();

        // 3. map to MovieDto object and return it
        MovieDto response=new MovieDto(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getPoster(),
                posterUrl
        );
        // 4. return the response
        return response;
    }

    @Override
    public List<MovieDto> getAllMovies(){
        // 1. fetch all data from DB
        List<Movie> movies=movieRepository.findAll();

        // 2. iterate through the list,generate posterUrl for each movie obj and map to MovieDto obj
        List<MovieDto> movieDtos=new ArrayList<>();
        for(Movie movie:movies){
            String posterUrl=baseUrl+"/file/" + movie.getPoster();
            MovieDto response=new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl
            );
            movieDtos.add(response);
        }
        // return list
        return movieDtos;
    }

    @Override
    public MovieDto updateMovie(Integer movieId, MovieDto movieDto, MultipartFile file) throws IOException {
        //1. check movie object exists with given movieId
        Movie movie=movieRepository.findById(movieId).orElseThrow(()->new MovieNotFoundException("Movie not found"));

        //2.1 if file is null, do nothing with fileservice
        //2.2 if file is not null, then delete existing file associcated with the record

        String fileName = movie.getPoster();
        if(file!=null){
            String posterPath=path+ fileName;
            Files.deleteIfExists(Paths.get(posterPath));
            fileName=fileService.uploadFile(path,file);
        }

        //3. set movieDto's poster value, according to step2
        movieDto.setPoster(fileName);


        //4. map it to movie object
        Movie upadatedMovie=new Movie(
                movieDto.getMovieId(),
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );


        //5. save the movie object -> return saved movie object
        movieRepository.save(upadatedMovie);

        //6. generate posterUrl for it
        String posterUrl=baseUrl+"/file/" + movieDto.getPoster();


        //7.map to MovieDto and return it
        MovieDto response=new MovieDto(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getPoster(),
                posterUrl
        );

        return response;

    }

    @Override
    public String deleteMovie(Integer movieId) throws IOException {
        //1. check movie object exists with given movieId
        Movie movie=movieRepository.findById(movieId).orElseThrow(()->new RuntimeException("Movie not found"));
        //2.  delete existing file associcated with the record

        String fileName = movie.getPoster();
            String posterPath=path + movie.getPoster();
            Files.deleteIfExists(Paths.get(posterPath));

        // 3. delete object from db
        movieRepository.deleteById(movieId);

        return "Movie deleted with id="+movieId;



    }

    @Override
    public MoviePageResponse getAllMoviesWithPagination(Integer pageNumber, Integer pageSize) {
        Pageable pageable= PageRequest.of(pageNumber,pageSize);
        Page<Movie> moviesPages=this.movieRepository.findAll(pageable);
        List<Movie> movies=moviesPages.getContent();

        List<MovieDto> movieDtos=new ArrayList<>();
        for(Movie movie:movies){
            String posterUrl=baseUrl+"/file/" + movie.getPoster();
            MovieDto response=new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl
            );
            movieDtos.add(response);
        }
        return new MoviePageResponse(movieDtos,pageNumber,pageSize,moviesPages.getTotalElements(),moviesPages.getTotalPages(),moviesPages.isLast());
    }

    @Override
    public MoviePageResponse getAllMoviesWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort=null;
        sort =(sortDir.equalsIgnoreCase("asc"))?sort.by(sortBy).ascending():
                                                                sort.by(sortBy).descending();
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Movie> moviesPages=this.movieRepository.findAll(pageable);
        List<Movie> movies=moviesPages.getContent();

        List<MovieDto> movieDtos=new ArrayList<>();
        for(Movie movie:movies){
            String posterUrl=baseUrl+"/file/" + movie.getPoster();
            MovieDto response=new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl
            );
            movieDtos.add(response);
        }
        return new MoviePageResponse(movieDtos,pageNumber,pageSize,moviesPages.getTotalElements(),moviesPages.getTotalPages(),moviesPages.isLast());
    }


    }
