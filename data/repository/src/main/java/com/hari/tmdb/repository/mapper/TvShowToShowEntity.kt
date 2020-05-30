package com.hari.tmdb.repository.mapper

import com.hari.tmdb.db.internal.entity.ShowEntity
import com.hari.tmdb.model.mapper.Mapper
import com.uwetrottmann.tmdb2.entities.TvShow
import javax.inject.Inject

class TvShowToShowEntity @Inject constructor() : Mapper<TvShow, ShowEntity> {
    override suspend fun map(from: TvShow): ShowEntity {
        return ShowEntity(
            tmdbId = from.id,
            backdrop_path = from.backdrop_path,
            posterPath = from.poster_path,
            voteCount = from.vote_count,
            voteAverage = from.vote_average,
            summary = from.overview,
            popularity = from.popularity,
            originalTitle = from.original_name,
            originalLanguage = from.original_language,
            numberOfSeasons = from.number_of_seasons,
            numberOfEpisodes = from.number_of_episodes,
            network = from.networks.firstOrNull()?.name,
            homepage = from.homepage,
            firstAired = from.first_air_date.toString(),
            country = from.origin_country.firstOrNull(),
            title = from.name,
            _genres = from.genres.joinToString { genre -> genre.name },
            languages = from.languages.joinToString(),
            createdBy = from.created_by.firstOrNull()?.name,
            networkLogoPath = from.networks.firstOrNull()?.logo_path
        )
    }
}