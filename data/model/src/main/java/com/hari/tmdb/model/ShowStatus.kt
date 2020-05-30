package com.hari.tmdb.model

enum class ShowStatus(val storageKey: String) {
    ENDED("Ended"),
    RETURNING("Returning Series"),
    CANCELED("Canceled"),
    IN_PRODUCTION("Inproduction"),
    UNKNOWN("");

    companion object {
        fun status(storageKey: String): ShowStatus {
            return values()
                .firstOrNull { it.storageKey == storageKey } ?: UNKNOWN
        }
    }
}
