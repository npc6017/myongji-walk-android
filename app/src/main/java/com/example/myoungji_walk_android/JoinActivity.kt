package com.example.myoungji_walk_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.myoungji_walk_android.Model.AccountCode
import com.example.myoungji_walk_android.Model.AccountEmail
import com.example.myoungji_walk_android.Model.AccountInfo
import com.example.myoungji_walk_android.api.RetrofitService
import com.example.myoungji_walk_android.api.ServiceGenerator
import com.example.myoungji_walk_android.databinding.ActivityJoinBinding
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
                    Log.d("JoinActivity::email", email)
                    certificationRequest(email)
                }
                binding.buttonCheckCertification.id -> {
                    val email = binding.emailEditText.text.toString()
                    val code = binding.certificationEditText.text.toString().toInt()
                    Log.d("JoinActivity::code", code.toString())
                    certificationCode(email, code)
                    //todo 인증번호 확인
                }

                binding.buttonRegister.id -> {
                    val email = binding.emailEditText.text.toString()
                    val password = binding.passwordEditText.text.toString()
                    join(email, password)
                }
            }
        }
    }

    //인증 번호 요청
    private fun certificationRequest(email : String){
        nonTokenService.certificationStudent(AccountEmail(email))
            .enqueue(object : Callback<String> {
                override fun onResponse(
                    call: Call<String>,
                    response: Response<String>
                ) {
                    //성공
                    Log.d("JoinActivity::code", response.body().toString())
                    Log.d("JoinActivity::code", response.code().toString())
                    if (response.isSuccessful.not()) {
                        Log.e("JoinActivity::response", response.message())
                        return
                    }
                    Log.d("JoinActivity::code", response.code().toString())
                    when(response.code()){
                        200 -> {
                            Toast.makeText(this@JoinActivity, "인증번호가 발송되었습니다.", Toast.LENGTH_SHORT).show()
                            //binding.buttonCheckCertification.isEnabled = true
                        }
                        else -> {
                            Toast.makeText(this@JoinActivity, "이메일을 확인해주세요.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    //실패
                    Log.e("JoinActivity::error", t.toString())
                }
            })
    }

    //인증 번호 확인
    private fun certificationCode(email : String, code : Int){
        nonTokenService.certificationNumber(AccountCode(code, email))
            .enqueue(object : Callback<String> {
                override fun onResponse(
                    call: Call<String>,
                    response: Response<String>
                ) {
                    //성공
                    Log.d("JoinActivity::code1", response.code().toString())
                    if (response.isSuccessful.not()) {
                        Log.e("JoinActivity::response", response.message())
                        return
                    }
                    Log.d("JoinActivity::code2", response.code().toString())
                    when(response.code()){
                        200 -> {
                            Toast.makeText(this@JoinActivity, "인증되었습니다.", Toast.LENGTH_SHORT).show()
                            //binding.buttonRegister.isEnabled = true
                        }
                        else -> {
                            Toast.makeText(this@JoinActivity, "인증번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun onFailure(call: Call<String>, t: Throwable) {
                    //실패
                    Log.e("JoinActivity::error", t.toString())
                }
            })
    }

    private fun join(email: String, password: String){
        nonTokenService.joinService(AccountInfo(email, password)).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                //성공
                if (response.isSuccessful.not()) {
                    return
                }
                Log.d("JoinActivity::codeAccount", response.code().toString())
                when(response.code()){
                    200 -> {
                        Toast.makeText(this@JoinActivity, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                val intent = Intent(this@JoinActivity, LoginActivity::class.java)
                startActivity(intent)
                overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                //실패
            }

        })
    }
}