package com.sharework.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SearchRetrofit {
    fun getService(): SearchService = retrofit.create(SearchService::class.java)

    private val retrofit =
            Retrofit.Builder()
                    .baseUrl("https://dapi.kakao.com") // 도메인 주소
                    .addConverterFactory(GsonConverterFactory.create()) // GSON을 사용하기 위해 ConverterFactory에 GSON 지정
                    .build()
}