package com.sharework.bossFragment

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sharework.R
import com.sharework.bossFragment.AddBusinessListViewAdapter.ViewHolder
import com.sharework.data.Business
import com.sharework.function.Server
import com.sharework.retrofit.SearchData
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*
import kotlin.collections.ArrayList

class AddBusinessListViewAdapter internal constructor(context: Context) : RecyclerView.Adapter<ViewHolder>() {
    private var itemList: ArrayList<SearchData> = ArrayList()//dataclass와 아이템 정보 클래스 맞추어야함
    private var server = Server()
    private var context = context
    var itemClick: ItemClick? = null

    inner class ViewHolder internal constructor(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {}

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    fun clear() {
        itemList.clear()
    }

    fun setData(data: ArrayList<SearchData>) {
        itemList = data
        notifyDataSetChanged()
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.business_search_content, parent, false)
        return ViewHolder(view)
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    override fun getItemCount(): Int {
        return itemList.size
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var name = itemList.get(position).place_name
        var rdAddr = itemList.get(position).road_address_name
        var addr = itemList.get(position).address_name
        var num: String? = itemList.get(position).phone
        holder.itemView.findViewById<TextView>(R.id.bs_content_et_name).text = name
        holder.itemView.findViewById<TextView>(R.id.bs_content_et_road_addr).text = rdAddr
        holder.itemView.findViewById<TextView>(R.id.bs_content_et_addr).text = addr
        holder.itemView.findViewById<TextView>(R.id.bs_content_et_num).text = num

        holder?.itemView?.onClick {
            if(num == null)
                num = ""
            var bs = Business("11", server.users.id, name,rdAddr,num, Calendar.getInstance().time)
            server.addBusiness(bs)
            var tmp = context as AddBusinessActivity
            tmp.finish()
        }
    }
}