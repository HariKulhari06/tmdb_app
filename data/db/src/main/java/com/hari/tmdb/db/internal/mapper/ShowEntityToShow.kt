package com.hari.tmdb.db.internal.mapper

import com.hari.tmdb.db.internal.entity.ShowEntity
import com.hari.tmdb.model.Show
import com.hari.tmdb.model.mapper.Mapper

val showEntityToShow = object : Mapper<ShowEntity, Show> {
    override suspend fun map(from: ShowEntity): Show {
        return Show(
            id = from.id,
            tmdbId = from.tmdbId!!,
            title = from.title,
            runtime = from.runtime,
            certification = from.certification,
            country = from.country,
            firstAired = from.firstAired,
            homepage = from.homepage,
            lastAired = from.lastAired,
            network = from.network,
            networkLogoPath = from.networkLogoPath,
            nextAir = from.nextAir,
            numberOfEpisodes = from.numberOfEpisodes,
            numberOfSeasons = from.numberOfSeasons,
            originalLanguage = from.originalLanguage,
            originalTitle = from.originalTitle,
            popularity = from.popularity,
            status = from.status,
            summary = from.summary,
            voteAverage = from.voteAverage,
            voteCount = from.voteCount,
            posterPath = from.posterPath,
            backdrop_path = from.backdrop_path,
            genre = from._genres,
            createdBy = from.createdBy
        )
    }

}