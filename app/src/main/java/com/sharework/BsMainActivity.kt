package com.sharework

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.sharework.BossFragment.PagerAdapter
import com.sharework.Data.Users
import com.sharework.Function.Server

class BsMainActivity : AppCompatActivity() {

    private lateinit var mName: TextView
    private lateinit var mPos: TextView
    private lateinit var mNum: TextView
    private lateinit var mChangeBusiness : ImageButton
    private lateinit var mAddPicture : ImageButton
    private lateinit var mAddBusiness : Button
    private lateinit var mAddJob : Button
    private lateinit var server : Server
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bs_main)

        val pagerAdapter = PagerAdapter(supportFragmentManager)
        val pager = findViewById<ViewPager>(R.id.activity_bs_main_container)
        pager.adapter = pagerAdapter
        val tab = findViewById<TabLayout>(R.id.activity_bs_main_tab)
        tab.setupWithViewPager(pager)

        initView()
        initFun()
        val user : Users = server.users


    }

    private fun initFun() {
        mAddBusiness.setOnClickListener(View.OnClickListener {

        })
    }

    private fun initView(){
        mName = findViewById(R.id.activity_bs_main_tv_business_name)
        mPos = findViewById(R.id.activity_bs_main_tv_business_pos)
        mNum = findViewById(R.id.activity_bs_main_tv_business_num)
        mChangeBusiness = findViewById(R.id.activity_bs_main_ibtn_change_business)
        mAddPicture = findViewById(R.id.activity_bs_main_ibtn_business_pic)
        mAddBusiness = findViewById(R.id.activity_bs_main_btn_add_business)
        mAddJob = findViewById(R.id.activity_bs_main_btn_add_job)
        server = Server()
    }
}



