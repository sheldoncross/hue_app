package com.example.android.hue.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*

//Url for the api
private const val BASE_URL = "https:/192.168.4.48/"

//Certificate handler for unsafe connections
private val unsafeOkHttpClient = UnsafeOkHttpClient.unsafeOkHttpClient

//Moshi is a modern JSON library. It makes it easy to parse JSON into Java objects:
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

//Retrofit turns HTTP API into a Java interface.
private val retrofit = Retrofit.Builder()
    //A Converter to text/plain bodies.
    .addConverterFactory(ScalarsConverterFactory.create())
    //A Converter for parsing JSON into Java
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .client(unsafeOkHttpClient)
    .build()

//Interface containing the methods for data retrieval that the API object will inherit
interface HueApiService {
    //POST call to obtain username
    @POST("api")
    fun postDeviceTypeAsync(@Body post: String) : Deferred<List<BridgeProperty>>

    //GET call to obtain the light map
    @GET("api/{username}/lights")
    fun getAllLightsAsync(@Path("username") username : String) :
            Deferred<Map<String, LightProperty>>

    //GET call to test on state and determine if switch should turn light on/off
    @GET("api/{username}/lights/{light}")
    fun getOnStateAsync(@Path("username") username: String,
                        @Path("light") light: Int) : Deferred<OnStateProperty>


    //PUT call to insert the value of the "on" attribute into any light
    @PUT("api/{username}/lights/{light}/state")
    fun putOnAttribute(@Path("username") username: String,
                       @Path("light") light: Int,
                       @Body body: LightDataProperty) : Call<String>

    //PUT call to insert the value of the "on" attribute into any light
    @PUT("api/{username}/lights/{light}/state")
    fun putOffAttribute(@Path("username") username: String,
                       @Path("light") light: Int,
                       @Body body: LightDataProperty) : Call<String>

}

//API object inheriting the ApiService
object HueApi {
    var username = ""

    val retrofitService : HueApiService by lazy {
        //Create intended API service
        retrofit.create(HueApiService::class.java)
    }
}