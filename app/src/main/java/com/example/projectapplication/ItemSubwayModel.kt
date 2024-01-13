package com.example.projectapplication

data class ItemSubwayModel(
    var STATION_NM: String? = null,
    var FACI_NM: String? = null,
    var LOCATION: String? = null,
    var USE_YN: String? = null,
    var STATION_ID: String? = null,
)

data class MyItems(val row:MutableList<ItemSubwayModel>)
data class MyModel(val SeoulMetroFaciInfo: MyItems)

//data class MyItem(val item:ItemRetrofitModel)
//data class MyItems(val items:MutableList<MyItem>)