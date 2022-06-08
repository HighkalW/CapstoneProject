package com.example.capstoneproject.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.capstoneproject.R
import com.example.capstoneproject.databinding.ActivityMainBinding
import com.example.capstoneproject.ui.chat.ChatroomFragment
import com.example.capstoneproject.ui.detail.FeedFragment
import com.example.capstoneproject.ui.detail.ProfileFragment
import com.example.capstoneproject.util.UserUtil
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
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