package com.example.myoungji_walk_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.myoungji_walk_android.Model.User
import com.example.myoungji_walk_android.api.RetrofitService
import com.example.myoungji_walk_android.api.ServiceGenerator
import com.example.myoungji_walk_android.data.PrefsHelper
import com.example.myoungji_walk_android.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    val nonTokenService = ServiceGenerator.createService(RetrofitService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initButton()
        PrefsHelper.init(applicationContext)
    }

    private fun getTokenAPI() {
        nonTokenService.loginService()
            .enqueue(object : Callback<User> {
                override fun onResponse(
                    call: Call<User>,
                    response: Response<User>
                ) {
                    //성공
                    if (response.isSuccessful.not()) {
                        //실패 처리에 대한 구현
                        return
                    }
                    val token = response.headers().get("Authorization")
                    retrofitService = ServiceGenerator.createService(RetrofitService::class.java, token)
                    response.body()?.let {
                        //Log.d("LoginActivity", it.name)
                        PrefsHelper.write("token", token)
                        PrefsHelper.write("email", it.email)
                        PrefsHelper.write("name", it.name)
                        PrefsHelper.write("grade", it.grade)
                    }
                    Log.d("LoginActivity", response.code().toString())

                }

                override fun onFailure(call: Call<User>, t: Throwable) {
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
                    getTokenAPI()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
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

    companion object{
        var retrofitService: RetrofitService? = null
    }
}