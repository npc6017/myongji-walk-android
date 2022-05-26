package com.example.myoungji_walk_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.myoungji_walk_android.Adapter.HistoryAdapter
import com.example.myoungji_walk_android.LoginActivity.Companion.retrofitService
import com.example.myoungji_walk_android.Model.History
import com.example.myoungji_walk_android.Model.LocalDto
import com.example.myoungji_walk_android.api.RetrofitService
import com.example.myoungji_walk_android.api.ServiceGenerator
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
            retrofitService1.searchLocation(placeText)
                .enqueue(object: Callback<LocalDto> {
                    override fun onResponse(call: Call<LocalDto>, response: Response<LocalDto>) {
                        if(response.isSuccessful.not()) {
                            Log.d("not ok", "not oK")
                            Log.d("SearchPlaceActivity::respneseCode", response.code().toString())
                            Log.e("SearchPlaceActivity::response", response.message())
                            return
                        }
                        response.body()?.let {
                            Log.d("ok", "oK")
                            val data = LocalDto(it.latitude, it.longitude, it.name)
                            //saveSearchKeyword(placeText)
                            Log.d("SearchPlaceActivity::respneseCode", response.code().toString())
                            val intent = Intent(this@SearchPlaceActivity, ConfirmPlaceActivity ::class.java)
                            with(intent) {
                                putExtra("data", data)
                                startActivity(this)
                                //overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
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