package com.example.myoungji_walk_android

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.myoungji_walk_android.databinding.FragmentArBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.ar.core.*
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.ArSceneView
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import com.gorisse.thomas.sceneform.util.getResourceUri
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PathOverlay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class ARActivity : AppCompatActivity(), SensorEventListener, OnMapReadyCallback {

    //센서 변수
    private lateinit var mSensorManager: SensorManager
    private lateinit var mAccelerometer: Sensor
    private lateinit var mMagnetometer: Sensor
    lateinit var mLocation: Location
    private lateinit var locationManager: LocationManager
    private var azimuthDegrees = 0f  // y축 각도
    private val noAccel = floatArrayOf(0f, 3f, 3f)
    private val mLastMagnetometer = FloatArray(3)
    private var mLastAccelerometerSet = false
    private var mLastMagnetometerSet = false
    private val mR = FloatArray(9)
    private val mOrientation = FloatArray(3)

    //AR 변수
    private lateinit var arFragment: ArFragment
    private lateinit var arSceneView: ArSceneView
    private val node = Node()
    lateinit var session: Session
    private lateinit var anchorNode: AnchorNode
    private lateinit var binding: FragmentArBinding
    private lateinit var locationModel: LocationModel
    private lateinit var modelRender: ModelRender

    //거리, 각도변수
    private lateinit var targetLocation: Location //사용자에게 가장 가까운 위치
    private lateinit var lastLocation: Location //목적지
    private var currentDistance: Double = 0.0 //사용자에게 가장 가까운 위치까지 거리
    private var lastDistance: Double = 0.0 //목적지까지 거리
    private var angle = 0F // 북위각도
    private var angleNext = 0F // 북위각도


    //위도 경도 형식으로 받아오는 배열값
    var gpsNodePointArrayList = ArrayList<DoubleArray>()

    /**hide this**/
    private val gpsNodePoint = arrayOf(
        doubleArrayOf(37.64450, 126.69155),
        doubleArrayOf(37.64447, 126.69070),
        doubleArrayOf(37.64442, 126.68899),
        doubleArrayOf(37.64452, 126.68715),
        doubleArrayOf(37.64485, 126.68586),
        doubleArrayOf(37.64489, 126.68569)

    )

    //권한 체크
    private fun checkPermission() {
        //권한 얻기 - GPS
        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_DENIED -> {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS
                )
            }
        }
    }

    //locationListener 선언
    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            mLocation = location

        }
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_ar)

        locationModel = LocationModel()
        modelRender = ModelRender()

        modelRender.checkPointModel()
        modelRender.arrowModel()

        //tast checkpoint. 추후 서버에서 받아올 수 있도록
        gpsNodePointArrayList.addAll(listOf(*gpsNodePoint))

        //센서 초기화
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        if (locationManager
                .getLastKnownLocation(LocationManager.GPS_PROVIDER) != null
        ) {
            mLocation = locationManager
                .getLastKnownLocation(LocationManager.GPS_PROVIDER)!!
        }else{
            mLocation = Location("myLoc")
        }

        checkPermission()
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            1000,  // interval.
            1f,  // 10 meters
            locationListener
        )

        //AR 화면 실행

        arFragment = supportFragmentManager.findFragmentById(R.id.arFragment) as ArFragment
        arSceneView = arFragment.arSceneView


        arSceneView.session = Session(this)

        session = arSceneView.session!!
        val config = session.config
        config.planeFindingMode = Config.PlaneFindingMode.HORIZONTAL
        config.lightEstimationMode = Config.LightEstimationMode.DISABLED
        arSceneView.session!!.configure(config)

        CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                distanceCal()
                nearCheckPoint()
                delay(1000)
            }
        }

        //naverMap
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as MapFragment
        mapFragment.getMapAsync(this)

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)

        binding.button.setOnClickListener(View.OnClickListener {
            if(bottomSheetBehavior.state==BottomSheetBehavior.STATE_COLLAPSED){
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                binding.button.setBackgroundResource(R.drawable.bar_down)
            }else{
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                binding.button.setBackgroundResource(R.drawable.bar_up)
            }
        })

        arFragment.arSceneView.scene.addOnUpdateListener {
            CoroutineScope(Dispatchers.Main).launch {
                createAnchor()
            }
        }
    }

    //가속도, 자기장 센서 값 받아오기
    override fun onSensorChanged(event: SensorEvent) {
        //두 센서값을 다 받아왔을 때 값 계산
        if (event.sensor == mAccelerometer) {
            mLastAccelerometerSet = true
        } else if (event.sensor == mMagnetometer) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.size)
            mLastMagnetometerSet = true
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(mR, null, noAccel, mLastMagnetometer)

            azimuthDegrees = ((Math.toDegrees(
                SensorManager.getOrientation(mR, mOrientation)[0]
                    .toDouble()
            ) + 360).toInt() % 360).toFloat()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    //가장 가까운 좌표와 다음 좌표의 거리 계산
    private fun distanceCal() {

        //현재위치에서 가장 가까운 위,경도 Location 객체로 변환
        targetLocation =
            locationModel.coordToLocation(gpsNodePointArrayList[0][0], gpsNodePointArrayList[0][1])

        //목적지 위,경도 Location 객체로 변환
        lastLocation = locationModel.coordToLocation(
            gpsNodePointArrayList[gpsNodePointArrayList.size - 1][0],
            gpsNodePointArrayList[gpsNodePointArrayList.size - 1][1]
        )

        //targetLocation 과 현재 위치의 거리 계산
        currentDistance = locationModel.getDistance(
            mLocation, targetLocation
        )

        //targetLocation 과 현재 위치의 각도 계산
        angle = locationModel.getAngle(mLocation, targetLocation).toFloat()

        angleNext = locationModel.getAngle(targetLocation, locationModel.coordToLocation(gpsNodePointArrayList[1][0], gpsNodePointArrayList[1][1])).toFloat()

        lastDistance = locationModel.getDistance(mLocation, lastLocation)
        binding.angle.text = "angle : " + angle
        binding.nextAngle.text = "next_angle : " + angleNext
        binding.checkPoint.text = "체크포인트 : " + gpsNodePointArrayList.size + " 개"
        binding.target.text = "남은 거리 : " + (Math.round(currentDistance * 100) / 100.0) + " m"
        binding.now.text =
            "현재좌표 : " + (mLocation.latitude.toString() + "," + mLocation.longitude.toString())
    }

    private fun nearCheckPoint() {
        //gps 위치가 최신 정보인지 확인
        if(TimeUnit.NANOSECONDS.toSeconds(
                SystemClock.elapsedRealtimeNanos()
                    - mLocation.elapsedRealtimeNanos)<100) {
            //일정 거리 이상 가까이 오면 다음 체크포인트로
            if (currentDistance <= 4) {
                gpsNodePointArrayList.removeAt(0)
            }
            val tmp2 = locationModel.getDistance(targetLocation, lastLocation)
            if (tmp2 > lastDistance) {
                gpsNodePointArrayList.removeAt(0)
            }
            //남은 체크포인트 중 지나친 체크포인트 체크
            try {
                if (gpsNodePointArrayList.size > 0) {
                    for (i in gpsNodePointArrayList.indices) {
                        val tmp = locationModel.getDistance(
                            mLocation, locationModel.coordToLocation(
                                gpsNodePointArrayList[i][0],
                                gpsNodePointArrayList[i][1]
                            )
                        )
                        //다음 점이 더 가깝고 일정 거리 이하라면
                        if (currentDistance > tmp) {
                            for (j in 0..i) {
                                gpsNodePointArrayList.removeAt(j)
                            }
                            break
                        }
                    }
                } else {
                    Toast.makeText(this, "경로 탐색 완료!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e: Exception) {
                Toast.makeText(this, e.message!!, Toast.LENGTH_SHORT).show()
                finish()
            }
        }else{
                //Toast.makeText(this,"gps 수신중입니다." ,Toast.LENGTH_SHORT).show()
        }
    }

    private fun createAnchor() {
        //탐지된 평면 데이터 가져오기
        val planes = arFragment.arSceneView.arFrame!!.getUpdatedTrackables(
            Plane::class.java
        )
        val planePose: Pose
        val tmpPose: Pose

        for (plane: Plane in planes) {
            if (plane.trackingState == TrackingState.TRACKING) {

                planePose = plane.centerPose

                val dPose = arFragment.arSceneView.arFrame!!.camera.displayOrientedPose

                val tmpVec = floatArrayOf(dPose.tx(), planePose.ty(), dPose.tz())
                tmpPose = Pose.makeTranslation(tmpVec)

                if (this::anchorNode.isInitialized && anchorNode.anchor != null) {
                    anchorNode.anchor!!.detach()
                    Log.d("detach", "detach")
                }
                val anchor = plane.createAnchor(tmpPose)
                anchorNode = AnchorNode(anchor)
                addToModelScene()

                return
            }
        }
    }

    private fun addToModelScene() {
        val transformableNode = TransformableNode(arFragment.transformationSystem)
        val rotateAngle = (-azimuthDegrees + angle) % 360

        val quaternion1 = Quaternion.axisAngle(Vector3(0F, -1F, 0F), rotateAngle)
        transformableNode.worldRotation = quaternion1

        transformableNode.parent = anchorNode
        transformableNode.select()
        transformableNode.renderable = modelRender.arrowRender
        arFragment.arSceneView.scene.addChild(anchorNode)

        node.parent = transformableNode
        node.renderable = modelRender.checkPointRender

        node.localPosition = Vector3(0F,1F,((currentDistance / 30) * -1).toFloat())

    }

    override fun onResume() {
        super.onResume()

        arSceneView.session?.apply {
            session = arSceneView.session!!
            val config = session.config
            config.planeFindingMode = Config.PlaneFindingMode.HORIZONTAL
            config.lightEstimationMode = Config.LightEstimationMode.DISABLED
            arSceneView.session!!.configure(config)
        }



        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME)
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        locationManager.removeUpdates(locationListener)
        mSensorManager.unregisterListener(this, mAccelerometer)
        mSensorManager.unregisterListener(this, mMagnetometer)
    }

    override fun onDestroy() {
        super.onDestroy()
        session.close()
    }

    init{
        instance = this
    }

    companion object {
        //GPS 변수
        private val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 10

        private var instance:ARActivity? = null
        fun getInstance(): ARActivity? {
            return instance
        }
    }

    override fun onMapReady(naverMap: NaverMap) {


        val marker = Marker()
        marker.position = LatLng(gpsNodePointArrayList[0][0], gpsNodePointArrayList[0][1])
        marker.position = LatLng(gpsNodePointArrayList[1][0], gpsNodePointArrayList[1][1])
        marker.map = naverMap

        val path = PathOverlay()
        path.coords = listOf(
            LatLng(gpsNodePointArrayList[0][0], gpsNodePointArrayList[0][1]),
            LatLng(gpsNodePointArrayList[1][0], gpsNodePointArrayList[1][1]),
            LatLng(gpsNodePointArrayList[2][0], gpsNodePointArrayList[2][1]),
            LatLng(gpsNodePointArrayList[3][0], gpsNodePointArrayList[3][1])
        )
        path.map = naverMap



    }
}




