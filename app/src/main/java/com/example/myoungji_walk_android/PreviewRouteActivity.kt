package com.example.myoungji_walk_android

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myoungji_walk_android.Adapter.NavigationListAdapter
import com.example.myoungji_walk_android.Model.RouteDto
import com.example.myoungji_walk_android.databinding.ActivityPreviewRouteBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.overlay.PolylineOverlay
import com.naver.maps.map.util.MarkerIcons

class PreviewRouteActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityPreviewRouteBinding
    private lateinit var naverMap: NaverMap

    private var coord = mutableListOf<LatLng>()
    private val path = PathOverlay()
    private lateinit var route : RouteDto
    private var option : Int = 0

    private val recyclerViewAdapter = NavigationListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewRouteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mapView.getMapAsync(this)

        route = intent.getSerializableExtra("route") as RouteDto
        option = intent.getIntExtra("option", 0)
        getList()
        initButton()
        initData()

        binding.bottomSheetLayout.recyclerView.adapter = recyclerViewAdapter
        binding.bottomSheetLayout.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    inner class buttonListener : View.OnClickListener {
        override fun onClick(v: View?) {
            when(v?.id){
                binding.cardViewLayout.detailButton.id -> {
                    binding.bottomSheetLayout.root.visibility = View.VISIBLE
                    when(option){
                        1 -> binding.bottomSheetLayout.bottomSheetTitleTextView.text = "가장 빠른"
                        2 -> binding.bottomSheetLayout.bottomSheetTitleTextView.text = "안전한"
                        3 -> binding.bottomSheetLayout.bottomSheetTitleTextView.text = "완만한"
                        4 -> binding.bottomSheetLayout.bottomSheetTitleTextView.text = "계단 없는"
                    }
                    binding.cardViewLayout.root.visibility = View.GONE
                }
                binding.cardViewLayout.guideButton.id -> {
                    val intent = Intent(this@PreviewRouteActivity, NavigationActivity::class.java)
                    with(intent) {
                        //todo 경로전달
                        startActivity(intent)
                        overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
                    }
                }
                binding.bottomSheetLayout.bottomSheetGuideButton.id -> {
                    val intent = Intent(this@PreviewRouteActivity, NavigationActivity::class.java)
                    with(intent) {
                        //todo 경로전달
                        startActivity(intent)
                        overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
                    }
                }
            }
        }
    }

    private fun initData(){
        route.let {
            recyclerViewAdapter.submitList(it.guide)
        }
    }
    private fun initButton(){
        binding.cardViewLayout.detailButton.setOnClickListener(buttonListener())
        binding.cardViewLayout.guideButton.setOnClickListener(buttonListener())
        binding.bottomSheetLayout.bottomSheetGuideButton.setOnClickListener(buttonListener())
    }

    private fun getList(){
        route.items.forEach {
            coord.add(LatLng(it.lat, it.lng))
        }
    }

    private fun upStartMarker(){
        val marker = Marker()
        marker.position = LatLng(route.start.location[0], route.start.location[1])
        marker.map = naverMap
        marker.icon = MarkerIcons.BLACK
        marker.iconTintColor = Color.RED
    }

    private fun upGoalMarker(){
        val marker = Marker()
        marker.position = LatLng(route.goal.location[0], route.goal.location[1])
        marker.map = naverMap
        marker.icon = MarkerIcons.BLACK
        marker.iconTintColor = Color.RED
    }

    private fun getRoute(){
        //polyline.map = naverMap
        //polyline.coords = coord
        path.coords = coord
        path.color = Color.BLUE
        path.width = 10
        path.map = naverMap
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

    override fun onMapReady(map: NaverMap) {
        naverMap = map
        naverMap.minZoom = 10.0
        naverMap.maxZoom = 18.0

        val cameraUpdate = CameraUpdate.scrollTo(LatLng(route.start.location[0], route.start.location[1]))
        naverMap.moveCamera(cameraUpdate)
        getRoute()
        upStartMarker()
        upGoalMarker()
    }
}