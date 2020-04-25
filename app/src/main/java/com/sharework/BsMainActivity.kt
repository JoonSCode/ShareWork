package com.sharework

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.sharework.BossFragment.AddBusinessActivity
import com.sharework.BossFragment.PagerAdapter
import com.sharework.Data.Users
import com.sharework.Function.Server
import org.jetbrains.anko.startActivity

class BsMainActivity : AppCompatActivity() {

    private lateinit var mName: TextView
    private lateinit var mPos: TextView
    private lateinit var mNum: TextView
    private lateinit var mChangeBusiness : ImageButton
    private lateinit var mAddPicture : ImageButton
    private lateinit var mAddBusiness : Button
    private lateinit var mAddJob : Button
    private lateinit var server : Server
    private lateinit var user : Users

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bs_main)

        initView()
        initFun()

        val pagerAdapter = PagerAdapter(supportFragmentManager)
        val pager = findViewById<ViewPager>(R.id.activity_bs_main_container)
        pager.adapter = pagerAdapter
        val tab = findViewById<TabLayout>(R.id.activity_bs_main_tab)
        tab.setupWithViewPager(pager)

    }


    private fun initFun() {
        mName.setOnClickListener{
            Log.d("BossActivity", "클리익")
        }
        mAddBusiness.setOnClickListener{
            Log.d("BossActivity", "영업장 추가버튼 클릭")
            startActivity<AddBusinessActivity>()
        }
        mAddJob.setOnClickListener{
            Log.d("BossActivity", "공고 추가버튼 클릭")
        }
        mAddPicture.setOnClickListener{
            Log.d("BossActivity", "사진 추가버튼 클릭")
        }
        Log.d("BossActivity", "기능 init")
    }

    private fun initView(){
        Log.d("BossActivity", "initView")
        val appBar : AppBarLayout = findViewById(R.id.activity_bs_main_appbar)
        if (appBar.layoutParams != null) {
            val layoutParams = appBar.layoutParams as CoordinatorLayout.LayoutParams
            val appBarLayoutBehaviour = AppBarLayout.Behavior()
            appBarLayoutBehaviour.setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
                override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                    return false
                }
            })
            layoutParams.behavior = appBarLayoutBehaviour
        }
        mName = findViewById(R.id.activity_bs_main_tv_business_name)
        mPos = findViewById(R.id.activity_bs_main_tv_business_pos)
        mNum = findViewById(R.id.activity_bs_main_tv_business_num)
        mChangeBusiness = findViewById(R.id.activity_bs_main_ibtn_change_business)
        mAddPicture = findViewById(R.id.activity_bs_main_ibtn_business_pic)
        mAddBusiness = findViewById(R.id.activity_bs_main_btn_add_business)
        mAddJob = findViewById(R.id.activity_bs_main_btn_add_job)
        server = Server()
        user = server.users

    }
}



