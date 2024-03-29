package com.example.myoungji_walk_android

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.myoungji_walk_android.Model.BookMark
import com.example.myoungji_walk_android.Model.nodeDto
import com.example.myoungji_walk_android.databinding.ActivityConfirmplaceBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker


class ConfirmPlaceActivity : AppCompatActivity(), OnMapReadyCallback{
    private lateinit var binding : ActivityConfirmplaceBinding
    private lateinit var db: AppDataBaseBookMark
    private lateinit var naverMap: NaverMap
    private var title : String = ""
    private var id : Int = 0
    private lateinit var data : nodeDto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmplaceBinding.inflate(layoutInflater)
        binding.mapView.onCreate(savedInstanceState)
        setContentView(binding.root)
        db = Room.databaseBuilder(
            applicationContext,
            AppDataBaseBookMark::class.java,
            "BookMarkDB"
        ).build()

        data = intent.getSerializableExtra("data") as nodeDto
        id = data.id
        title = data.name
        binding.mapView.getMapAsync(this)

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
                        putExtra("id", id)
                        putExtra("title", title)
                        putExtra("divisionLocal", 101)
                        startActivity(intent)
                        overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
                    }
                }
                binding.cardViewLayout.destinationButton.id -> {
                    val intent = Intent(this@ConfirmPlaceActivity, MainRouteSearchActivity::class.java)
                    with(intent) {
                        putExtra("id", id)
                        putExtra("title", title)
                        putExtra("divisionLocal", 102)
                        startActivity(intent)
                        overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
                    }
                }
                binding.cardViewLayout.bookMarkCheckBox.id -> {
                    if(binding.cardViewLayout.bookMarkCheckBox.isChecked){
                        saveBookMark(id, binding.cardViewLayout.title.text.toString())
                    }
                    else {
                        deleteBookMark(binding.cardViewLayout.title.text.toString())
                    }
                }
            }
        }
    }

    private fun initBookMarkButton(keyword: String){
        Thread {
            if(db.bookMarkDao().search((keyword))){
                binding.cardViewLayout.bookMarkCheckBox.isChecked = true
            }
        }.start()
    }

    private fun saveBookMark(id : Int, keyword: String){
        Thread {
            db.bookMarkDao().insertHistory(BookMark(id, keyword))
        }.start()
    }

    private fun deleteBookMark(keyword: String){
        Thread {
            db.bookMarkDao().delete(keyword)
        }.start()
    }

    private fun initButton(){
        binding.backButton.setOnClickListener(buttonListener())
        binding.cardViewLayout.startingPointButton.setOnClickListener(buttonListener())
        binding.cardViewLayout.destinationButton.setOnClickListener(buttonListener())
        binding.cardViewLayout.bookMarkCheckBox.setOnClickListener(buttonListener())
    }

    private fun initLocalCardView(data : nodeDto){
        initBookMarkButton(data.name)
        binding.cardViewLayout.title.text = data.name
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map
        naverMap.minZoom = 10.0
        naverMap.maxZoom = 18.0
        val cameraUpdate = CameraUpdate.scrollTo(LatLng(data.latitude.toDouble(), data.longitude.toDouble()))
        naverMap.moveCamera(cameraUpdate)
        val marker = Marker()
        marker.position = LatLng(data.latitude.toDouble(), data.longitude.toDouble())
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