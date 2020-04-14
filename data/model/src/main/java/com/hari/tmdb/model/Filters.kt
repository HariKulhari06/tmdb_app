package com.hari.tmdb.model

data class Filters(
    val genres: Set<Genre> = mutableSetOf(),
    val languages: Set<Genre> = mutableSetOf(),
    val certifications: Set<Genre> = mutableSetOf(),
    var includeAdult: Set<Boolean> = mutableSetOf(),
    var sortBy: Set<SortBy> = mutableSetOf()
) {


    fun isFiltered(): Boolean {
        return genres.isNotEmpty() ||
                languages.isNotEmpty() ||
                certifications.isNotEmpty() ||
                includeAdult.isNotEmpty() ||
                sortBy.isNotEmpty()
    }

    private fun Movie.isNotFilterableServiceSession() = true
}