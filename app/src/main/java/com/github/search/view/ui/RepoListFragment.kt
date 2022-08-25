package com.github.search.view.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.search.GithubApp
import com.github.search.R
import com.github.search.databinding.FragmentRepoListBinding
import com.github.search.util.ConnectivityLiveData
import com.github.search.view.MainActivity
import com.github.search.view.adapter.RepoAdapter
import javax.inject.Inject


/**
 * Fragment class through which user interacts with the app and we show the search
 * response in this UI class as well.
 */
class RepoListFragment : Fragment() {

    private lateinit var binding: FragmentRepoListBinding
    private lateinit var act: MainActivity
    private lateinit var viewModel: RepoViewModel
    private lateinit var mAdapter: RepoAdapter
    private lateinit var layoutManager: LinearLayoutManager

    lateinit var query: String
    private var isNetworkAvailable: Boolean = false

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var connectivityLiveData: ConnectivityLiveData


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GithubApp.getComponent().inject(this)
        act = activity as MainActivity

        connectivityLiveData = ConnectivityLiveData(GithubApp.getInstance())

        viewModel = ViewModelProvider(act, viewModelFactory)[RepoViewModel::class.java]

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRepoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * fragment onViewCreated is called so now we can make the network call and
         * set up our UI as well.
         */
        initialiseObservers()
        initialiseUIElements()

    }

    private fun initialiseObservers() {

        viewModel.repoMediatorData.observe(viewLifecycleOwner, Observer {
            val responseCount = it.totalCount ?: 0
            if (responseCount > 0) {
                mAdapter.updateData(it.items)
                binding.rvRepo.smoothScrollToPosition(0);
                binding.rvRepo.visibility = View.VISIBLE
                binding.tvError.visibility = View.INVISIBLE

            } else {
                binding.rvRepo.visibility = View.GONE
                binding.tvError.visibility = View.VISIBLE
                binding.tvError.text = getString(R.string.message_empty_result)
            }

        })

        viewModel.repoLoadingStateLiveData.observe(viewLifecycleOwner, Observer {
            onRepoLoadingStateChanged(it)
        })

        connectivityLiveData.observe(viewLifecycleOwner, Observer { isAvailable ->
            isNetworkAvailable = isAvailable
            when (isAvailable) {
                true -> viewModel.onFragmentReady()


                else -> {}
            }
        })
    }


    private fun initialiseUIElements() {
        mAdapter = RepoAdapter()
        layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.rvRepo.layoutManager = layoutManager
        binding.rvRepo.adapter = mAdapter

        /**
         * searchView that listens to the user's queries.
         * it will make a new fetch call as soon as user submits new query
         */

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(q: String?): Boolean {
                if (isNetworkAvailable) {
                    binding.progress.visibility = View.VISIBLE
                    query = q!!
                    viewModel.onSearchQuery(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                binding.tvError.visibility = View.INVISIBLE
                return false
            }
        })
    }


    private fun onRepoLoadingStateChanged(state: RepoLoadingState) {
        when (state) {
            RepoLoadingState.LOADING -> {
                binding.progress.visibility = View.VISIBLE
            }
            RepoLoadingState.LOADED -> {
                connectivityLiveData.value?.let {
                    if (it) {
                        binding.rvRepo.visibility = View.VISIBLE
                        binding.tvError.visibility = View.INVISIBLE
                    } else {
                        binding.rvRepo.visibility = View.INVISIBLE
                        binding.tvError.visibility = View.VISIBLE
                        binding.tvError.text = getString(R.string.message_empty_result)
                    }
                }
                binding.progress.visibility = View.INVISIBLE
            }
            RepoLoadingState.ERROR -> {
                binding.rvRepo.visibility = View.GONE
                binding.tvError.visibility = View.VISIBLE
                binding.progress.visibility = View.INVISIBLE
            }

        }
    }


}