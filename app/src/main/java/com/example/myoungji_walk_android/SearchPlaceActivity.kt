package com.example.myoungji_walk_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.myoungji_walk_android.Adapter.HistoryAdapter
import com.example.myoungji_walk_android.Model.History
import com.example.myoungji_walk_android.Model.nodeDto
import com.example.myoungji_walk_android.api.RetrofitService
import com.example.myoungji_walk_android.api.ServiceGenerator
import com.example.myoungji_walk_android.data.PrefsHelper
import com.example.myoungji_walk_android.databinding.ActivitySearchplaceBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchPlaceActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchplaceBinding
    private lateinit var db: AppDataBase
    private lateinit var historyAdapter: HistoryAdapter
    private val retrofitService1 = ServiceGenerator.createService(RetrofitService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchplaceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = Room.databaseBuilder(
            applicationContext,
            AppDataBase::class.java,
            "LatelySearchDB"
        ).fallbackToDestructiveMigration().build()

        initBackButton()
        initSearchButton()
        initHistoryRecyclerView()
        showHistoryView()

    }

    private fun initBackButton(){
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun initSearchButton(){
        binding.searchButton.setOnClickListener {
            val placeText = binding.placeEditTextView.text.toString()
            Log.d("SearchPlaceActivity::placeText", placeText)
            if(placeText.startsWith("Y")){
                Log.d("SearchPlaceActivity", "실행되었음")
                when {
                    placeText.contains("Y23") && placeText.length == 6 -> commuteRetrofit("차세대과학관")
                    placeText.contains("Y19") && placeText.length == 6 -> commuteRetrofit("3공학관")
                    placeText.contains("Y13") && placeText.length == 6 -> commuteRetrofit("4공학관")
                    placeText.contains("Y12") && placeText.length == 6 -> commuteRetrofit("디자인조형센터")
                    placeText.contains("Y9") && placeText.length == 5 -> commuteRetrofit("함박관")
                    placeText.contains("Y8") && placeText.length == 5 -> commuteRetrofit("2공학관")
                    placeText.contains("Y6") && placeText.length == 5 -> commuteRetrofit("예체능관")
                    placeText.contains("Y5") && placeText.length == 5 -> commuteRetrofit("5공학관")
                    placeText.contains("Y3") && placeText.length == 5 -> commuteRetrofit("명진당")
                    placeText.contains("Y2") && placeText.length == 5 -> commuteRetrofit("창조예술관")
                    placeText.length == 4 -> commuteRetrofit("1공학관")
                    else -> Toast.makeText(this, "잘못된 강의실 번호입니다.", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                commuteRetrofit(placeText)
            }
        }
    }

    private fun commuteRetrofit(placeText: String){
        val accessToken = PrefsHelper.read("accessToken", "")
        retrofitService1.searchLocation("Bearer $accessToken", placeText)
            .enqueue(object: Callback<nodeDto> {
                override fun onResponse(call: Call<nodeDto>, response: Response<nodeDto>) {
                    if(response.isSuccessful.not()) {
                        Log.d("not ok", "not oK")
                        Log.d("SearchPlaceActivity::responseCode", response.code().toString())
                        Log.e("SearchPlaceActivity::response", response.message())
                        Toast.makeText(this@SearchPlaceActivity, "해당하는 장소가 없습니다.", Toast.LENGTH_SHORT).show()
                        return
                    }
                    when(response.code()){
                        200 -> {
                            response.body()?.let {
                                Log.d("ok", "oK")
                                val data = nodeDto(it.id, it.latitude, it.longitude, it.name)
                                saveSearchKeyword(placeText)
                                Log.d("SearchPlaceActivity::responseCode", response.code().toString())
                                val intent = Intent(this@SearchPlaceActivity, ConfirmPlaceActivity ::class.java)
                                with(intent) {
                                    putExtra("data", data)
                                    startActivity(this)
                                    overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
                                }
                            }
                        }
                        else -> {
                            Toast.makeText(this@SearchPlaceActivity, "해당하는 장소가 없습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }

                }
                override fun onFailure(call: Call<nodeDto>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun saveSearchKeyword(keyword: String){
        Thread {
            db.historyDao().insertHistory(History(keyword))
        }.start()
    }

    private fun deleteSearchKeyword(keyword: String){
        Thread {
            db.historyDao().delete(keyword)
            showHistoryView()
        }.start()
    }

    private fun showHistoryView(){
        Thread {
            val keywords = db.historyDao().getAll().reversed()
            runOnUiThread {
                historyAdapter.submitList(keywords.orEmpty())
            }
        }.start()
    }

    private fun initHistoryRecyclerView(){
        historyAdapter = HistoryAdapter(itemClickedListener = {
            commuteRetrofit(it)
        }, historyDeleteClickedListener = {
            deleteSearchKeyword(it)
        })
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.historyRecyclerView.adapter = historyAdapter
    }
}