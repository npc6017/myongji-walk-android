package com.example.myoungji_walk_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.myoungji_walk_android.Model.User
import com.example.myoungji_walk_android.api.RetrofitService
import com.example.myoungji_walk_android.api.ServiceGenerator
import com.example.myoungji_walk_android.data.PrefsHelper
import com.example.myoungji_walk_android.databinding.ActivityJoinBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JoinActivity : AppCompatActivity() {

    private lateinit var binding : ActivityJoinBinding
    val nonTokenService = ServiceGenerator.createService(RetrofitService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initButton()
    }

    private fun initButton(){
        binding.buttonCertification.setOnClickListener(buttonListener())
        binding.buttonCheckCertification.setOnClickListener(buttonListener())
        binding.buttonRegister.setOnClickListener(buttonListener())
    }

    inner class buttonListener : View.OnClickListener {
        override fun onClick(v: View?) {
            when (v?.id) {
                binding.buttonCertification.id -> {
                    //todo 이메일로 인증번호 요청
                    val email = binding.emailEditText.text.toString()
                    certificationRequest(email)
                }
                binding.buttonCheckCertification.id -> {
                    val email = binding.emailEditText.text.toString()
                    val code = binding.certificationEditText.text.toString().toInt()
                    certificationCode(email, code)
                    //todo 인증번호 확인
                }

                binding.buttonCheckCertification.id -> {
                    join()
                }
            }
        }
    }

    //인증 번호 요청
    private fun certificationRequest(email : String){
        nonTokenService.certificationStudent(email)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    //성공
                    if (response.isSuccessful.not()) {
                        return
                    }
                    val intent = Intent(this@JoinActivity, LoginActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
                    Log.d("JoinActivity::code", response.code().toString())
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    //실패
                }
            })
    }

    //인증 번호 확인
    private fun certificationCode(email : String, code : Int){
        nonTokenService.certificationNumber(email, code)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    //성공
                    if (response.isSuccessful.not()) {
                        return
                    }
                    Log.d("JoinActivity::code", response.code().toString())

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    //실패
                }
            })
    }

    private fun join(){
        nonTokenService.joinService().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                //성공
                if (response.isSuccessful.not()) {
                    return
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                //실패
            }

        })
    }
}