package com.example.rakesh.api

import com.example.rakesh.model.DataResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("launches")
    suspend fun getRecord(): Response<DataResponse>
}