package ie.koala.fantascope.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ie.koala.fantascope.R
import ie.koala.fantascope.ui.Wiki
import ie.koala.fantascope.web.ViewClient
import kotlinx.android.synthetic.main.activity_copyright.*
import org.slf4j.LoggerFactory

class WikiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_copyright)

        log.debug("onCreate:")

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val w: Wiki = intent.getParcelableExtra(ARG_WIKI)
        log.debug("onCreate: w=$w")
        updateContent(w)
    }

    override fun onBackPressed() {
        log.debug("onBackPressed:")
        super.onBackPressed()
    }

    private fun updateContent(w: Wiki) {
        log.debug("updateContent: w=$w")

        val url = "assets://wiki/" + w.url + ".wiki"

        try {
            toolbar_layout.title = w.title

            wiki.webViewClient = ViewClient(null)
            wiki.loadUrl(url)
        } catch (e: Exception) {
            log.debug("onCreateView: exception ", e)
            wiki.loadData("Unable to show wiki page", "text/html", "")
        }
    }

    companion object {
        const val ARG_WIKI = "ARG_WIKI"
        val log = LoggerFactory.getLogger(WikiActivity::class.java)
    }
}
