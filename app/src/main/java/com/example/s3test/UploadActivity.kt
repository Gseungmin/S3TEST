package com.example.s3test

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.s3test.databinding.ActivityUploadBinding
import com.example.umc.adapter.ImageUploadAdapter

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private lateinit var imageRVAdapter : ImageUploadAdapter
    lateinit var viewModel: ImageViewModel

    private val PICK_IMAGE_FROM_GALLERY = 1000
    private val PICK_IMAGE_FROM_GALLERY_PERMISSION = 1010

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

    /**
     * 갤러리 화면으로 이동
     * */
    private fun showGallery(activity: Activity) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        activity.startActivityForResult(intent, PICK_IMAGE_FROM_GALLERY)
    }
}