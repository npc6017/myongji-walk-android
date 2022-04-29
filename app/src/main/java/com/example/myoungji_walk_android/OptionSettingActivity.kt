package com.example.myoungji_walk_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.myoungji_walk_android.Model.LocalDto
import com.example.myoungji_walk_android.Model.RouteDto
import com.example.myoungji_walk_android.api.LocalService
import com.example.myoungji_walk_android.databinding.ActivityOptionSettingBinding
import com.naver.maps.geometry.LatLng
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
                    getListFromAPI()
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

    private fun getListFromAPI() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://run.mocky.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(LocalService::class.java).also {
            it.priviewRoute()
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
                                putExtra("route", route)
                                startActivity(this)
                            }
//                            dto.items.forEach {
//                                Log.d("OptionSettingActivity: ", it.lat.toString())
//                                Log.d("OptionSettingActivity: ", it.lng.toString())
//                            }
//                            dto.guide.forEach {
//                                Log.d("OptionSettingActivity: ", it.pointIndex.toString())
//                                Log.d("OptionSettingActivity: ", it.type.toString())
//                                Log.d("OptionSettingActivity: ", it.distance)
//                            }
                        }
                    }

                    override fun onFailure(call: Call<RouteDto>, t: Throwable) {
                        //실패
                    }
                })
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