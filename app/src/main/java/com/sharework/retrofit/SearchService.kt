package com.sharework.retrofit

import android.provider.Settings.Global.getString
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface SearchService {
    @Headers("Authorization: KakaoAK 0bd148518238a55b720b705412aa1041")
    @GET("/v2/local/search/keyword.json")
    fun requestSearchPlace(
            @Query("query") keyword: String,
            @Query("page") page: Int,
            @Query("x") longitude: Double,
            @Query("y") latitude: Double,
            @Query("radius") rad : Int
    ): Call<Search>
}