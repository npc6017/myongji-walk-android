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
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.myoungji_walk_android.databinding.FragmentArBinding
import com.google.ar.core.*
import com.google.ar.sceneform.*
import com.google.ar.sceneform.Camera
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PathOverlay
import kotlinx.coroutines.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutionException
import kotlin.math.abs


class ARActivity : AppCompatActivity(), SensorEventListener, OnMapReadyCallback {

    //센서 변수
    lateinit var mSensorManager: SensorManager
    lateinit var mAccelerometer: Sensor
    lateinit var mMagnetometer: Sensor
    lateinit var mLocation: Location
    lateinit var locationManager: LocationManager
    private var azimuthinDegress = 0f  // y축 각도
    private val noAccel = floatArrayOf(0f, 3f, 3f)
    private val mLastMagnetometer = FloatArray(3)
    private var mLastAccelerometerSet = false
    private var mLastMagnetometerSet = false
    private val mR = FloatArray(9)
    private val mOrientation = FloatArray(3)

    //AR 변수
    private var checkPointRender: ModelRenderable? = null
    lateinit var arFragment: ArFragment
    lateinit var arSceneView: ArSceneView
    private lateinit var camera: Camera
    private val node = Node()
    lateinit var targetLocation: Location
    lateinit var lastLocation: Location
    private var angle = 0F // 북위각도
    lateinit var session: Session
    private lateinit var anchorNode: AnchorNode

    private var count = 0 // count 변수
    private lateinit var binding: FragmentArBinding
    private lateinit var locationModel: LocationModel

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

            val longitude = location.longitude
            val latitude = location.latitude

            Log.d("Location", "Latitude : $latitude, Longitude : $longitude")
        }

        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_ar)
        locationModel = LocationModel()
        //tast checkpoint. 추후 서버에서 받아올 수 있도록
        gpsNodePointArrayList.addAll(listOf(*gpsNodePoint))


        //센서 초기화
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        mLocation = locationManager
            .getLastKnownLocation(LocationManager.GPS_PROVIDER)!!

        checkPermission()
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            1000,  // interval.
            1f,  // 10 meters
            locationListener
        )


        //체크포인트 AR 이미지 초기화
        val goal = ModelRenderable.builder()
            .setSource(this, Uri.parse("check_point.glb"))
            .setIsFilamentGltf(true)
            .setAsyncLoadEnabled(true)
            .build()
        CompletableFuture.allOf(goal)
            .handle<Any?> { notUsed: Void?, throwable: Throwable? ->
                if (throwable != null) {
                    return@handle null
                }
                try {
                    checkPointRender = goal.get()
                } catch (e: InterruptedException) {
                    e.getStackTrace()
                } catch (e: ExecutionException) {
                    e.getStackTrace()
                }
                null
            }


        //AR 화면 실행
        arFragment = supportFragmentManager.findFragmentById(R.id.arFragment) as ArFragment
        arSceneView = arFragment.arSceneView


        //ar 조명효과 비활성화
        session = Session(this)
        val config = session.config
        config.lightEstimationMode = Config.LightEstimationMode.DISABLED
        //수평면만 감지
        config.planeFindingMode = Config.PlaneFindingMode.HORIZONTAL
        session.configure(config)

        camera = arSceneView.scene.camera

        CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                distanceCal()
                checkPointCal()
                delay(1000)
            }

        }

        //naverMap
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_fragment, it).commit()
            }

        mapFragment.getMapAsync(this)

        arFragment.arSceneView.scene.addOnUpdateListener {
            Log.d("addOnUpdateListenerCall", "call")
            checkPoint()
            creatAncher()
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
            //azimuth 가 너무 갑자기 변하면
            azimuthinDegress = ((Math.toDegrees(
                SensorManager.getOrientation(mR, mOrientation)[0]
                    .toDouble()
            ) + 360).toInt() % 360).toFloat()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    var currentDistance: Double = 0.0
    var lastDistance: Double = 0.0

    //target 위치에 모델 세우기
    private fun distanceCal() {
        targetLocation =
            locationModel.coordToLocation(gpsNodePointArrayList[0][0], gpsNodePointArrayList[0][1])
        lastLocation = locationModel.coordToLocation(
            gpsNodePointArrayList[gpsNodePointArrayList.size - 1][0],
            gpsNodePointArrayList[gpsNodePointArrayList.size - 1][1]
        )

        //target 위치와 현재 위치 간 각도 및 거리 계산
        angle = locationModel.gpsToDegree(mLocation, targetLocation).toFloat()
        currentDistance = locationModel.getDistance(
            mLocation, locationModel.coordToLocation(
                gpsNodePointArrayList[0][0],
                gpsNodePointArrayList[0][1]
            )
        )
        lastDistance = locationModel.getDistance(mLocation, lastLocation)
        binding.checkPoint.text = "체크포인트 : " + gpsNodePointArrayList.size + " 개"
        binding.target.text = "남은 거리 : " + (Math.round(lastDistance * 100) / 100.0) + " m"
        binding.now.text =
            "현재좌표 : " + (mLocation.latitude.toString() + "," + mLocation.longitude.toString())
    }

    private fun checkPoint() {
        //체크포인트 표시
        if (abs(angle - azimuthinDegress) <= 10 && currentDistance <= 5) {
            node.parent = arSceneView.scene
            node.renderable = checkPointRender
            val ray = camera.screenPointToRay(1000 / 2f, 500f)
            val newPosition = ray.getPoint((currentDistance / 5).toFloat())
            node.localPosition = newPosition
        } else {
            node.renderable = null
        }
    }

    private fun checkPointCal() {
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
            finish()
            Log.e("Error is detected : ", e.message!!)
            Toast.makeText(this, e.message!!, Toast.LENGTH_SHORT).show()
            Toast.makeText(this, "경로 탐색 종료", Toast.LENGTH_SHORT).show()
        }
    }

    private fun creatAncher() {
        //탐지된 평면 데이터 가져오기
        val planes = arFragment.arSceneView.arFrame!!.getUpdatedTrackables(
            Plane::class.java
        )

        val planePose: Pose
        val tmpPose: Pose

        for (plane: Plane in planes) {
            if (plane.trackingState == TrackingState.TRACKING) {
                //센서를 통해서 평면 위치 계산후 방위각 계산
                planePose = plane.centerPose
                //내 위치를 화살표가 잘보이는 위치로 변경
                val dPose = arFragment.arSceneView.arFrame!!.camera.displayOrientedPose
                //평면의 Pose와 화면 계산
                val tmpVec = floatArrayOf(dPose.tx(), planePose.ty(), dPose.tz())
                tmpPose = Pose.makeTranslation(tmpVec)

                //평면에 내가 바라보는 방향으로 Anchor 생성
                val anchor = plane.createAnchor(tmpPose)
                makeArrow(anchor, angle)
                return
            }
        }

    }
    //끝

    //이미지 만들기 - 방향 표시 화살표
    private fun makeArrow(anchor: Anchor, angle: Float) {
        if (this::anchorNode.isInitialized && anchorNode.anchor != null) {
            anchorNode.anchor!!.detach()
            Log.d("detach", "detach")
        }

        anchorNode = AnchorNode(anchor)
        ModelRenderable.builder()
            .setSource(this@ARActivity, Uri.parse("arrow.glb"))
            .setIsFilamentGltf(true)
            .setAsyncLoadEnabled(true)
            .build()
            .thenAccept { modelRenderable: ModelRenderable ->
                addToModelScene(
                    anchor,
                    anchorNode,
                    modelRenderable,
                    angle
                )
            }
            .exceptionally { throwable: Throwable ->
                val builder: AlertDialog.Builder =
                    AlertDialog.Builder(this@ARActivity)
                builder.setMessage(throwable.message)
                    .show()
                null
            }
    }

    //찾은 앵커에 3D 모델을 만듦
    private fun addToModelScene(
        anchor: Anchor,
        anchorNode: AnchorNode,
        modelRenderable: ModelRenderable,
        angle: Float
    ) {
        val transformableNode = TransformableNode(arFragment.transformationSystem)
        val rotateAngle = (-azimuthinDegress + angle) % 360

        val quaternion1 = Quaternion.axisAngle(Vector3(0F, -1F, 0F), rotateAngle)
        transformableNode.worldRotation = quaternion1
        transformableNode.parent = anchorNode
        transformableNode.select()
        transformableNode.renderable = modelRenderable
        arFragment.arSceneView.scene.addChild(anchorNode)
    }

    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME)
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        locationManager.removeUpdates(locationListener)
        mSensorManager.unregisterListener(this, mAccelerometer)
        mSensorManager.unregisterListener(this, mMagnetometer)
    }

    companion object {
        //GPS 변수
        private val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 10
    }

    override fun onMapReady(naverMap: NaverMap) {
        val marker = Marker()
        marker.position = LatLng(gpsNodePointArrayList[0][0], gpsNodePointArrayList[0][1])
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




