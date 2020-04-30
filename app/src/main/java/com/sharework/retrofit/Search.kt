package com.sharework.retrofit

data class Search(
    val meta: SearchMeta,
    val documents: List<SearchData>
)
data class SearchMeta(
    val total_count: Int, // 검색어에 검색된 문서수
    val pageable_count: Int, // total_count 중에 노출가능 문서수
    val is_end: Boolean // 현재 페이지가 마지막 페이지인지 여부
)
data class SearchData(
    val address_name : String,
    val category_group_code : String,
    val category_group_name : String,
    val category_name : String,
    val distance : String,
    val id : String,
    val phone : String,
    val place_name : String,
    val place_url : String,
    val road_address_name : String,
    val x : Double,
    val y : Double
)