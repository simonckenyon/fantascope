package ie.koala.fantascope.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ie.koala.fantascope.model.Movie

@JsonClass(generateAdapter = true)
data class MoviesResponse(
    @field:Json(name = "page") var page: Long = 0,
    @field:Json(name = "results") var movies: List<Movie>? = listOf(),
    @field:Json(name = "total_pages") var totalPages: Long = 0,
    @field:Json(name = "total_results") var totalResults: Long = 0
)
