package com.dicoding.picodiploma.loginwithanimation.view.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.response.Story
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityDetailStoryBinding
import com.dicoding.picodiploma.loginwithanimation.di.Result
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import kotlinx.coroutines.launch

class DetailStoryActivity : AppCompatActivity() {
    private val viewModel by viewModels<DetailStoryViewModel>{
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityDetailStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        val id = intent.getStringExtra("EXTRA_ID").toString()

        lifecycleScope.launch {
        viewModel.getDetailStories(id).collect { result ->
            when (result) {
                is Result.Loading -> {
                    loading(true)
                }

                is Result.Success -> {
                    loading(false)
                    result.data.message.let { toast(it) }
                    result.data.story.let { setDetailStories(it) }
                }

                is Result.Error -> {
                    loading(false)
                    result.error
                }
            }
        }
    }


    }
    private fun setDetailStories(user: Story) {
        binding.tvDetailName.text = user.name
        binding.tvDetailDescription.text = user.description
        binding.tvPublishTime.text = user.createdAt
        Glide.with(binding.root.context)
            .load(user.photoUrl)
            .into(binding.ivDetailPhoto)
    }
    private fun loading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
