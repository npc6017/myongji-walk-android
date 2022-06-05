package com.example.myoungji_walk_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.myoungji_walk_android.Model.pathFindDto
import com.example.myoungji_walk_android.api.RetrofitService
import com.example.myoungji_walk_android.api.ServiceGenerator
import com.example.myoungji_walk_android.data.PrefsHelper
import com.example.myoungji_walk_android.databinding.ActivityOptionSettingBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OptionSettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOptionSettingBinding
    private val retrofitService = ServiceGenerator.createService(RetrofitService::class.java)
    private var start : Int = 0
    private var end : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOptionSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initButton()
        initVariable()
    }

    inner class initListener : View.OnClickListener {
        override fun onClick(v: View?) {
            when (v?.id) {
                binding.backButton.id -> {
                    finish()
                }
                binding.optionLowHillButton.id -> {
                    //todo 출발지, 목적지, 옵션 서버에 전송 후 경로 받아오기
                    getListFromAPI("LOW_HILL")
                }
                binding.optionLowRainButton.id -> {
                    getListFromAPI("LOW_RAIN")
                }
                binding.optionNoStairsButton.id -> {
                    getListFromAPI("LOW_STAIR")
                }
                binding.optionSafetyButton.id -> {
                    getListFromAPI("STREET_LAMP")
                }
            }
        }
    }

    private fun initVariable(){
        start = intent.getIntExtra("start", 0)
        end = intent.getIntExtra("end", 0)
        Log.d("OptionSettingActivity::start", start.toString())
        Log.d("OptionSettingActivity::end", end.toString())
    }


    private fun getListFromAPI(weightCode : String) {
        val accessToken = PrefsHelper.read("accessToken", "")
        //testId start 1065, end 1049, weightCode = STREET_LAMP
        retrofitService.priviewRoute("Bearer $accessToken", start, end, weightCode)
            .enqueue(object : Callback<pathFindDto> {
                override fun onResponse(
                    call: Call<pathFindDto>,
                    response: Response<pathFindDto>
                ) {
                    //성공
                    if (response.isSuccessful.not()) {
                        //실패 처리에 대한 구현
                        return
                    }
                    response.body()?.let {
                        val route = pathFindDto(it.goal, it.guide, it.items, it.start, it.sumDistance)
                        val intent = Intent(this@OptionSettingActivity, PreviewRouteActivity::class.java)
                        with(intent){
                            putExtra("weightCode", weightCode)
                            putExtra("route", route)
                            startActivity(this)
                            overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
                        }
                    }
                }
                override fun onFailure(call: Call<pathFindDto>, t: Throwable) {
                    //실패
                }
            })
    }

    private fun initButton() {
        binding.backButton.setOnClickListener(initListener())
        binding.optionLowHillButton.setOnClickListener(initListener())
        binding.optionLowRainButton.setOnClickListener(initListener())
        binding.optionNoStairsButton.setOnClickListener(initListener())
        binding.optionSafetyButton.setOnClickListener(initListener())
    }
}