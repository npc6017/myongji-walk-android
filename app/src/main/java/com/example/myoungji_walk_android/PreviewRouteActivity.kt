package com.example.myoungji_walk_android

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myoungji_walk_android.Adapter.NavigationListAdapter
import com.example.myoungji_walk_android.Model.pathFindDto
import com.example.myoungji_walk_android.ar.NavigationActivity
import com.example.myoungji_walk_android.databinding.ActivityPreviewRouteBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.util.MarkerIcons

class PreviewRouteActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityPreviewRouteBinding
    private lateinit var naverMap: NaverMap

    private var coord = mutableListOf<LatLng>()
    private val path = PathOverlay()
    private lateinit var route : pathFindDto
    private var option : String = ""

    private val recyclerViewAdapter = NavigationListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewRouteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mapView.getMapAsync(this)

        route = intent.getSerializableExtra("route") as pathFindDto
        option = intent.getStringExtra("weightCode")!!
        getList()
        initButton()
        initData()

        binding.bottomSheetLayout.recyclerView.adapter = recyclerViewAdapter
        binding.bottomSheetLayout.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    inner class buttonListener : View.OnClickListener {
        @SuppressLint("SetTextI18n")
        override fun onClick(v: View?) {
            when(v?.id){
                binding.cardViewLayout.detailButton.id -> {
                    binding.bottomSheetLayout.root.visibility = View.VISIBLE
                    when(option){
                        "LOW_HILL" -> binding.bottomSheetLayout.bottomSheetTitleTextView.text = "완만한"
                        "LOW_RAIN" -> binding.bottomSheetLayout.bottomSheetTitleTextView.text = "비를 피할 수 있는"
                        "LOW_STAIR" -> binding.bottomSheetLayout.bottomSheetTitleTextView.text = "계단 없는"
                        "STREET_LAMP" -> binding.bottomSheetLayout.bottomSheetTitleTextView.text = "안전한"
                    }
                    binding.bottomSheetLayout.bottomSheetDistanceTextView.text = "${route.sumDistance}M"
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

    @SuppressLint("SetTextI18n")
    private fun initData(){
        route.let {
            recyclerViewAdapter.submitList(it.guide)
        }
        binding.cardViewLayout.distanceTextView.text = "${route.sumDistance}M"
    }
    private fun initButton(){
        binding.cardViewLayout.detailButton.setOnClickListener(buttonListener())
        binding.cardViewLayout.guideButton.setOnClickListener(buttonListener())
        binding.bottomSheetLayout.bottomSheetGuideButton.setOnClickListener(buttonListener())
    }

    private fun getList(){
        route.items.forEach {
            coord.add(LatLng(it.latitude.toDouble(), it.longitude.toDouble()))
        }
    }

    private fun upStartMarker(){
        val marker = Marker()
        marker.position = LatLng(route.start.latitude.toDouble(), route.start.longitude.toDouble())
        marker.map = naverMap
        marker.icon = MarkerIcons.BLACK
        marker.iconTintColor = Color.RED
    }

    private fun upGoalMarker(){
        val marker = Marker()
        marker.position = LatLng(route.goal.latitude.toDouble(), route.goal.longitude.toDouble())
        marker.map = naverMap
        marker.icon = MarkerIcons.BLACK
        marker.iconTintColor = Color.RED
    }

    private fun getRoute(){
        path.width = 30
        path.patternImage = OverlayImage.fromResource(R.drawable.arrow_path)
        path.patternInterval = 60
        path.outlineWidth = 5
        path.color = Color.parseColor("#00AAFF")
        path.coords = coord
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

        val cameraUpdate = CameraUpdate.scrollTo(LatLng(route.start.latitude.toDouble(), route.start.longitude.toDouble()))
        naverMap.moveCamera(cameraUpdate)
        getRoute()
        upStartMarker()
        upGoalMarker()
    }
}