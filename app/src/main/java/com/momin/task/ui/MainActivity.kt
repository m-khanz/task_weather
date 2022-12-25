package com.momin.task.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.momin.task.R
import com.momin.task.common.NetworkUtils
import com.momin.task.databinding.ActivityMainBinding
import com.momin.task.ui.adapter.CitiesPagingAdapter
import com.momin.task.utils.Resource.ERROR
import com.momin.task.utils.Resource.LOADING
import com.momin.task.utils.Resource.SUCCESS
import com.momin.task.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var citiesPagingAdapter: CitiesPagingAdapter

    override fun onStart() {
        super.onStart()
        handleNetworkChanges()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialize()
    }

    private fun initialize() {
        setUpRecyclerview()
        searchViewListener()
        observeState()
    }

    private fun setUpRecyclerview() {
        citiesPagingAdapter = CitiesPagingAdapter()
        binding.rvCitiesList.layoutManager = LinearLayoutManager(this)
        binding.rvCitiesList.adapter = citiesPagingAdapter
        binding.rvCitiesList.itemAnimator = null
    }

    private fun observeState() {
        viewModel.state.observe(this) { dataHandler ->
            when (dataHandler) {
                is SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                }
                is ERROR -> {
                    binding.progressBar.visibility = View.GONE
                }
                is LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE

                }
            }

        }
    }

    private fun searchViewListener() {
        binding.searchViewCity.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.searchCityInDb(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { viewModel.searchCityInDb(it) }
                return false
            }
        })
    }

    private fun observeCitiesFromLocalDb() {
        viewModel.offlineCitiesList.observe(this) {
            lifecycleScope.launch {
                citiesPagingAdapter.submitData(it)
            }
        }
    }

    private fun handleNetworkChanges() {
        NetworkUtils.getNetworkResponse(applicationContext).observe(this) { isConnected ->
            if (!isConnected) {
                binding.textViewNetworkStatus.text = getString(R.string.no_internet_connection)
                binding.viewNetworkStatus.apply {
                    visibility = View.VISIBLE
                }
            } else {
                if (citiesPagingAdapter.itemCount == 0) getData()//this will get data when user connects with the net...
                binding.textViewNetworkStatus.text = getString(R.string.connected)
                binding.viewNetworkStatus.apply {
                    animate()
                        .alpha(1f)
                        .setStartDelay(1000)
                        .setDuration(1000)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                visibility = View.GONE
                            }
                        })
                }
            }
        }
    }

    private fun getData() {
        viewModel.getCitiesListFromRemoteSource()
        observeCitiesFromLocalDb()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val item = menu?.findItem(R.id.mode_switch)
        if ((resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES)
            item?.title = getString(R.string.light_mode)
        else
            item?.title = getString(R.string.dark_mode)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mode_switch -> {
                when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        item.title = getString(R.string.dark_mode)
                    }
                    Configuration.UI_MODE_NIGHT_NO -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        item.title = getString(R.string.light_mode)
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}