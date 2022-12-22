package com.example.s3test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.s3test.databinding.ActivityMainBinding
import com.example.s3test.databinding.ActivityUploadBinding
import com.example.umc.adapter.CardStackAdapter
import com.example.umc.model.Image
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var cardStackAdapter: CardStackAdapter
    lateinit var manager : CardStackLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val cardStackView = binding.cardStackView
        manager = cardStackLayoutManager()

        val initList = mutableListOf<Image>()
        connectCardStackView(initList, cardStackView)

        binding.next.setOnClickListener {
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
        }
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
}