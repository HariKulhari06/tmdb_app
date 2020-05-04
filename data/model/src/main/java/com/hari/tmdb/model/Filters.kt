package com.hari.tmdb.model

import kotlinx.coroutines.flow.flowOf

data class Filters(
    val genres: Set<Genre> = mutableSetOf(),
    var languages: Set<Language> = mutableSetOf(),
    val certifications: Set<Certification> = mutableSetOf(),
    var includeAdult: Set<String> = mutableSetOf(),
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

fun adultsFiltersAsFlow() = flowOf(listOf("Yes", "No"))