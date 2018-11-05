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
package ie.koala.fantascope.db

import androidx.lifecycle.LiveData
import ie.koala.fantascope.model.Movie
import java.util.concurrent.Executor

/**
 * Class that handles the DAO local movieLiveData source. This ensures that methods are triggered on the
 * correct executor.
 */
class MovieLocalCache(
    private val movieDao: MovieDao,
    private val ioExecutor: Executor
) {
    /**
     * Insert a movie into the database, on a background thread.
     */
    fun insert(movies: List<Movie>, insertFinished: () -> Unit) {
        ioExecutor.execute {
            movieDao.insert(movies)
            insertFinished()
        }
    }

    /**
     * Request a LiveData<MoviesResponse> from the Dao
     */
    fun topRatedMovies(): LiveData<List<Movie>> {
        val movieLiveData = movieDao.topRatedMovies()
        return movieLiveData
    }

    /**
     * Request a LiveData<MoviesResponse> from the Dao
     */
    fun searchMovies(queryString: String): LiveData<List<Movie>> {
        val movieLiveData = movieDao.searchMovies(queryString)
        return movieLiveData
    }
}