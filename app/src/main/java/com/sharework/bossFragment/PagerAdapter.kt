package com.sharework.bossFragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PagerAdapter(fm: FragmentManager) :  FragmentPagerAdapter(fm) {
    private val pageMaxCnt = 3
    private val fragment = arrayOf(BossMenu1Fragment(), BossMenu2Fragment(), BossMenu3Fragment())

    override fun getItem(position: Int): Fragment {
        return fragment[position]
    }
    override fun getCount(): Int {
        return pageMaxCnt
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val title = when(position){
            0 -> "진행중"
            1 -> "마감"
            2 -> "인재관리"
            else -> "etc"
        }
        return title
    }
}