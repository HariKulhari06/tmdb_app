rootProject.name = "TMDB"

include(
    ":app",
    ":corecomponent:androidcomponets",
    ":data:api",
    ":ext:log",
    ":data:db",
    ":data:local",
    ":data:model",
    ":data:repository",
    ":image-loading",
    ":feature:movies",
    ":feature:system",
    ":feature:authentication",
    ":feature:account",
    ":corecomponent:compose",
    ":feature:search",
    ":feature:settings"
)
