package com.hari.tmdb.model

enum class Certification(name: String?) {
    U("U"),
    UA("UA"),
    A("A"),
    OTHER(null);


    companion object {
        fun gePage(name: String): Certification {
            return values()
                .firstOrNull { it.name == name } ?: OTHER
        }
    }
}

val setOfCertifications = setOf(
    Certification.U,
    Certification.UA,
    Certification.A
)
