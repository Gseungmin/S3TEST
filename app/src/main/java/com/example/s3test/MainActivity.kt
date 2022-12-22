package com.example.s3test

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.amazonaws.regions.Regions
import com.example.s3test.databinding.ActivityMainBinding
import com.example.s3test.databinding.ActivityUploadBinding
import com.example.umc.adapter.CardStackAdapter
import com.example.umc.model.Image
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var cardStackAdapter: CardStackAdapter
    lateinit var manager : CardStackLayoutManager
    lateinit var viewModel: ImageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this).get(ImageViewModel::class.java)

        val cardStackView = binding.cardStackView
        manager = cardStackLayoutManager()

        val initList = mutableListOf<Image>()
        connectCardStackView(initList, cardStackView)

        binding.next.setOnClickListener {
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
        }

        val intent = getIntent()
        val file = File(intent.getStringExtra("file"))
        Log.d("file", file.toString())

        val downloadWithTransferUtility = S3Util().getInstance()
            ?.setKeys(Constants.ACCESS_KEY, Constants.ACCESS_SECRET_KEY)
            ?.setRegion(Regions.AP_NORTHEAST_2)
            ?.downloadWithTransferUtility(
                this,
                "aws-s3-study-bucket-ji",
                "s3Test", file, "test"
            )
        Log.d("file", downloadWithTransferUtility.toString())
        }

    private fun connectCardStackView(
        imageList: MutableList<Image>,
        cardStackView: CardStackView,
    ) {
        cardStackAdapter = CardStackAdapter(imageList)
        cardStackView.layoutManager = manager
        cardStackView.adapter = cardStackAdapter
    }

    /**
     * CardStackLayoutManager 사용을 위한 오버라이딩 메소드
     * */
    private fun cardStackLayoutManager() =
        CardStackLayoutManager(this, object : CardStackListener {
            override fun onCardDragging(direction: Direction?, ratio: Float) {
            }

            override fun onCardSwiped(direction: Direction?) {
            }

            override fun onCardRewound() {
            }

            override fun onCardCanceled() {
            }

            override fun onCardAppeared(view: View?, position: Int) {
            }

            override fun onCardDisappeared(view: View?, position: Int) {
            }
        })

    /**
     * uri 체크
     * */
    private fun getRealPathFromURI(uri: Uri): String {
        val buildName = Build.MANUFACTURER
        if(buildName.equals("Xiaomi")) {
            return uri.path.toString()
        }

        var columnIndex = 0
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        var cursor = contentResolver.query(uri, proj, null, null, null)

        if(cursor!!.moveToFirst()) {
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }

        return cursor.getString(columnIndex)
    }
}