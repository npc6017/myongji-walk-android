package com.example.myoungji_walk_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.example.myoungji_walk_android.Model.LocalDto
import com.example.myoungji_walk_android.databinding.ActivityConfirmplaceBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.Tm128
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker


class ConfirmPlaceActivity : AppCompatActivity(), OnMapReadyCallback{
    private lateinit var binding : ActivityConfirmplaceBinding
    private lateinit var naverMap: NaverMap
    private var mapx : Int = 0
    private var mapy : Int = 0
    private var address : String = ""
    private var title : String = ""

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmplaceBinding.inflate(layoutInflater)
        binding.mapView.onCreate(savedInstanceState)
        setContentView(binding.root)

        val data = intent.getSerializableExtra("data") as LocalDto
        Log.d("ConfirmPlaceActivity", data.toString())

        binding.mapView.getMapAsync(this)
        initData(data)

        initButton()
        initLocalCardView(data)
    }
    inner class buttonListener : View.OnClickListener {
        override fun onClick(v: View?) {
            when(v?.id){
                binding.backButton.id -> {
                    finish()
                }
                binding.cardViewLayout.startingPointButton.id -> {
                    val intent = Intent(this@ConfirmPlaceActivity, MainRouteSearchActivity::class.java)
                    with(intent) {
                        putExtra("title", title)
                        putExtra("divisionLocal", 101)
                        startActivity(intent)
                    }
                }
                binding.cardViewLayout.destinationButton.id -> {
                    val intent = Intent(this@ConfirmPlaceActivity, MainRouteSearchActivity::class.java)
                    with(intent) {
                        putExtra("title", title)
                        putExtra("divisionLocal", 102)
                        startActivity(intent)
                    }
                }
                binding.cardViewLayout.bookMarkButton.id -> {
                    //todo 즐겨찾기
                }
            }
        }
    }


    private fun initButton(){
        binding.backButton.setOnClickListener(buttonListener())
        binding.cardViewLayout.startingPointButton.setOnClickListener(buttonListener())
        binding.cardViewLayout.destinationButton.setOnClickListener(buttonListener())
        binding.cardViewLayout.bookMarkButton.setOnClickListener(buttonListener())
    }

    private fun initLocalCardView(data : LocalDto){
       data.items.forEach {
           binding.cardViewLayout.title.text = it.title
        }
    }

    private fun initData(data : LocalDto){
        data.items.forEach {
            binding.localName.text = it.title
            title = it.title
            mapx = it.mapx
            mapy = it.mapy
            address = it.address
        }
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map
        naverMap.minZoom = 10.0
        naverMap.maxZoom = 18.0
        val tm128 = Tm128(mapx.toDouble(), mapy.toDouble())
        val lat = tm128.toLatLng().latitude
        val lng = tm128.toLatLng().longitude
        val cameraUpdate = CameraUpdate.scrollTo(tm128.toLatLng())
        Log.d("ConfirmPlace :: ", tm128.toLatLng().toString())
        naverMap.moveCamera(cameraUpdate)
        val marker = Marker()
        marker.position = LatLng(lat, lng)
        marker.map = naverMap
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }
}