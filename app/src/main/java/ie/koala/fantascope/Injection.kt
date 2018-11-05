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
package ie.koala.fantascope

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import ie.koala.fantascope.viewmodel.ViewModelFactory
import ie.koala.fantascope.data.MovieRepository
import ie.koala.fantascope.api.MovieService
import ie.koala.fantascope.db.MovieDatabase
import ie.koala.fantascope.db.MovieLocalCache
import java.util.concurrent.Executors

object Injection {

    /**
     * Creates an instance of [MovieLocalCache] based on the database DAO.
     */
    private fun provideCache(context: Context): MovieLocalCache {
        val database = MovieDatabase.getInstance(context)
        return MovieLocalCache(database.movieDao(), Executors.newSingleThreadExecutor())
    }

    /**
     * Creates an instance of [MovieRepository] based on the [MovieService] and a
     * [MovieLocalCache]
     */
    private fun provideMovieRepository(context: Context): MovieRepository {
        return MovieRepository(MovieService.create(context), provideCache(context))
    }

    /**
     * Provides the [ViewModelProvider.Factory] that is then used to getMovies a reference to
     * [ViewModel] objects.
     */
    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(provideMovieRepository(context))
    }

}