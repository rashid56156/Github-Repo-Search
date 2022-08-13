package com.github.search.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.search.GithubApplication
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

    private var query: String = Constants.DEFAULT_QUERY
    private var pageStart: Int = Constants.DEFAULT_PAGE
    private var isLoading: Boolean = false
    private var isLastPage: Boolean = false
    private var totalPages: Int = 1
    private var currentPage: Int = pageStart



    @Inject
    lateinit var api: GithubApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GithubApplication.getComponent().inject(this)
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
         * fragment oNviewCreated is called so now we can make the network call and
         * set up our UI as well.
         */
        fetchRepositories()
        setupUI()

    }

    private fun setupUI() {
        binding.rvRepo.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.rvRepo.adapter = mAdapter

        binding.rvRepo.addOnScrollListener(object : PaginationScrollListener(binding.rvRepo.layoutManager as LinearLayoutManager) {
            override fun loadMoreItems() {
                if(!isLoading) {
                    isLoading = true
                    currentPage += 1
                    fetchRepositories()
                }
            }

            override fun getTotalPageCount(): Int {
                return totalPages
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

        })

        /**
         * Searchview that listens to the user's queries
         */

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(q: String?): Boolean {
                if(q?.length!! < 256) {
                    query = q
                    mRepositories.clear()
                    mAdapter.notifyDataSetChanged()
                    fetchRepositories()
                } else {
                        Toast.makeText(act, getString(R.string.message_query_length), Toast.LENGTH_SHORT).show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                binding.tvError.visibility = View.INVISIBLE
                return false
            }
        })
    }

    private fun fetchRepositories(){
        showProgress()
        viewModel.searchGithubRepositories(query, currentPage)
    }


    override fun didFetchRepositories(response: RepoModel) {
        act.runOnUiThread {
            isLoading = false
            if(response.items!!.isEmpty()){
                binding.rvRepo.visibility = View.INVISIBLE
                binding.tvError.visibility = View.VISIBLE
                binding.tvError.text = getString(R.string.message_empty_result)
                hideProgress()
            } else {
                binding.rvRepo.visibility = View.VISIBLE
                binding.tvError.visibility = View.INVISIBLE
            }
            setupAdapter(response)
        }
    }

    override fun errorFetchingRepositories(message: String) {
        act.runOnUiThread {
            isLoading = false
            binding.rvRepo.visibility = View.INVISIBLE
            binding.tvError.visibility = View.VISIBLE
            binding.tvError.text = message
            hideProgress()

        }
    }


    /**
     * loading the fetched repositories to the recyclerview adapter
     */
    private fun setupAdapter(repo: RepoModel){
        repo.items.let { mRepositories.addAll(it!!) }
        mAdapter.notifyDataSetChanged()
        hideProgress()
    }

    private fun showProgress() {
        binding.progress.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.progress.visibility = View.INVISIBLE
    }


}