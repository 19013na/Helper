package com.example.projectapplication

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface NetworkService {
    //"http://openapi.seoul.go.kr:8088/6f6669585870686f3831586c724e6b/json/SeoulMetroFaciInfo/1/1000/?"
    @GET("{api_key}/{type}/SeoulMetroFaciInfo/1/{end_num}")
    fun getList(
        @Path("api_key") apikey:String,
        @Path("type") type:String,
        @Path("end_num") endn:Int,
        ) : Call<MyModel>
}