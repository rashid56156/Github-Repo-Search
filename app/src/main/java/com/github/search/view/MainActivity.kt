package com.github.search.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.github.search.R
import com.github.search.databinding.ActivityMainBinding
import com.github.search.view.ui.RepoListFragment

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        val fm = supportFragmentManager
        fm.beginTransaction().add(R.id.frameContainer, RepoListFragment()).commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    fun showProgress() {
        try {
            binding!!.progress.visibility = View.VISIBLE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hideProgress() {
        try {
            binding!!.progress.visibility = View.INVISIBLE
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}