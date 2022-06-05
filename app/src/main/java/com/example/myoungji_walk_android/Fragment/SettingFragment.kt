package com.example.myoungji_walk_android.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myoungji_walk_android.LoginActivity
import com.example.myoungji_walk_android.SearchPlaceActivity
import com.example.myoungji_walk_android.data.PrefsHelper
import com.example.myoungji_walk_android.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {

    private lateinit var binding : FragmentSettingBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        initLogoutButton()
        return binding.root
    }

    private fun initLogoutButton(){
        binding.logoutButton.setOnClickListener {
            PrefsHelper.write("accessToken", "")
            PrefsHelper.write("autoLogin", false)
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
        }
    }
}