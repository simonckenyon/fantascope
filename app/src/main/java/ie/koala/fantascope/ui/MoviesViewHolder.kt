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

package ie.koala.fantascope.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ie.koala.fantascope.R
import ie.koala.fantascope.model.Movie
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import com.bumptech.glide.Glide
import java.util.*


/**
 * View Holder for a [Movie] RecyclerView list item.
 */
class MoviesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val title = view.findViewById(R.id.title) as TextView
    private val poster = view.findViewById(R.id.poster) as ImageView
    private val voteAverage = view.findViewById(R.id.voteAverage) as TextView
    private val overview = view.findViewById(R.id.overview) as TextView

    private var movie: Movie? = null

    init {
        view.setOnClickListener {
            movie?.id?.let {
                // TODO!
                // display details about this movie
            }
        }
    }

    fun bind(movie: Movie?, position: Int) {
        if (movie == null) {
            log.error("bind: movie is empty?")
        } else {
            showMovie(itemView, movie)
        }
    }

    private fun showMovie(view: View, movie: Movie) {
        this.movie = movie

        title.text = movie.title
        voteAverage.text = String.format(Locale.getDefault(), "%2.2f", movie.voteAverage)
        overview.text = movie.overview

        val posterPath = movie.posterPath
        if (posterPath != null) {
            val url = getPosterUrl(posterPath)
            Glide.with(view)
                .load(url)
                .into(poster)
        }
    }

    private fun getPosterUrl(posterPath: String): String {
        return POSTER_PREFIX + posterPath
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(MoviesViewHolder::class.java)

        private const val POSTER_PREFIX = "https://image.tmdb.org/t/p/w500/"

        fun create(parent: ViewGroup): MoviesViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_movie, parent, false)
            return MoviesViewHolder(view)
        }
    }
}