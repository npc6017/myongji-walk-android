package com.example.myoungji_walk_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.myoungji_walk_android.databinding.ActivityOptionSettingBinding

class OptionSettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOptionSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOptionSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initButton()
    }

    inner class initListener : View.OnClickListener {
        override fun onClick(v: View?) {
            when (v?.id) {
                binding.backButton.id -> {
                    finish()
                }
                binding.optionShortestButton.id -> {
                    //todo 출발지, 목적지, 옵션 서버에 전송 후 경로 받아오기
                    //startActivity(Intent(this@OptionSettingActivity, ConfirmPlaceActivity::class.java))
                }
                binding.optionSafetyButton.id -> {
                    //startActivity(Intent(this@OptionSettingActivity, ConfirmPlaceActivity::class.java))
                }
                binding.optionNoHillButton.id -> {
                    //startActivity(Intent(this@OptionSettingActivity, ConfirmPlaceActivity::class.java))
                }
                binding.optionNoStairsButton.id -> {
                    //startActivity(Intent(this@OptionSettingActivity, ConfirmPlaceActivity::class.java))
                }
            }
        }
    }
    private fun initButton() {
        binding.backButton.setOnClickListener(initListener())
        binding.optionShortestButton.setOnClickListener(initListener())
        binding.optionSafetyButton.setOnClickListener(initListener())
        binding.optionNoHillButton.setOnClickListener(initListener())
        binding.optionNoStairsButton.setOnClickListener(initListener())
    }
}