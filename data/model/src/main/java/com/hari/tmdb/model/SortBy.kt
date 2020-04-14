package com.hari.tmdb.model

enum class SortBy(private val value: String) {
    POPULARITY_ASC("popularity.asc"),
    POPULARITY_DESC("popularity.desc"),
    RELEASE_DATE_ASC("release_date.asc"),
    RELEASE_DATE_DESC("release_date.desc"),
    REVENUE_ASC("revenue.asc"),
    REVENUE_DESC("revenue.desc"),
    PRIMARY_RELEASE_DATE_ASC("primary_release_date.asc"),
    PRIMARY_RELEASE_DATE_DESC("primary_release_date.desc"),
    ORIGINAL_TITLE_ASC("original_title.asc"),
    ORIGINAL_TITLE_DESC("original_title.desc"),
    VOTE_AVERAGE_ASC("vote_average.asc"),
    VOTE_AVERAGE_DESC("vote_average.desc"),
    VOTE_COUNT_ASC("vote_count.asc"),
    VOTE_COUNT_DESC("vote_count.desc");

    override fun toString(): String {
        return value
    }

}


fun getSortingOptionAsSet() = setOf(
    SortBy.POPULARITY_ASC,
    SortBy.POPULARITY_DESC,
    SortBy.RELEASE_DATE_ASC,
    SortBy.RELEASE_DATE_DESC,
    SortBy.REVENUE_ASC,
    SortBy.REVENUE_DESC,
    SortBy.PRIMARY_RELEASE_DATE_ASC,
    SortBy.PRIMARY_RELEASE_DATE_DESC,
    SortBy.ORIGINAL_TITLE_ASC,
    SortBy.ORIGINAL_TITLE_DESC,
    SortBy.VOTE_AVERAGE_ASC,
    SortBy.VOTE_AVERAGE_DESC,
    SortBy.VOTE_COUNT_ASC,
    SortBy.VOTE_COUNT_DESC
)