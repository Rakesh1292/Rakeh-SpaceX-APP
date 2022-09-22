package com.example.rakesh.repository

import com.example.rakesh.api.ApiService
import com.example.rakesh.common.network.NetworkState
import com.example.rakesh.model.DataResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpaceXRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getData(): NetworkState<DataResponse> {
        val response = apiService.getRecord()
        return if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                NetworkState.Success(responseBody)
            } else {
                NetworkState.Error(response)
            }
        } else {
            NetworkState.Error(response)
        }
    }
}