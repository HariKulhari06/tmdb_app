package com.hari.tmdb.repository.internal.mapper

import com.hari.tmdb.db.internal.entity.GenreEntity
import com.hari.tmdb.model.Genre
import com.hari.tmdb.model.Movie
import com.uwetrottmann.tmdb2.entities.BaseMovie
import com.uwetrottmann.tmdb2.entities.MovieResultsPage

fun GenreEntity.toGenre(): Genre = Genre(id = id, name = name)

fun MovieResultsPage.toMovies(): List<Movie> {
    return results?.map { it.toMovie() } ?: emptyList()
}

fun BaseMovie.toMovie(): Movie = Movie(
    id = id,
    adult = adult,
    backdropPath = backdrop_path,
    genreIds = genre_ids,
    originalLanguage = original_language,
    originalTitle = original_title,
    overview = overview,
    popularity = popularity,
    posterPath = poster_path,
    releaseDate = release_date.toLocaleString(),
    title = title,
    video = false,
    voteAverage = vote_average,
    voteCount = vote_count
)
