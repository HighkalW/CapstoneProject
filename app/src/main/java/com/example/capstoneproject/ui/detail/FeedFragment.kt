package com.example.capstoneproject.ui.detail

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstoneproject.adapters.FeedAdapter
import com.example.capstoneproject.databinding.FragmentFeedBinding
import com.example.capstoneproject.models.Post
import com.example.capstoneproject.ui.main.CallEmergencyActivity
import com.example.capstoneproject.ui.story.CreatePostActivity
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore


class FeedFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: FeedAdapter

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fab.setOnClickListener {
            startActivity(Intent(context, CreatePostActivity::class.java))
        }
        binding.emergency.setOnClickListener {
            startActivity(Intent(context, CallEmergencyActivity::class.java))
        }

        recyclerView = binding.feedRecyclerView

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val firestore = FirebaseFirestore.getInstance()
        val query = firestore.collection("Posts")

        val recyclerViewOptions =
            FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post::class.java).build()

        context?.let {
            adapter = FeedAdapter(recyclerViewOptions, it)
        }

        if (this::adapter.isInitialized) {
            recyclerView.adapter = adapter
        }

        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.itemAnimator = null
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "FeedFragment"
    }
}