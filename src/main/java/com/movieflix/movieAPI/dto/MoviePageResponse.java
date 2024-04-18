package com.movieflix.movieAPI.dto;

import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.List;

public record MoviePageResponse(List<MovieDto> movieDtos,
                                Integer pageNumber,
                                Integer pageSize,
                                long totalElements,
                                long totalPages,
                                boolean lastPage) {
}
