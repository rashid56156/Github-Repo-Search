package com.github.search.view.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.search.GithubApplication
import com.github.search.R
import com.github.search.api.Constants
import com.github.search.api.GithubApiService
import com.github.search.databinding.FragmentRepoListBinding
import com.github.search.models.RepoItem
import com.github.search.models.RepoModel
import com.github.search.util.AlertView
import com.github.search.view.MainActivity
import com.github.search.view.adapter.RepoAdapter
import com.github.search.view.paging.PaginationScrollListener
import javax.inject.Inject


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

        fetchRepositories()
        setupUI()

        return binding.root
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

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(q: String?): Boolean {
                query = q!!
                mRepositories.clear()
                mAdapter.notifyDataSetChanged()
                fetchRepositories()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                binding.tvError.visibility = View.INVISIBLE
                return false
            }
        })
    }

    override fun didGetRepositories(response: RepoModel) {
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

    override fun errorProcessingRequest(message: String) {
        act.runOnUiThread {
            isLoading = false
            binding.rvRepo.visibility = View.INVISIBLE
            binding.tvError.visibility = View.VISIBLE
            binding.tvError.text = getString(R.string.message_fetching_error)
            hideProgress()
            AlertView.showErrorMsg(act, message)
        }
    }

    private fun fetchRepositories(){
        showProgress()
        viewModel.searchGithubRepositories(query, currentPage)
    }


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