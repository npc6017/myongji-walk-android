package com.example.myoungji_walk_android

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.myoungji_walk_android.Adapter.BookMarkAdapter
import com.example.myoungji_walk_android.Adapter.HistoryAdapter
import com.example.myoungji_walk_android.Adapter.NavigationListAdapter
import com.example.myoungji_walk_android.databinding.ActivityMainroutesearchBinding

class MainRouteSearchActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainroutesearchBinding
    private var divisionLocal : Int? = 0
    private lateinit var db: AppDataBaseBookMark
    private val bookMarkAdapter = BookMarkAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainroutesearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        divisionLocal = intent?.getIntExtra("divisionLocal", 0)

        db = Room.databaseBuilder(
            applicationContext,
            AppDataBaseBookMark::class.java,
            "BookMarkDB"
        ).build()

        initButton()
        initOptionSettingButton()
        initText()
        initBookMarkRecyclerView()
        showBookMarkView()
    }

    inner class initListener : View.OnClickListener {
        override fun onClick(v: View?) {
            when(v?.id) {
                binding.startInputButton.id -> {
                    startActivity(Intent(this@MainRouteSearchActivity, SearchPlaceActivity::class.java))
                }
                binding.destinationInputButton.id -> {
                    startActivity(Intent(this@MainRouteSearchActivity, SearchPlaceActivity::class.java))
                }
                binding.changeButton.id -> {
                    val temp : String = startTitle
                    startTitle = destinationTitle
                    destinationTitle = temp
                    binding.destinationInputButton.text = startTitle
                    binding.startInputButton.text = destinationTitle
                }
            }

        }
    }

    private fun showBookMarkView(){
        Thread {
            val keywords = db.bookMarkDao().getAll().reversed()
            runOnUiThread {
                bookMarkAdapter.submitList(keywords.orEmpty())
            }
        }.start()
    }

    private fun initBookMarkRecyclerView(){
        binding.bookMarkRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.bookMarkRecyclerView.adapter = bookMarkAdapter
    }

    private fun initButton(){
        binding.startInputButton.setOnClickListener(initListener())
        binding.destinationInputButton.setOnClickListener(initListener())
        binding.changeButton.setOnClickListener(initListener())
    }

    private fun initOptionSettingButton(){
        binding.optionSettingButton.setOnClickListener {
            startActivity(Intent(this, OptionSettingActivity::class.java))
            overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
        }
    }

    private fun initText(){
        if(divisionLocal == 101){
            startTitle = intent?.getStringExtra("title").toString()
        }
        if(divisionLocal == 102){
            destinationTitle = intent?.getStringExtra("title").toString()
        }
        binding.startInputButton.text = startTitle
        binding.destinationInputButton.text = destinationTitle
    }

    companion object {
        var startTitle : String = "장소를 입력하세요."
        var destinationTitle : String = "장소를 입력하세요."
    }
}