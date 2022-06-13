package com.example.capstoneproject.ui.main

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstoneproject.R
import com.example.capstoneproject.adapters.ListStoryAdapter
import com.example.capstoneproject.adapters.LoadingStateAdapter
import com.example.capstoneproject.databinding.ActivityMainBinding
import com.example.capstoneproject.ui.chat.ChatroomFragment
import com.example.capstoneproject.ui.detail.FeedFragment
import com.example.capstoneproject.ui.detail.ProfileFragment
import com.example.capstoneproject.ui.story.StoryActivity
import com.example.capstoneproject.ui.viewmodelfactory.StoryVMF
import com.example.capstoneproject.util.UserUtil
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*
        * If the user is not logged In, then taking to AuthenticationActivity
        * */
        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivity(Intent(this, AuthenticationActivity::class.java))
        }

        UserUtil.getCurrentUser()

        /*
        * Default Fragment
        * */
        setFragment(FeedFragment())

        /*
        * Bottom Navigation View
        * */
        binding.navigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home_item -> {
                    setFragment(FeedFragment())
                }
                R.id.consult_item -> {
                    setFragment(SearchFragment())
                }
                R.id.chat_item -> {
                    setFragment(ChatroomFragment())
                }
                R.id.profile_item -> {
                    setFragment(ProfileFragment())
                }
            }
            true
        }
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvStory.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvStory.layoutManager = LinearLayoutManager(this)
        }

        title = getString(R.string.app_name)
        setupViewModel()
        setupAction()
    }

    private fun setupViewModel() {
        val factory: StoryVMF = StoryVMF.getInstance(this)
        mainViewModel = ViewModelProvider(
            this,
            factory
        )[MainViewModel::class.java]
        mainViewModel.getToken().observe(this){ token ->
            this.token = token
            if (token.isNotEmpty()){
                val adapter = ListStoryAdapter()
                binding.rvStory.adapter = adapter.withLoadStateFooter(
                    footer = LoadingStateAdapter {
                        adapter.retry()
                    }
                )
                mainViewModel.getStories(token).observe(this){result ->
                    adapter.submitData(lifecycle, result)
                }
            }
        }

//        mainViewModel.isLogin().observe(this){
//            if (!it){
//                startActivity(Intent(this, AuthenticationActivity::class.java))
//                finish()
//            }
//        }

    }
    private fun setupAction() {
        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this, StoryActivity::class.java)
            intent.putExtra(StoryActivity.EXTRA_TOKEN, token)
            startActivity(intent)
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun setFragmentWithBackStack(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

}