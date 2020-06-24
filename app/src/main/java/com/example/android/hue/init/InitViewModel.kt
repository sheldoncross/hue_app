package com.example.android.hue.init

import android.app.Application
import androidx.lifecycle.*
import com.example.android.hue.database.light.Light
import com.example.android.hue.database.light.LightDatabaseDao
import com.example.android.hue.network.HueApi
import com.example.android.hue.network.LightProperty
import com.example.android.hue.database.user.User
import com.example.android.hue.database.user.UserDatabaseDao
import kotlinx.coroutines.*
import org.json.JSONObject
import timber.log.Timber

class InitViewModel(private val userDatabaseDao: UserDatabaseDao,
                    private val lightDatabaseDao: LightDatabaseDao,
                    application: Application): AndroidViewModel(application) {
    //Private class username string obtained from hue bridge on first initialization
    private var _user = MutableLiveData<User>()

    //Global username string obtained accessible from view model instance
    val user : LiveData<User>
        get() = _user

    /*Private class light map variable containing each light and its properties accessible from a
    light number*/
    private val _lightMap = MutableLiveData<Map<String, LightProperty>>()

    private val _lightList = MutableLiveData<List<Light>>()

    //Global light list string obtained from view model instance
    val lightList : LiveData<List<Light>>
        get() = _lightList

    private val _navigateToHome = MutableLiveData<Boolean>()

    val navigateToHome: LiveData<Boolean>
        get() = _navigateToHome

    //View model's background job
    private var viewModelJob = Job()

    //Defines view model's scope for new coroutines
    private val coroutineScope =
        CoroutineScope(viewModelJob + Dispatchers.Main )

    init {
        coroutineScope.launch {
            //Get default username api key
            _user.value = userDatabaseDao.getDefaultUser()

            //If no username is found begin initial bridge setup
            _lightList.value = lightDatabaseDao.getAllLights()

            //Start initial request for username api key if the default username is empty
            @Suppress("SENSELESS_COMPARISON")
            when {
                _user.value == null -> {
                    postDeviceType()
                }
                _lightList.value!!.isEmpty() -> {
                    //If username is found immediately retrieve all lights using username API key
                    getHueProperties(_user.value!!.username)
                }
                else -> {
                    Timber.d(_user.value.toString())
                    Timber.d(_lightList.value.toString())
                    navigateToHome()
                }
            }
        }
    }

    //Function to obtain the initial username from the hue bridge
    private fun postDeviceType(){
        coroutineScope.launch {
            //An object containing the apps username parameters
            val paramObject = JSONObject()

            paramObject.put("devicetype", "my_hue_app#android stefan")

            try {
                Timber.d("Waiting for initial username from hue bridge")

                /*Continue making calls for a username from the bridge while the username value is
                 invalid */
                var response = "null"

                while (response == "null"){

                    //POST call containing the apps parameter values
                    val postDeviceTypeDeferred =
                        HueApi.retrofitService.postDeviceTypeAsync(paramObject.toString())

                    //Obtain the username from the POST call
                    val listData = postDeviceTypeDeferred.await()

                    response = listData[0].success?.username.toString()
                }
                Timber.d(String.format("Username is: $response"))

                //Create new user API key
                val newUser = User(response)

                _user.value = newUser

                //Insert new username API key into room database
                userDatabaseDao.insert(newUser)
                getHueProperties(_user.value!!.username)
            }catch (e : Exception){
                Timber.d("Failure: %s", e.message)
                postDeviceType()
            }
            Timber.d("Saved username: %s", _user.value)
        }
    }

    //Function to retrieve all lights from a bridge after a username has been obtained
    private fun getHueProperties(username : String) {
        coroutineScope.launch {
            //GET call containing the obtained username string
            val getAllLightsDeferred = HueApi.retrofitService
                .getAllLightsAsync(username)

            try {
                Timber.d("Getting all light data")

                //Obtain the light map from the GET call
                val listResult = getAllLightsDeferred.await()

                Timber.d("Data retrieved")
                _lightMap.value = listResult
                for(lightProperty: LightProperty in listResult.values){
                    lightDatabaseDao.insert(
                        Light(
                            lightProperty.name,
                            lightProperty.state!!.on,
                            lightProperty.state.bri,
                            lightProperty.state.hue,
                            lightProperty.state.sat
                        )
                    )
                }
                Timber.d(String.format("Light objects: " + _lightMap.value.toString()))
                navigateToHome()
            }catch (e : java.lang.Exception){
                Timber.d(String.format("Failure: " + e.message))
            }
        }
    }

    private fun navigateToHome() {
            _navigateToHome.value = true
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
        viewModelJob.cancel()
    }
}