package com.example.myoungji_walk_android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.myoungji_walk_android.Adapter.HistoryAdapter
import com.example.myoungji_walk_android.Model.History
import com.example.myoungji_walk_android.Model.LocalDto
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
    private val retrofitService = ServiceGenerator.createService(RetrofitService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchplaceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = Room.databaseBuilder(
            applicationContext,
            AppDataBase::class.java,
            "LatelySearchDB"
        ).build()

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
            //todo 장소 검색
            retrofitService.searchLocation()
                .enqueue(object: Callback<LocalDto> {
                    override fun onResponse(call: Call<LocalDto>, response: Response<LocalDto>) {
                        if(response.isSuccessful.not()) {
                            return
                        }
                        response.body()?.let {
                            val data = LocalDto(it.place, it.location)
                            saveSearchKeyword(placeText)
                            val intent = Intent(this@SearchPlaceActivity, ConfirmPlaceActivity ::class.java)
                            with(intent) {
                                putExtra("data", data)
                                //flag_acitivity_single_top -> 이미 생성된 메인액티비티를 그대로 사용할 수 있나?
                                //setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                                startActivity(this)
                                overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
                            }
                        }
                    }
                    override fun onFailure(call: Call<LocalDto>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
        }
    }

    private fun saveSearchKeyword(keyword: String){
        Thread {
            db.historyDao().insertHistory(History(null, keyword))
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
        historyAdapter = HistoryAdapter(historyDeleteClickedListener = {
            deleteSearchKeyword(it)
        })
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.historyRecyclerView.adapter = historyAdapter
    }
}