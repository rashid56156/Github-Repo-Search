package com.github.search.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.search.R
import com.github.search.databinding.ItemRepoBinding
import com.github.search.models.RepoItem


class RepoAdapter(private val mRepositories: List<RepoItem?>, private val mContext: Context) : RecyclerView.Adapter<RepoAdapter.ViewHolder>() {

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
            binding.tvGithubLanguage.text = mRepoItem?.language

        }
    }

    override fun getItemCount(): Int {
        return mRepositories.size
    }
}