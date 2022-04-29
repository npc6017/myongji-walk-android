package com.example.myoungji_walk_android

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewRouteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mapView.getMapAsync(this)

        route = intent.getSerializableExtra("route") as RouteDto
        getList()
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