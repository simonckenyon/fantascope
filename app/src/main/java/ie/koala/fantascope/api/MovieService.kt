package ie.koala.fantascope.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import ie.koala.fantascope.api.MovieService.Companion.log
import ie.koala.fantascope.model.Movie
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

fun getMovie(
    service: MovieService,
    key: String,
    language: String,
    page: Int,
    onSuccess: (movies: List<Movie>) -> Unit,
    onError: (error: String) -> Unit) {

    log.debug("getMovie: here we go!")

    service.getMoviesFromTheMovieDatabase(key, language, page).enqueue(
            object : Callback<MoviesResponse> {
                override fun onFailure(call: Call<MoviesResponse>?, t: Throwable) {
                    val message = t.message ?: "unknown error"
                    log.error("getMovie.onFailure: $message")
                    onError(message)
                }

                override fun onResponse(call: Call<MoviesResponse>, response: Response<MoviesResponse>) {
                    log.debug("getMovie.onResponse:")
                    if (response.isSuccessful) {
                        val movies: List<Movie>? = response.body()?.movies
                        if (movies != null) {
                            onSuccess(movies)
                        } else {
                            onError("movies is null")
                        }
                    } else {
                        onError(response.errorBody()?.string() ?: "Unknown error")
                    }
                }
            }
    )
}

interface MovieService {
    @GET("top_rated")
    fun getMoviesFromTheMovieDatabase(@Query("api_key") key: String,
                                      @Query("language") language: String,
                                      @Query("page") page: Int): Call<MoviesResponse>

    companion object {
        val log: Logger = LoggerFactory.getLogger(MovieService::class.java)

        private const val BASE_URL = "https://api.themoviedb.org/3/movie/"

        private const val CACHE_SIZE = (5 * 1024 * 1024).toLong()   // 5 Megabytes
        private const val MAX_AGE = 5                               // The maximum amount of time (5 seconds) that a resource will be considered fresh.
        private const val MAX_STALE = 60 * 60 * 24 * 7              // The app is willing to accept a response that has exceeded its expiration time by (1 week).

        fun hasNetwork(context: Context): Boolean? {
            var isConnected: Boolean? = false // Initial Value
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
            if (activeNetwork != null && activeNetwork.isConnected)
                isConnected = true
            return isConnected
        }

        fun create(context: Context): MovieService {
            val logger = HttpLoggingInterceptor()
            logger.level = Level.BODY   // was BASIC

            val myCache = Cache(context.cacheDir, CACHE_SIZE)

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .cache(myCache)
                .addInterceptor { chain ->
                    var request = chain.request()
                    request = if (hasNetwork(context)!!)
                        request.newBuilder().header("Cache-Control", "public, max-age=$MAX_AGE").build()
                    else
                        request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=$MAX_STALE").build()
                    chain.proceed(request)
                }
                .build()

            val moshi = Moshi
                    .Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .build()
                    .create(MovieService::class.java)
        }
    }
}