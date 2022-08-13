package com.github.search.view.ui

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import javax.inject.Inject
import kotlin.collections.ArrayList
import androidx.recyclerview.widget.PagerSnapHelper

import androidx.recyclerview.widget.SnapHelper

import android.os.Looper
import com.github.search.GithubApplication
import com.github.search.api.GithubApi
import com.github.search.databinding.FragmentRepoListBinding
import com.github.search.models.Repo
import com.github.search.models.RepoItem
import com.github.search.util.AlertView
import com.github.search.view.MainActivity
import com.github.search.view.adapter.RepoAdapter


class RepoListFragment : Fragment(), RepoListView {

    private lateinit var binding: FragmentRepoListBinding
    private lateinit var act: MainActivity
    private lateinit var viewModel: RepoListViewModel
    private lateinit var mRepositories: ArrayList<RepoItem?>
    private lateinit var mAdapter: RepoAdapter



    @Inject
    lateinit var api: GithubApi

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

        fetchForecast()
        setupUI()

        return binding.root
    }


    private fun setupUI() {
        binding.rvRepo.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.rvRepo.adapter = mAdapter


        act.hideProgress()
    }



    override fun didGetRepositories(response: Repo) {
        act.runOnUiThread {
            setupAdapter(response)
        }


    }

    override fun errorProcessingRequest(message: String) {
        act.runOnUiThread {
            act.hideProgress()
            AlertView.showErrorMsg(act, message)
        }
    }

    private fun fetchForecast(){
        act.showProgress()
        viewModel.getWeatherForecast()
    }


    private fun setupAdapter(repo: Repo){
        mRepositories.clear()
        repo.items.let { mRepositories.addAll(it!!) }
        mAdapter.notifyDataSetChanged()
        act.hideProgress()
    }


}