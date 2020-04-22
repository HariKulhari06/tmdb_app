package com.hari.tmdb.db.internal.mapper

import com.hari.tmdb.db.internal.entity.MovieEntityImp
import com.hari.tmdb.db.internal.entity.PopularMovieEntity
import com.hari.tmdb.model.mapper.Mapper
import com.uwetrottmann.tmdb2.entities.MovieResultsPage

val popularMovieEntryMapper =
    object : Mapper<MovieResultsPage, List<Pair<MovieEntityImp, PopularMovieEntity>>> {
        override suspend fun map(from: MovieResultsPage): List<Pair<MovieEntityImp, PopularMovieEntity>> {
            return from.results.map { baseMovie ->
                val movieEntity = baseMovieToMovieEntityMapper.map(baseMovie)
                val popularMovieEntity = PopularMovieEntity(
                    movieId = movieEntity.id,
                    pageOrder = 1,
                    page = from.page

                )
                movieEntity to popularMovieEntity
            }
        }
    }
