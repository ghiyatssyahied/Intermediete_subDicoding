package com.dicoding.picodiploma.loginwithanimation.view.main

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.view.detail.DetailStoryActivity
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.view.upload.StoryDiffCallback
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.databinding.ItemStoryBinding


class ListStoryAdapter(private var listStory: List<ListStoryItem>) : RecyclerView.Adapter<ListStoryAdapter.ListStoryViewHolder>() {

    inner class ListStoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemStoryBinding.bind(itemView)

        fun bind(storyItem: ListStoryItem) {
            binding.apply {
                tvItemName.text = storyItem.name
                tvTimeStamp.text = storyItem.createdAt
                Glide.with(itemView)
                    .load(storyItem.photoUrl)
                    .into(imgStoryPhoto)

                itemView.setOnClickListener {
                    val moveDataIntent = Intent(itemView.context, DetailStoryActivity::class.java)
                    moveDataIntent.putExtra("EXTRA_ID", storyItem.id)

                    val optionCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(tvItemName, "name"),
                            Pair(tvTimeStamp, "time"),
                            Pair(imgStoryPhoto, "image"),
                        )
                    itemView.context.startActivity(moveDataIntent,
                        optionCompat.toBundle()
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListStoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        return ListStoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListStoryViewHolder, position: Int) {
        val storyItem = listStory[position]
        holder.bind(storyItem)
    }

    override fun getItemCount(): Int {
        return listStory.size
    }

    fun submitList(newList: List<ListStoryItem>) {
        val diffCallback = StoryDiffCallback(listStory, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listStory = newList
        diffResult.dispatchUpdatesTo(this)
    }
}
