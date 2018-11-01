package ie.koala.fantascope.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity
data class Movie(
    @field:Json(name = "adult") var adult: Boolean = false,
    @field:Json(name = "backdrop_path") var backdropPath: String = "",
    @field:Json(name = "genre_ids") var genreIds: List<Long> = listOf(),
    @field:Json(name = "id") @PrimaryKey() var id: Long = 0,
    @field:Json(name = "original_language") var originalLanguage: String = "",
    @field:Json(name = "original_title") var originalTitle: String = "",
    @field:Json(name = "overview") var overview: String = "",
    @field:Json(name = "popularity") var popularity: Double = 0.0,
    @field:Json(name = "poster_path") var posterPath: String = "",
    @field:Json(name = "release_date") var releaseDate: String = "",
    @field:Json(name = "title") var title: String = "",
    @field:Json(name = "video") var video: Boolean = true,
    @field:Json(name = "vote_average") var voteAverage: Double = 0.0,
    @field:Json(name = "vote_count") var voteCount: Long = 0
)
