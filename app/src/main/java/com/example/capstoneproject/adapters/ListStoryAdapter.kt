package com.example.capstoneproject.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.capstoneproject.R
import com.example.capstoneproject.databinding.ItemsStoryBinding
import com.example.capstoneproject.ui.detail.DetailActivity
import com.example.capstoneproject.data.local.entity.Story

class ListStoryAdapter :
    PagingDataAdapter<Story, ListStoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(holder.itemView.context, data)
        }
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val binding =
            ItemsStoryBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return MyViewHolder(binding)
    }

    class MyViewHolder(private val binding: ItemsStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, data: Story) {
            binding.apply {
                postAuthor.text = data.name
                postText.text = data.description
                itemView.setOnClickListener {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_STORY, data)
                    context.startActivity(intent)
                }
            }
            Glide.with(itemView.context)
                .load(data.photoUrl)
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.image_error)
                .into(binding.feedPostImage)
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}