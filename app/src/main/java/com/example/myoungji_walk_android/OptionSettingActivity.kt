package com.example.myoungji_walk_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.myoungji_walk_android.Model.RouteDto
import com.example.myoungji_walk_android.api.RetrofitService
import com.example.myoungji_walk_android.api.ServiceGenerator
import com.example.myoungji_walk_android.data.PrefsHelper
import com.example.myoungji_walk_android.databinding.ActivityOptionSettingBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OptionSettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOptionSettingBinding
    private val retrofitService = ServiceGenerator.createService(RetrofitService::class.java, PrefsHelper.read("token",""))

    private val startId : Int = 0
    private val destinationId : Int = 0
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
                    getListFromAPI(1)
                }
                binding.optionSafetyButton.id -> {
                    getListFromAPI(2)
                }
                binding.optionNoHillButton.id -> {
                    getListFromAPI(3)
                }
                binding.optionNoStairsButton.id -> {
                    getListFromAPI(4)
                }
            }
        }
    }

    private fun getListFromAPI(option : Int) {
        retrofitService.priviewRoute()
            .enqueue(object : Callback<RouteDto> {
                override fun onResponse(
                    call: Call<RouteDto>,
                    response: Response<RouteDto>
                ) {
                    //성공
                    if (response.isSuccessful.not()) {
                        //실패 처리에 대한 구현
                        return
                    }
                    response.body()?.let {
                        val route = RouteDto(it.start, it.goal, it.items, it.guide)
                        val intent = Intent(this@OptionSettingActivity, PreviewRouteActivity::class.java)
                        with(intent){
                            putExtra("option", option)
                            putExtra("route", route)
                            startActivity(this)
                            overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
                        }
                    }
                }
                override fun onFailure(call: Call<RouteDto>, t: Throwable) {
                    //실패
                }
            })
    }

    private fun initButton() {
        binding.backButton.setOnClickListener(initListener())
        binding.optionShortestButton.setOnClickListener(initListener())
        binding.optionSafetyButton.setOnClickListener(initListener())
        binding.optionNoHillButton.setOnClickListener(initListener())
        binding.optionNoStairsButton.setOnClickListener(initListener())
    }
}