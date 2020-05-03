package com.hari.tmdb.model.repository

import com.hari.tmdb.model.Movie
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun search(query: Flow<String>): Flow<List<Movie>>
}