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
package ie.koala.fantascope.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ie.koala.fantascope.data.MovieRepository
import ie.koala.fantascope.model.Movie
import ie.koala.fantascope.model.MovieResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * ViewModel for the [MainActivity] activity.
 * The ViewModel works with the [MovieRepository] to get the movieLiveData.
 */
class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    private val queryLiveData = MutableLiveData<String>()
    private val movieResult: LiveData<MovieResult> = Transformations.map(queryLiveData) { request: String -> repository.get(request) }

    val movies: LiveData<List<Movie>> = Transformations.switchMap(movieResult) { movieResult: MovieResult ->
        movieResult.movieLiveData
    }
    val networkErrors: LiveData<String> = Transformations.switchMap(movieResult) { movieResult: MovieResult ->
        movieResult.networkErrors
    }

    fun getMovies(request: String) {
        queryLiveData.postValue(request)
    }

    fun listScrolled(visibleItemCount: Int, lastVisibleItemPosition: Int, totalItemCount: Int) {
        if (visibleItemCount + lastVisibleItemPosition + VISIBLE >= totalItemCount) {
            val immutableQuery = lastQueryValue()
            if (immutableQuery != null) {
                repository.requestMore(immutableQuery)
            }
        }
    }

    fun lastQueryValue(): String? = queryLiveData.value

    override fun toString(): String {
        return "MovieViewModel(" +
                "repository=$repository," +
                "queryLiveData=$queryLiveData," +
                "movieResult=$movieResult," +
                "movies=$movies," +
                "networkErrors=$networkErrors" +
                ")"
    }


    companion object {
        val log: Logger = LoggerFactory.getLogger(MovieViewModel::class.java)

        private const val VISIBLE = 5
    }
}