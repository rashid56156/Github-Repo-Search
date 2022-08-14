package com.github.search.view.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.search.Github
import com.github.search.R
import com.github.search.api.Constants
import com.github.search.api.GithubApiService
import com.github.search.databinding.FragmentRepoListBinding
import com.github.search.models.RepoItem
import com.github.search.models.RepoModel
import com.github.search.view.MainActivity
import com.github.search.view.adapter.RepoAdapter
import com.github.search.view.paging.PaginationScrollListener
import javax.inject.Inject


/**
 * Fragment class through which user interacts with the app and we show the search
 * response in this UI class as well.
 */
class RepoListFragment : Fragment(), RepoListView {

    private lateinit var binding: FragmentRepoListBinding
    private lateinit var act: MainActivity
    private lateinit var viewModel: RepoListViewModel
    private lateinit var mRepositories: ArrayList<RepoItem?>
    private lateinit var mAdapter: RepoAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private var query: String = Constants.DEFAULT_QUERY
    private var pageStart: Int = Constants.PAGE_INDEX
    private var isLoading: Boolean = false
    private var isLastPage: Boolean = false
    private var maxCount: Int = Constants.MAX_RESULT_COUNT
    private var currentPage: Int = pageStart


    /**
     * injecting our API dependency
     */

    @Inject
    lateinit var api: GithubApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Github.getComponent().inject(this)
        act = activity as MainActivity
        mRepositories = arrayListOf()
        mAdapter = RepoAdapter(mRepositories, act)
        viewModel = RepoListViewModel(api, this)
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
        showProgress()
        fetchRepositories()
        setupUI()

    }

    private fun setupUI() {
        layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.rvRepo.layoutManager = layoutManager
        binding.rvRepo.adapter = mAdapter

        binding.rvRepo.addOnScrollListener(object :
            PaginationScrollListener(binding.rvRepo.layoutManager as LinearLayoutManager) {
            override fun loadMoreItems() {
                showProgress()
                isLoading = true
                currentPage += 1
                fetchRepositories()
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

        })

        /**
         * searchview that listens to the user's queries.
         * it will make a new fetch call as soon as user submits new query
         */

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(q: String?): Boolean {
                if (q?.length!! < 256) {
                    showProgress()
                    query = q
                    resetPaginationParameters()
                    fetchRepositories()
                } else {
                    Toast.makeText(
                        act,
                        getString(R.string.message_query_length),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                binding.tvError.visibility = View.INVISIBLE
                return false
            }
        })
    }

    /**
     * function responsible for calling viewModel function to fetch repositories
     */

    private fun fetchRepositories() {
        viewModel.searchGithubRepositories(query, currentPage)
    }


    override fun didFetchRepositories(response: RepoModel) {
        act.runOnUiThread {
            /**
             * If the API response is empty, display the relevant message otherwise
             * populate the results to recyclerview adapter
             */
            if (response.items!!.isEmpty()) {
                binding.rvRepo.visibility = View.INVISIBLE
                binding.tvError.visibility = View.VISIBLE
                binding.tvError.text = getString(R.string.message_empty_result)
                hideProgress()
            } else {
                val responseCount = response.totalCount ?: 0
                binding.rvRepo.visibility = View.VISIBLE
                binding.tvError.visibility = View.INVISIBLE
                if (responseCount < maxCount) maxCount = responseCount

                setupAdapter(response)
            }

            isLoading = false

        }
    }

    /**
     * Error fetching the response from API so display the error message
     */
    override fun errorFetchingRepositories(message: String) {
        act.runOnUiThread {
            isLoading = false
            Toast.makeText(act, message, Toast.LENGTH_SHORT).show()
            hideProgress()

        }
    }


    /**
     * loading the fetched repositories to the recyclerview adapter
     */
    private fun setupAdapter(repo: RepoModel) {
        //mRepositories.clear()
        repo.items.let { mRepositories.addAll(it!!) }
        mAdapter.notifyDataSetChanged()
        if (layoutManager.itemCount > Constants.PER_PAGE) {
            binding.rvRepo.smoothScrollToPosition(layoutManager.itemCount - Constants.PER_PAGE + 1)
        }

        /**
         * Setting up the last page of the pagination
         */
        if (layoutManager.itemCount >= maxCount) isLastPage = true

        Log.e("Max Count", maxCount.toString())
        Log.e("LM SIze", layoutManager.itemCount.toString())
        Log.e("Last Page", isLastPage.toString())

        hideProgress()
    }

    private fun showProgress() {
        binding.progress.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.progress.visibility = View.INVISIBLE
    }

    private fun resetPaginationParameters(){
        isLastPage = false
        maxCount = Constants.MAX_RESULT_COUNT
        currentPage = pageStart
        mRepositories.clear()
        mAdapter.notifyDataSetChanged()
    }


}