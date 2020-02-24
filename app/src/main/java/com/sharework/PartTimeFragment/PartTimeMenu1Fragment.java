package com.sharework.PartTimeFragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sharework.R;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapView;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;


public class PartTimeMenu1Fragment extends Fragment {

    private RecyclerView recyclerView;
    private PtMainListViewAdapter listViewAdapter;
    private LinearLayout tmapLayout;
    private ImageButton mTrackingBtn;
    private ImageButton mSearchBtn;
    private EditText mSearchEt;
    private TMapData tMapData;
    private TMapView tMapView;
    private SearchPlace searchPlace;


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
        tMapData = new TMapData();

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


        tMapView = new TMapView(getContext());//tmap 띄우기
        tMapView.setSKTMapApiKey("l7xxb91f48be6d3842a99b70e58b6c67f5ed");
        tmapLayout = (LinearLayout) view.findViewById(R.id.tmap_view);
        tmapLayout.addView(tMapView);

        recyclerView = view.findViewById(R.id.fragment_pt_1_content_list);//리스트생성
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(listViewAdapter);

        mTrackingBtn = view.findViewById(R.id.pt_menu1_btn_tracking_mode);//자기위치찾기
        mTrackingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = "사용중";
                if(tMapView.getIsTracking()) {
                    tMapView.setTrackingMode(false);
                    a = "사용안함";
                }
                else {
                    tMapView.setTrackingMode(true);
                    a = "사용중";
                }

                Log.d("트래킹모드", a);
            }
        });


        mSearchBtn = view.findViewById(R.id.pt_menu1_btn_search);//검색기능
        mSearchEt = view.findViewById(R.id.pt_menu1_et_search);
        searchPlace = new SearchPlace();
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPlace.execute();
            }
        });

        return view;
    }

    private class SearchPlace extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer... integers) {
            String search = mSearchEt.getText().toString();
            ArrayList result = new ArrayList();
            try {
                result = tMapData.findAllPOI(search);
                while(result == null){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(!result.isEmpty()){
                    TMapPOIItem item = (TMapPOIItem) result.get(0);
                    Log.d("위치검색", item.noorLon);
                   tMapView.setCenterPoint(Double.parseDouble(item.noorLon),Double.parseDouble(item.noorLat));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
            return null;
        }
    }




}
