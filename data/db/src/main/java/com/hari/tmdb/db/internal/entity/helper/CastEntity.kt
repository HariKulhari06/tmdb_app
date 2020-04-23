package com.hari.tmdb.db.internal.entity.helper

import com.hari.tmdb.model.TmdbEntity
import com.uwetrottmann.tmdb2.entities.BaseMember


abstract class MemberEntity : TmdbEntity {
    var creditId: String? = null
    var name: String? = null
    var profilePath: String? = null
}

class CastMember : BaseMember() {
    var character: String? = null
    var order: Int? = null
    var castId: Int? = null
}


class CrewMember : BaseMember() {
    var department: String? = null
    var job: String? = null
}