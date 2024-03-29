package com.example.myoungji_walk_android

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myoungji_walk_android.Adapter.ViewPageAdapter
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
        permissionRequest()
    }

    private fun initViewPager(){
        val list = listOf(MapFragment(), SettingFragment())
        val pagerAdatper = ViewPageAdapter(list, this)
        with(binding) {
            viewPager.adapter = pagerAdatper
            val titles = listOf("지도", "설정")
            val iconArray = arrayOf(R.drawable.ic_baseline_map_24, R.drawable.ic_baseline_settings_24)
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = titles.get(position)
                tab.icon = getDrawable(iconArray[position])
            }.attach()
        }
    }

    private fun permissionRequest(){
        when{
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
            }
            else -> {
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1000)
            }
        }
    }
}