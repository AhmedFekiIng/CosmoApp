package com.example.data.repository

import com.example.data.model.Device
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface ApiService {
    @GET("devices")
    suspend fun getDevices(): DeviceResponse

    companion object {
        fun create(baseUrl: String): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}

data class DeviceResponse(val devices: List<Device>)
