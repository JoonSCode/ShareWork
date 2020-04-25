package com.sharework.BossFragment

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.Behavior.DragCallback
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.sharework.R
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView


class AddBusinessActivity : AppCompatActivity(), MapView.CurrentLocationEventListener {

    private lateinit var mapView : MapView
    private lateinit var mapViewContainer : ViewGroup
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_business)

        val appBar : AppBarLayout = findViewById(R.id.activity_add_business_appbar)
        if (appBar.layoutParams != null) {
            val layoutParams = appBar.layoutParams as CoordinatorLayout.LayoutParams
            val appBarLayoutBehaviour = AppBarLayout.Behavior()
            appBarLayoutBehaviour.setDragCallback(object : DragCallback() {
                override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                    return false
                }
            })
            layoutParams.behavior = appBarLayoutBehaviour
        }

        mapView = MapView(this)
        mapViewContainer = findViewById(R.id.activity_add_business_map_view)
        mapViewContainer.addView(mapView)
        checkPermissions()
        mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
        mapView.setCurrentLocationEventListener(this)

    }
    //권한관련 함수------------------------------------------------------
    private var permissionListener: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
            //initView()
            // 권한 승인이 필요없을 때 실행할 함수
        }

        override fun onPermissionDenied(deniedPermissions: List<String>) {
            Toast.makeText(applicationContext, "권한 허용을 하지 않으면 서비스를 이용할 수 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) { // 마시멜로(안드로이드 6.0) 이상 권한 체크
            TedPermission.with(applicationContext)
                    .setPermissionListener(permissionListener)
                    .setRationaleMessage("위치 설정을 위해서 접근 권한이 필요합니다")
                    .setDeniedMessage("앱에서 요구하는 권한설정이 필요합니다...\n [설정] > [권한] 에서 사용으로 활성화해주세요.")
                    .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                    .check()
        }
    }

    override fun onCurrentLocationUpdateFailed(p0: MapView?) {
        Log.d("AddBusiness", "트래킹 실패")

    }

    override fun onCurrentLocationUpdate(p0: MapView?, mapPoint: MapPoint?, p2: Float) {
        Log.d("AddBusiness", "트래킹됨")
        val mapPointGeo = mapPoint!!.mapPointGeoCoord
        val currentMapPoint = MapPoint.mapPointWithGeoCoord(mapPointGeo.latitude, mapPointGeo.longitude);
        //이 좌표로 지도 중심 이동
        mapView.setMapCenterPoint(currentMapPoint, true);
        mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
        Log.d("AddBusiness", "트래킹 종료")
    }

    override fun onCurrentLocationUpdateCancelled(p0: MapView?) {
        Log.d("AddBusiness", "트래킹 취소")

    }

    override fun onCurrentLocationDeviceHeadingUpdate(p0: MapView?, p1: Float) {
        Log.d("AddBusiness", "트래킹 방향 업뎃")

    }
}
