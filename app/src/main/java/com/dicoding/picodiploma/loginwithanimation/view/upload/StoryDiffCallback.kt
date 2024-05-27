package com.dicoding.picodiploma.loginwithanimation.view.upload

import androidx.recyclerview.widget.DiffUtil
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.response.ListStoryItem

class StoryDiffCallback(
    private val oldList: List<ListStoryItem>,
    private val newList: List<ListStoryItem>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
