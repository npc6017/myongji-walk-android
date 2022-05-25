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

class MapFragment : Fragment() {

    private lateinit var binding : FragmentMapBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        initInputPlaceButton()
        initFindPlaceButton()
        //ar 테스트용 진입버튼
        binding.gotoAr.setOnClickListener {
            val intent = Intent(requireContext(), NavigationActivity::class.java)
            startActivity(intent)
        }
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
}