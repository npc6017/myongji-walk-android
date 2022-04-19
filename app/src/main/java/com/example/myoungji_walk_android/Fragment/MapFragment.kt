package com.example.myoungji_walk_android.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myoungji_walk_android.MainRouteSearchActivity
import com.example.myoungji_walk_android.SearchPlaceActivity
import com.example.myoungji_walk_android.databinding.FragmentMapBinding

class MapFragment : Fragment() {

    private lateinit var binding : FragmentMapBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        initInputPlaceButton()
        initFindPlaceButton()
        return binding.root
    }

    fun initInputPlaceButton(){
        binding.buttonInputPlace.setOnClickListener {
            val intent = Intent(requireContext(), SearchPlaceActivity::class.java)
            startActivity(intent)
        }
    }

    fun initFindPlaceButton(){
        binding.buttonFindDirection.setOnClickListener {
            val intent = Intent(requireContext(), MainRouteSearchActivity::class.java)
            startActivity(intent)
        }
    }
}