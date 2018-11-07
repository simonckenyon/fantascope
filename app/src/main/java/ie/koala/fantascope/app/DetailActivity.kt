package ie.koala.fantascope.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import ie.koala.fantascope.R
import ie.koala.fantascope.api.MovieService
import ie.koala.fantascope.model.Movie
import ie.koala.fantascope.web.AppWikiModel
import ie.koala.fantascope.web.ViewClient
import kotlinx.android.synthetic.main.activity_detail.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // FIXME!
        // the title transition doesn't really play well with the CollapsingToolbarLayout
        for (i in 0 until toolbar.childCount) {
            val child = toolbar.getChildAt(i)
            if (child is TextView) {
                child.transitionName = "movieTitle"
                break
            }
        }

        val movie: Movie = intent.getParcelableExtra(Movie.ARG_MOVIE)
        log.debug("onCreate: movie=$movie")
        updateContent(movie)

        fab.setOnClickListener {
            val url = MovieService.WEBSITE_BASE_URL +  movie.id
            val message = """Here is a link to \"${movie.title}\" on themoviedb.org:
                |
                |$url
                |
                |Enjoy!""".trimMargin()
            val subject = "Movie: ${movie.title}"
            log.debug("getDefaultIntent: subject=\"$subject\" message=\"$message\"")

            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, message)
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
            intent.type = "text/plain"
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateContent(movie: Movie) {
        try {
            toolbar_layout.title = "${movie.title} (${movie.releaseDate})"


            wiki.webViewClient = ViewClient(null)
            //val webSettings = wiki.settings
            //webSettings.javaScriptEnabled = true
            val wikiModel = AppWikiModel()
            val htmlStr = wikiModel.render(movie.overview)
            wiki.loadData(htmlStr, "text/html; charset=utf-8", "UTF-8")
        } catch (e: Exception) {
            log.debug("onCreateView: exception ", e)
            wiki.loadData("Unable to show wiki page", "text/html", "")
        }
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(DetailActivity::class.java)

        fun newIntent(context: Context, movie: Movie): Intent {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(Movie.ARG_MOVIE, movie)
            return intent
        }
    }
}
