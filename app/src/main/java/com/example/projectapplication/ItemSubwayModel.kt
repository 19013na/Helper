package com.example.projectapplication

data class ItemSubwayModel(
    var STATION_NM: String? = null, //역명
    var FACI_NM: String? = null,    // 승강기명
    var LOCATION: String? = null,   // 설치위치
    var USE_YN: String? = null,     // 운행 상태 (가능 or 불가능)
    var STATION_ID: String? = null, // 역코드
){
    // STATION_NM이 특정 키워드를 포함하는지 확인하는 함수 추가
    fun containsKeyword(keyword: String): Boolean {
        return STATION_NM?.contains(keyword, ignoreCase = true) == true
    }
}


data class MyItems(val row:MutableList<ItemSubwayModel>)
data class MyModel(val SeoulMetroFaciInfo: MyItems)

//data class MyItem(val item:ItemRetrofitModel)
//data class MyItems(val items:MutableList<MyItem>)