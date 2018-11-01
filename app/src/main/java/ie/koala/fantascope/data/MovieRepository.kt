/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ie.koala.fantascope.data

import androidx.lifecycle.MutableLiveData
import ie.koala.fantascope.BuildConfig
import ie.koala.fantascope.api.MovieService
import ie.koala.fantascope.api.getMovie
import ie.koala.fantascope.db.MovieLocalCache
import ie.koala.fantascope.model.MovieResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

/**
 * Repository class that works with local and remote movieLiveData sources.
 */
class MovieRepository(
        private val service: MovieService,
        private val cache: MovieLocalCache
) {

    /*
     * TMDB chunks responses in pages (which according to https://www.themoviedb.org/talk/587bea71c3a36846c300ff73)
     * are always sent back in blocks of 20
     *
     * This variable keeps a record of the last page requested.
     */
    private var page = 1    // This variable keeps a record of the last page requested.


    // LiveData of network errors.
    private val networkErrors = MutableLiveData<String>()

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    fun get(request: String): MovieResult {
        requestAndSaveData(request)
        page = 1

        // Get movieLiveData from the local cache
        val data = cache.movies()
        return MovieResult(data, networkErrors)
    }

    fun requestMore(request: String) {
        requestAndSaveData(request)
    }

    private fun requestAndSaveData(request: String) {
        log.debug("requestAndSaveData:")
        if (isRequestInProgress) return

        isRequestInProgress = true
        getMovie(service, BuildConfig.ApiKeyV3, Locale.getDefault().toString(), page, { movies ->
            log.debug("requestAndSaveData: inserting in cache")
            cache.insert(movies) {
                page += 1
                isRequestInProgress = false
            }
        }, { error ->
            networkErrors.postValue(error)
            isRequestInProgress = false
        })
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(MovieRepository::class.java)

        /*
         * According to https://www.themoviedb.org/talk/587bea71c3a36846c300ff73
         * movies are always sent back in blocks of 20
         *
         * Currently unused!
         */
        private const val PAGE_SIZE = 20
    }
}