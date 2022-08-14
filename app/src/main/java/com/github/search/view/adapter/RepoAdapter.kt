package com.github.search.view.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.search.R
import com.github.search.databinding.ItemRepoBinding
import com.github.search.models.RepoItem

/**
 * Recyclerview Adapter which manages a collection of repo model
 * This adapter has two view types including a white background where wiki is true, while greyed background one where
 * wiki is false
 * */

class RepoAdapter(private val mRepositories: List<RepoItem?>, private val mContext: Context) : RecyclerView.Adapter<RepoAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: ItemRepoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val mRepoItem = mRepositories[position]

            binding.tvGithubRepoName.text =  mRepoItem?.name
            binding.tvGithubUser.text = binding.root.context.getString(R.string.repo_owner, mRepoItem?.owner?.login)
            binding.tvGithubSize.text =  binding.root.context.getString(R.string.repo_size, mRepoItem?.size)
            binding.tvGithubWiki.text = binding.root.context.getString(R.string.repo_has_wiki, mRepoItem?.hasWiki.toString())

            if(mRepoItem?.hasWiki == true) {
                binding.root.setCardBackgroundColor(Color.WHITE)
                binding.tvGithubRepoName.setTextColor(Color.BLACK)
                binding.tvGithubUser.setTextColor(Color.BLACK)
                binding.tvGithubSize.setTextColor(Color.BLACK)
                binding.tvGithubWiki.setTextColor(Color.BLACK)

            } else {
                binding.root.setCardBackgroundColor(mContext.resources.getColor(R.color.colorPrimary))
                binding.tvGithubRepoName.setTextColor(Color.WHITE)
                binding.tvGithubUser.setTextColor(Color.WHITE)
                binding.tvGithubSize.setTextColor(Color.WHITE)
                binding.tvGithubWiki.setTextColor(Color.WHITE)
            }



        }
    }

    override fun getItemCount(): Int {
        return mRepositories.size
    }

}