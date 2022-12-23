package com.example.s3test

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.example.s3test.Constants.ACCESS_KEY
import com.example.s3test.Constants.ACCESS_SECRET_KEY
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

        val file = File("/storage/emulated/0/Pictures/" + "test.jpg")

        downloadWithTransferUtility(this,
            "aws-s3-study-bucket-ji",
            "test", file)
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
     * S3 파일 다운로드
     *
     * @param context    Context
     * @param bucketName S3 버킷 내 폴더 경로(이름포함, /(슬래쉬) 맨 앞, 맨 뒤 없이)
     * @param fileName   파일 이름
     * @param file       저장할 Local 파일 경로
     * @param listener   AWS S3 TransferListener
     */
    fun downloadWithTransferUtility(
        context: Context?,
        bucketName: String?,
        fileName: String?,
        file: File
    ) {

        val awsCredentials: AWSCredentials = BasicAWSCredentials(
            ACCESS_KEY, ACCESS_SECRET_KEY
        )
        val s3Client = AmazonS3Client(
            awsCredentials, Region.getRegion(Regions.AP_NORTHEAST_2)
        )
        val transferUtility = TransferUtility.builder()
            .s3Client(s3Client)
            .context(context)
            .build()
        TransferNetworkLossHandler.getInstance(context)

        val downloadObserver = transferUtility.download(
            bucketName, fileName, file
        )
        downloadObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                if (state == TransferState.COMPLETED) {
                    Log.d("AWS", "DOWNLOAD Completed!")
                }
            }

            override fun onProgressChanged(id: Int, current: Long, total: Long) {
                val done = (((current.toDouble() / total) * 100.0).toInt())
                Log.d("MYTAG", "UPLOAD - - ID: $id, percent done = $done")
            }

            override fun onError(id: Int, ex: Exception) {
                Log.d("MYTAG", "UPLOAD ERROR - - ID: $id - - EX: ${ex.message.toString()}")
            }
        })
    }
}