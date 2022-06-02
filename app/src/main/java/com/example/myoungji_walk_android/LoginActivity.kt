package com.example.myoungji_walk_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myoungji_walk_android.Model.AccountInfo
import com.example.myoungji_walk_android.Model.Token
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

    private fun getTokenAPI(email: String, password: String) {
        nonTokenService.loginService(AccountInfo(email, password))
            .enqueue(object : Callback<Token> {
                override fun onResponse(
                    call: Call<Token>,
                    response: Response<Token>
                ) {
                    //성공
                    if (response.isSuccessful.not()) {
                        Toast.makeText(this@LoginActivity, "정보가 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
                        return
                    }
                    response.body().let {
                        val accessToken = it?.accessToken
                        Log.d("LoginActivity::accessToken", accessToken.toString())
                        PrefsHelper.write("accessToken", accessToken)
                        //retrofitService = ServiceGenerator.createService(RetrofitService::class.java, accessToken)
                    }
                    when(response.code()){
                        200 -> {
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            with(intent) {
                                startActivity(this)
                                overridePendingTransition(
                                    androidx.appcompat.R.anim.abc_fade_in,
                                    androidx.appcompat.R.anim.abc_fade_out
                                )
                            }
                        }
                        else -> {
                            Toast.makeText(this@LoginActivity, "정보가 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }

                }

                override fun onFailure(call: Call<Token>, t: Throwable) {
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
                    val email = binding.EmailEditText.text.toString()
                    val password = binding.passwordEditText.text.toString()
                    getTokenAPI(email, password)
                }
            }
        }
    }

    companion object{
        //var retrofitService: RetrofitService? = null
    }
}