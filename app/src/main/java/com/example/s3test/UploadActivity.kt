package com.example.s3test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.s3test.databinding.ActivityUploadBinding
import com.example.umc.adapter.ImageUploadAdapter

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private lateinit var imageRVAdapter : ImageUploadAdapter
    lateinit var viewModel: ImageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this).get(ImageViewModel::class.java)

        viewModel.datas.observe(this) {
            imageRVAdapter = ImageUploadAdapter(viewModel.datas.value!!)
            binding.recyclerView.adapter = imageRVAdapter
            binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        }
    }
}