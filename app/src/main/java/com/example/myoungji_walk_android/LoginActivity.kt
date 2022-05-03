package com.example.myoungji_walk_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.myoungji_walk_android.api.LocalService
import com.example.myoungji_walk_android.api.ServiceGenerator
import com.example.myoungji_walk_android.databinding.ActivityLoginBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private val localService = ServiceGenerator.createService(LocalService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initButton()
    }

    private fun getTokenAPI() {
        localService.loginService()
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    //성공
                    if (response.isSuccessful.not()) {
                        //실패 처리에 대한 구현
                        return
                    }
                    val token = response.headers().get("token")
                    Log.d("LoginActivity::token", token.toString())
                    //tokenManager.set(token)
                    Log.d("LoginActivity", response.code().toString())

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    //실패
                }
            })
    }

    private fun initButton(){
        binding.joinButton.setOnClickListener(buttonListener())
        binding.loginButton.setOnClickListener(buttonListener())
    }

    inner class buttonListener : View.OnClickListener {
        override fun onClick(v: View?) {
            when (v?.id) {
                binding.joinButton.id -> {
                    val intent = Intent(this@LoginActivity, JoinActivity::class.java)
                    with(intent) {
                        startActivity(this)
                        overridePendingTransition(
                            androidx.appcompat.R.anim.abc_fade_in,
                            androidx.appcompat.R.anim.abc_fade_out
                        )
                    }
                }
                binding.loginButton.id -> {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    getTokenAPI()
                    with(intent) {
                        startActivity(this)
                        overridePendingTransition(
                            androidx.appcompat.R.anim.abc_fade_in,
                            androidx.appcompat.R.anim.abc_fade_out
                        )
                    }
                }
            }
        }
    }
}