package com.hari.tmdb.log

import android.util.Log.*
import timber.log.Timber

object Timber {
    fun assert(throwable: Throwable) {
        Timber.log(ASSERT, null, throwable, null)
    }

    fun error(throwable: Throwable) {
        Timber.log(ERROR, null, throwable, null)
    }

    fun info(throwable: Throwable) {
        Timber.log(INFO, null, throwable, null)
    }

    fun debug(throwable: Throwable) {
        Timber.log(DEBUG, null, throwable, null)
    }

    fun verbose(throwable: Throwable) {
        Timber.log(VERBOSE, null, throwable, null)
    }
}


