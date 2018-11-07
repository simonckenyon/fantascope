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
package ie.koala.fantascope.app

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.koala.fantascope.Injection
import ie.koala.fantascope.R
import ie.koala.fantascope.app.WikiActivity.Companion.ARG_WIKI
import ie.koala.fantascope.model.Movie
import ie.koala.fantascope.preferences.PreferenceHelper.defaultPrefs
import ie.koala.fantascope.preferences.PreferenceKeys.NAV_MODE_NORMAL
import ie.koala.fantascope.preferences.PreferencesActivity
import ie.koala.fantascope.ui.MoviesAdapter
import ie.koala.fantascope.ui.Wiki
import ie.koala.fantascope.viewmodel.MovieViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MovieViewModel
    private val adapter = MoviesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener { menuItem ->
            // set item as selected to persist highlight
            menuItem.isChecked = true
            // close drawer when item is tapped
            drawer_layout.closeDrawer(GravityCompat.START)

            when (menuItem.itemId) {
                R.id.nav_settings -> {
                    startActivity<PreferencesActivity>()
                    true
                }
                R.id.nav_copyright -> {
                    val wiki = Wiki("Copyright Statement", "copyright")
                    startActivity<WikiActivity>(ARG_WIKI to wiki)
                    true
                }
                R.id.nav_about -> {
                    val wiki = Wiki("About this app", "about")
                    startActivity<WikiActivity>(ARG_WIKI to wiki)
                    true
                }
                else -> false
            }
        }

        setupScrollListener()

        // getMovies the view model
        viewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(this))
            .get(MovieViewModel::class.java)

        initAdapter()

        val query: String? = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: ""
        if (query != null) {
            viewModel.getMovies(query)
        } else {
            viewModel.getMovies("")
        }
        adapter.submitList(null)
    }

    override fun onBackPressed() {
        val prefs = defaultPrefs(this)
        val navModeNormal: Boolean = prefs.getBoolean(NAV_MODE_NORMAL, false)
        when (navModeNormal) {
            true ->
                if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                    drawer_layout.closeDrawer(GravityCompat.START)
                } else {
                    super.onBackPressed()
                }
            else ->
                if (!drawer_layout.isDrawerOpen(GravityCompat.START)) {
                    drawer_layout.openDrawer(GravityCompat.START)
                } else {
                    super.onBackPressed()
                }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            isSubmitButtonEnabled = true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_refresh -> consume {
            // for now scroll to top of list
            list.smoothScrollToPosition(0)
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH_QUERY, viewModel.lastQueryValue())
    }

    private inline fun consume(f: () -> Unit): Boolean {
        f()
        return true
    }

    private fun initAdapter() {
        list.adapter = adapter
        viewModel.movies.observe(this, Observer<List<Movie>> {movieList ->
            log.warn("observe: movieList=$movieList")
            if (movieList.isNotEmpty()) {
                adapter.submitList(movieList)
            } else {
                log.warn("observe: movieList is empty")
                list.setEmptyView(emptyView)
            }
        })
        viewModel.networkErrors.observe(this, Observer<String> {
            Toast.makeText(this, "Network error $it", Toast.LENGTH_LONG).show()
        })
    }

    override fun onNewIntent(intent: Intent) {
        setIntent(intent)
        // Verify the action and getMovies the query
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                viewModel.getMovies("%$query%")
                adapter.submitList(null)
            }
        }
    }

    private fun setupScrollListener() {
        val layoutManager = list.layoutManager as LinearLayoutManager
        list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                viewModel.listScrolled(visibleItemCount, lastVisibleItem, totalItemCount)
            }
        })
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(MainActivity::class.java)

        private const val LAST_SEARCH_QUERY: String = "last_search_query"
    }
}

