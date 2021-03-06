package com.example.android.hue.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.android.hue.database.light.Light
import com.example.android.hue.database.user.User
import com.example.android.hue.network.*
import kotlinx.coroutines.*
import retrofit2.await
import timber.log.Timber

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    //User value from the shared view model containing the initial default user value
    var user = MutableLiveData<User>()

    //Light list value containing the default light list value obtained from the shared view model
    var lightList = MutableLiveData<List<Light>>()

    //View model's background job
    private var viewModelJob = Job()

    //Defines view model's scope for new coroutines
    private val coroutineScope =
        CoroutineScope(viewModelJob + Dispatchers.Main )

    fun testOnState(lightNumber: Int) {
        var stateVal : Boolean
        var lightState : OnStateProperty
        coroutineScope.launch {
            val onTestCall = HueApi.retrofitService.getOnStateAsync(
                user.value!!.username,
                lightNumber)

            Timber.d("Making GET call to test on state for light %s", lightNumber)

            lightState = onTestCall.await()

            Timber.d("Return from call: %s", lightState.state!!.on.toString())

            stateVal = lightState.state!!.on

            if(stateVal){
                switchLightOff(lightNumber)
            }else{
                switchLightOn(lightNumber)
            }
        }
    }

    private fun switchLightOn(lightNumber: Int) {
        coroutineScope.launch {
            val switchCall = HueApi.retrofitService.putOnAttribute(
                user.value!!.username,
                lightNumber,
                LightDataProperty(true))

            Timber.d("Making Light switch on call for light %s", lightNumber)

            switchCall.await()

            Timber.d("Light switch ON call made")
        }
    }

    private fun switchLightOff(lightNumber: Int) {
        coroutineScope.launch {
            val switchCall = HueApi.retrofitService.putOffAttribute(
                user.value!!.username,
                lightNumber,
                LightDataProperty(false))

            Timber.d("Making Light switch OFF call for light %s", lightNumber)

            switchCall.await()

            Timber.d("Light switch OFF call made")
        }
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
        viewModelJob.cancel()
    }
}

interface LightSwitchListener {
    val username : String

    var idNumber : Int

    fun onButtonClick()
}