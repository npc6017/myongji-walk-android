package com.example.myoungji_walk_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myoungji_walk_android.Adapter.ViewPageAdapter
import com.example.myoungji_walk_android.Fragment.BestPlaceFragment
import com.example.myoungji_walk_android.Fragment.MapFragment
import com.example.myoungji_walk_android.Fragment.SettingFragment
import com.example.myoungji_walk_android.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewPager()
    }

    private fun initViewPager(){
        val list = listOf(MapFragment(), BestPlaceFragment(), SettingFragment())
        val pagerAdatper = ViewPageAdapter(list, this)
        with(binding) {
            viewPager.adapter = pagerAdatper
            val titles = listOf("지도", "인기장소", "설정")
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = titles.get(position)
            }.attach()
        }
    }


}