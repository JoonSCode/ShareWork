package com.sharework.PartTimeFragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.appbar.AppBarLayout;
import com.google.maps.android.clustering.ClusterManager;
import com.sharework.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class PartTimeMenu1Fragment extends Fragment implements OnMapReadyCallback {

    private RecyclerView recyclerView;
    private PtMainListViewAdapter listViewAdapter;
    private LinearLayout tmapLayout;
    private ImageButton mTrackingBtn;
    private ImageButton mSearchBtn;
    private EditText mSearchEt;
    private GoogleMap mMap;
    ClusterManager mClusterManager;


    public PartTimeMenu1Fragment() { }


    public static PartTimeMenu1Fragment newInstance() {
        PartTimeMenu1Fragment fragment = new PartTimeMenu1Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<String> list = new ArrayList<>();
        for (int i=0; i<100; i++) {
            list.add(String.format("TEXT %d", i)) ;
        }
        listViewAdapter = new PtMainListViewAdapter(list);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1); //위치권한 탐색 허용 관련 내용
            }
            return;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("프래그먼트", "1만들어짐");
        View view = inflater.inflate(R.layout.fragment_part_time_menu1, container, false);

        AppBarLayout appBar = (AppBarLayout) view.findViewById(R.id.appbar);
        if (appBar.getLayoutParams() != null) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) appBar.getLayoutParams();
            AppBarLayout.Behavior appBarLayoutBehaviour = new AppBarLayout.Behavior();
            appBarLayoutBehaviour.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
                @Override
                public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                    return false;
                }
            });
            layoutParams.setBehavior(appBarLayoutBehaviour);
        }

        recyclerView = view.findViewById(R.id.fragment_pt_1_content_list);//리스트생성
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(listViewAdapter);

        mSearchBtn = view.findViewById(R.id.pt_menu1_btn_search);//검색기능
        mSearchEt = view.findViewById(R.id.pt_menu1_et_search);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);
        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.514,127.102716),16.0f));

//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(SEOUL);
//        markerOptions.title("서울");
//        markerOptions.snippet("수도");
//        googleMap.addMarker(markerOptions);
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
//        googleMap.animateCamera(CameraUpdateFactory.zoomTo(13));
    }
}
