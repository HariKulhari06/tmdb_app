buildscript {
    repositories {
        jcenter()
    }
}

plugins {
    `kotlin-dsl`
}


repositories {
    jcenter()
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}