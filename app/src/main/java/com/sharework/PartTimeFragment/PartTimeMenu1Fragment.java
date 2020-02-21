package com.sharework.PartTimeFragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.sharework.R;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapView;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


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
    public PartTimeMenu1Fragment() {
        // Required empty public constructor
    }


    public static PartTimeMenu1Fragment newInstance() {
        PartTimeMenu1Fragment fragment = new PartTimeMenu1Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_part_time_menu1, container, false);

        recyclerView = view.findViewById(R.id.fragment_pt_1_content_list);

        ArrayList<String> list = new ArrayList<>();
        for (int i=0; i<100; i++) {
            list.add(String.format("TEXT %d", i)) ;
        }

        listViewAdapter = new PtMainListViewAdapter(list);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(listViewAdapter);


        tmapLayout = (LinearLayout) view.findViewById(R.id.tmap_view);
        tMapView = new TMapView(getContext());

        tMapView.setSKTMapApiKey("l7xxb91f48be6d3842a99b70e58b6c67f5ed");
        tmapLayout.addView(tMapView);
        tMapData = new TMapData();

        mTrackingBtn = view.findViewById(R.id.pt_menu1_btn_tracking_mode);
        mTrackingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tMapView.setTrackingMode(true);
            }
        });


        mSearchBtn = view.findViewById(R.id.pt_menu1_btn_search);
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
