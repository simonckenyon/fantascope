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

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.koala.fantascope.model.Movie
import ie.koala.fantascope.ui.MoviesAdapter
import ie.koala.fantascope.viewmodel.MovieViewModel
import kotlinx.android.synthetic.main.activity_main.*

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MovieViewModel
    private val adapter = MoviesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        setupScrollListener()

        // get the view model
        viewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(this))
            .get(MovieViewModel::class.java)

        initAdapter()
        viewModel.getMovies("")
        adapter.submitList(null)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_refresh -> consume {
            // for now scroll to top of list
            list.smoothScrollToPosition(0)
        }
        else -> super.onOptionsItemSelected(item)
    }

    private inline fun consume(f: () -> Unit): Boolean {
        f()
        return true
    }

    private fun initAdapter() {
        log.debug("initAdapter:")
        list.adapter = adapter
        viewModel.movies.observe(this, Observer<List<Movie>> {
            log.debug("initAdapter: movies=$it")
            list.setEmptyView(emptyView)
            if (it.isNotEmpty()) {
                adapter.submitList(it)
            } else {
                log.warn("initAdapter: movies list is null or empty")
            }
        })
        viewModel.networkErrors.observe(this, Observer<String> {
            Toast.makeText(this, "Network error $it", Toast.LENGTH_LONG).show()
        })
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
    }
}

