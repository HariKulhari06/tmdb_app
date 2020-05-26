package com.hari.tmdb.repository.mapper

import com.hari.tmdb.db.internal.entity.PopularShowEntity
import com.hari.tmdb.db.internal.entity.ShowEntity
import com.hari.tmdb.model.mapper.Mapper
import com.uwetrottmann.tmdb2.entities.TvShowResultsPage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TvShowResultsPageToShows @Inject constructor() :
    Mapper<TvShowResultsPage, List<Pair<ShowEntity, PopularShowEntity>>> {
    override suspend fun map(from: TvShowResultsPage): List<Pair<ShowEntity, PopularShowEntity>> {
        return from.results.map { baseTvShow ->
            val showEntity = ShowEntity(
                tmdbId = baseTvShow.id,
                title = baseTvShow.name,
                summary = baseTvShow.overview,
                posterPath = baseTvShow.poster_path,
                backdrop_path = baseTvShow.backdrop_path
            )

            val popularShowEntity = PopularShowEntity(
                movieId = baseTvShow.id,
                page = from.page,
                pageOrder = 1
            )

            showEntity to popularShowEntity
        }
    }
}