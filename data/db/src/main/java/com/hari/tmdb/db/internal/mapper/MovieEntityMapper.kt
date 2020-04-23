@file:Suppress(
    "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS",
    "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS"
)

package com.hari.tmdb.db.internal.mapper

import com.hari.tmdb.db.internal.entity.MovieEntityImp
import com.hari.tmdb.db.internal.entity.RelatedMovieEntity
import com.hari.tmdb.model.Movie
import com.hari.tmdb.model.mapper.Mapper
import com.uwetrottmann.tmdb2.entities.BaseCompany
import com.uwetrottmann.tmdb2.entities.BaseMovie
import com.uwetrottmann.tmdb2.entities.MovieResultsPage
import com.uwetrottmann.tmdb2.entities.Movie as TmdbMovie


val movieResultToMovieEntityImp = object : Mapper<MovieResultsPage, List<MovieEntityImp>> {
    override suspend fun map(from: MovieResultsPage): List<MovieEntityImp> {
        return from.results?.map {
            baseMovieToMovieEntityMapper.map(it)
        } ?: emptyList()
    }
}

val baseMovieToMovieEntityMapper = object : Mapper<BaseMovie, MovieEntityImp> {
    override suspend fun map(from: BaseMovie): MovieEntityImp = MovieEntityImp(
        id = from.id,
        adult = from.adult,
        backdropPath = from.backdrop_path,
        genreIds = from.genres?.map { it.id }?.joinToString(","),
        originalLanguage = from.original_language,
        originalTitle = from.original_title,
        overview = from.overview,
        popularity = from.popularity,
        posterPath = from.poster_path,
        releaseDate = from.release_date.toString(),
        title = from.title,
        video = false,
        voteAverage = from.vote_average,
        voteCount = from.vote_count
    )
}


val movieToMovieEntityMapper = object : Mapper<TmdbMovie, MovieEntityImp> {
    override suspend fun map(from: TmdbMovie): MovieEntityImp = MovieEntityImp(
        id = from.id,
        adult = from.adult,
        backdropPath = from.backdrop_path,
        budget = from.budget,
        genreIds = from.genres.map { it.id }.joinToString(","),
        homepage = from.homepage,
        imdbId = from.imdb_id,
        originalLanguage = from.original_language,
        originalTitle = from.original_title,
        overview = from.overview,
        popularity = from.popularity,
        posterPath = from.poster_path,
        releaseDate = from.release_date.toString(),
        revenue = from.revenue,
        runtime = from.runtime,
        status = from.status.value,
        tagLine = from.tagline,
        title = from.title,
        video = false,
        voteAverage = from.vote_average,
        voteCount = from.vote_count,
        productionCompanyPath = productionCompanyPath(from.production_companies)
    )

    private fun productionCompanyPath(productionCompanies: List<BaseCompany>): String? {
        if (productionCompanies.isNullOrEmpty()) return null
        var path: String? = null
        productionCompanies.forEach {
            if (it.logo_path.isNullOrEmpty().not() && path == null) {
                path = it.logo_path
            }
        }
        return path
    }
}

val movieToRelatedMovieEntity =
    object : Mapper<TmdbMovie, List<Pair<MovieEntityImp, RelatedMovieEntity>>> {
        override suspend fun map(from: TmdbMovie): List<Pair<MovieEntityImp, RelatedMovieEntity>> {

            return from.similar.results.map { baseMovie ->
                val movieEntityImp = baseMovieToMovieEntityMapper.map(baseMovie)
                val relatedMovieEntity = RelatedMovieEntity(
                    movieId = baseMovie.id,
                    otherMovieId = from.id,
                    orderIndex = 1
                )
                movieEntityImp to relatedMovieEntity
            }
        }

    }

val movieEntityToMovie = object : Mapper<MovieEntityImp, Movie> {
    override suspend fun map(from: MovieEntityImp): Movie = Movie(
        id = from.id,
        adult = from.adult ?: false,
        backdropPath = from.backdropPath,
        budget = from.budget ?: 0,
        genres = emptyList(),
        homepage = from.homepage,
        imdbId = from.imdbId,
        originalLanguage = from.originalLanguage ?: "",
        originalTitle = from.originalTitle ?: "",
        overview = from.overview ?: "",
        popularity = from.popularity ?: 0.0,
        posterPath = from.posterPath ?: "",
        releaseDate = from.releaseDate ?: "",
        revenue = from.revenue ?: 0,
        runtime = from.runtime ?: 0,
        status = from.status ?: "",
        tagLine = from.tagLine,
        title = from.title ?: "",
        video = from.video ?: false,
        voteAverage = from.voteAverage ?: 0.0,
        voteCount = from.voteCount ?: 0,
        productionCompanyPath = from.productionCompanyPath
    )

}