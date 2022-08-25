package com.github.search.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.github.search.GithubApp
import com.github.search.R
import com.github.search.databinding.ActivityMainBinding
import com.github.search.view.ui.RepoListFragment

/**
 * Main application screen. This is the app entry point.
 * */
class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)


        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )


        val fm = supportFragmentManager
        fm.beginTransaction().add(R.id.frameContainer, RepoListFragment()).commit()

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        GithubApp.clearComponent()
    }


}