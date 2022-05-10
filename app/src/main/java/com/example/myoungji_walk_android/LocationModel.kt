package com.example.myoungji_walk_android

import android.location.Location
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

class LocationModel {

    //위도 경도에 따른 Location 값으로 변환
    fun coordToLocation(lat: Double, lng: Double): Location {
        val location = Location("nextLoc")
        location.latitude = lat
        location.longitude = lng
        return location
    }

    //내 위치에서 원하는 위치로 갈 때 회전각 계산
     fun getAngle(myLocation: Location, targetLocation: Location): Double {

        //내 위치 위도 경도를 라디안으로 변환
        val myLat = myLocation.latitude * (3.141592 / 180)
        val myLng = myLocation.longitude * (3.141592 / 180)

        //타겟 위치 위도 경도를 라디안으로 변환
        val targetLat = targetLocation.latitude * (3.141592 / 180)
        val targetLng = targetLocation.longitude * (3.141592 / 180)

        //라디안 거리 계산
        val radianDistance = acos(
            sin(myLat) * sin(targetLat)
                    + cos(myLat) * cos(targetLat) * cos(myLng - targetLng)
        )

        //라디안 거리 => 라디안 각 => 각도 변환
        val radianBearing = acos(
            (sin(targetLat) - sin(myLat) * cos(radianDistance)) / (0.00001 + (cos(
                myLat
            ) * sin(radianDistance)))
        )
        var trueBearing = radianBearing * (180 / 3.141592)
        if (sin(targetLng - myLng) < 0) {
            trueBearing = 360 - trueBearing
        }
        return trueBearing
    }

    //두 위도 경도 주어지면 거리로 변환
     fun getDistance(myLocation: Location, targetLocation: Location): Double {
        //위도 추출
        val lat1 = myLocation.latitude
        val lat2 = targetLocation.latitude

        //경도 추출
        val lng1 = myLocation.longitude
        val lng2 = targetLocation.longitude
        val distance: Double
        val locationA = Location("point A")
        locationA.latitude = lat1
        locationA.longitude = lng1
        val locationB = Location("point B")
        locationB.latitude = lat2
        locationB.longitude = lng2
        distance = locationA.distanceTo(locationB).toDouble()
        return distance
    }


}