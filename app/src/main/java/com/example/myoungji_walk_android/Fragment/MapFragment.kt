package com.example.myoungji_walk_android.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myoungji_walk_android.MainRouteSearchActivity
import com.example.myoungji_walk_android.SearchPlaceActivity
import com.example.myoungji_walk_android.ar.NavigationActivity
import com.example.myoungji_walk_android.databinding.FragmentMapBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding : FragmentMapBinding
    private lateinit var naverMap: NaverMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        initInputPlaceButton()
        initFindPlaceButton()
        //ar 테스트용 진입버튼
//        binding.gotoAr.setOnClickListener {
//            val intent = Intent(requireContext(), NavigationActivity::class.java)
//            startActivity(intent)
//        }
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)
        return binding.root
    }

    fun initInputPlaceButton(){
        binding.buttonInputPlace.setOnClickListener {
            val intent = Intent(requireContext(), SearchPlaceActivity::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
        }
    }

    fun initFindPlaceButton(){
        binding.buttonFindDirection.setOnClickListener {
            val intent = Intent(requireContext(), MainRouteSearchActivity::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
        }
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
        val cameraUpdate = CameraUpdate.scrollTo(LatLng(37.221761383374904,127.18567641896878))
        naverMap.moveCamera(cameraUpdate)
    }
}