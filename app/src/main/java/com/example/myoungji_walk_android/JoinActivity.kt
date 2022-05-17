package com.example.myoungji_walk_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myoungji_walk_android.databinding.ActivityJoinBinding

class JoinActivity : AppCompatActivity() {

    private lateinit var binding : ActivityJoinBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}