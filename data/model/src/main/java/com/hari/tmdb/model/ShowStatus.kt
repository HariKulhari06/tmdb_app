package com.hari.tmdb.model

enum class ShowStatus(val storageKey: String) {
    ENDED("ended"),
    RETURNING("returning"),
    CANCELED("canceled"),
    IN_PRODUCTION("inproduction");
}
