package com.sharework.bossFragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.Behavior.DragCallback
import com.sharework.R
import com.sharework.data.Business
import com.sharework.function.Server
import com.sharework.retrofit.SearchData
import com.sharework.retrofit.SearchRetrofit
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onClick


class AddBusinessActivity : AppCompatActivity(), MapView.CurrentLocationEventListener {

    private lateinit var mapView: MapView
    private lateinit var mapViewContainer: ViewGroup
    private lateinit var centerPoint : MapPoint
    private lateinit var data: List<SearchData>
    private lateinit var adapter : AddBusinessListViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_business)
        initView()
    }

    private fun initView() {
        val appBar: AppBarLayout = findViewById(R.id.activity_add_business_appbar)
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
        val backBtn : ImageButton = findViewById(R.id.activity_add_business_ibtn_back)
        backBtn.onClick {
            onBackPressed()
        }

        val et : EditText = findViewById(R.id.add_business_et_bs)
        val btn = findViewById<Button>(R.id.add_business_btn_search)
        btn.onClick {
            Log.d("retrofit", "onclick")
            val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(et.windowToken, 0);
            search(et.text.toString())
        }
        mapView = MapView(this)
        mapViewContainer = findViewById(R.id.activity_add_business_map_view)
        mapViewContainer.addView(mapView)
        mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
        mapView.setCurrentLocationEventListener(this)
        val recyclerView = findViewById<RecyclerView>(R.id.activity_add_business_content_list)
        adapter = AddBusinessListViewAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = false;
    }


    private fun search(str: String){
        doAsync {
            mapView.removeAllPOIItems()
            centerPoint = mapView.mapCenterPoint
            val response = SearchRetrofit.getService().requestSearchPlace(keyword = str, page = 1, longitude = centerPoint.mapPointGeoCoord.longitude,
                    latitude = centerPoint.mapPointGeoCoord.latitude, rad = 20000).execute()
            if (response.isSuccessful) {
                data = response.body()!!.documents
                runOnUiThread {
                    adapter.clear()
                    adapter.setData(ArrayList(data))
                    Log.d("retrofit", "recycler 갱신")
                }
                Log.d("retrofit", data.toString())
                var i = 1;
                var tmp = data[0]
                mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(tmp.y,tmp.x), true)
                mapView.setZoomLevel(5,true)
                for (a in data) {
                    val mapPoi = MapPOIItem()
                    mapPoi.itemName = a.place_name
                    mapPoi.tag = i
                    mapPoi.mapPoint = MapPoint.mapPointWithGeoCoord(a.y, a.x)
                    Log.d("poi", "핀 만듬")
                    mapPoi.markerType = MapPOIItem.MarkerType.BluePin
                    mapPoi.selectedMarkerType = MapPOIItem.MarkerType.RedPin
                    mapView.addPOIItem(mapPoi)
                    i += 1
                }
            } else
                Log.d("retrofit", "실패")
        }

    }

    override fun onCurrentLocationUpdateFailed(p0: MapView?) {
        Log.d("AddBusiness", "트래킹 실패")
    }

    override fun onCurrentLocationUpdate(p0: MapView?, mapPoint: MapPoint?, p2: Float) {
        Log.d("AddBusiness", "트래킹됨")
        val mapPointGeo = mapPoint!!.mapPointGeoCoord
        centerPoint = MapPoint.mapPointWithGeoCoord(mapPointGeo.latitude, mapPointGeo.longitude);
        //이 좌표로 지도 중심 이동
        mapView.setMapCenterPoint(centerPoint, true);
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

