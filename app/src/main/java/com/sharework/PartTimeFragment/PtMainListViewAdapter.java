package com.sharework.PartTimeFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sharework.R;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class PtMainListViewAdapter extends RecyclerView.Adapter<PtMainListViewAdapter.ViewHolder> {

    //private ArrayList<dataclass> itemList = new ArrayList();  dataclass와 아이템 정보 클래스 맞추어야함
    private ArrayList<String> itemList = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView1 ;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            //textView1 = itemView.findViewById(R.id.text1) ;
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    PtMainListViewAdapter(ArrayList<String> list) {
        itemList = list ;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public PtMainListViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.main_content_item, parent, false) ;
        PtMainListViewAdapter.ViewHolder vh = new PtMainListViewAdapter.ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(PtMainListViewAdapter.ViewHolder holder, int position) {
        //String text = itemList.get(position) ;
        //holder.textView1.setText(text) ;
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return itemList.size() ;
    }


}
