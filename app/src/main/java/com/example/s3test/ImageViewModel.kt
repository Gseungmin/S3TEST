package com.example.s3test

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ImageViewModel() : ViewModel() {

    private var _datas = MutableLiveData<List<Uri>>()
    val datas : LiveData<List<Uri>>
        get() = _datas

    fun setImage(images: List<Uri>) {
        _datas.value = images
    }
}