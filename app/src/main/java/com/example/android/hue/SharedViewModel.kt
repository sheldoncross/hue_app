package com.example.android.hue

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.hue.database.light.Light
import com.example.android.hue.database.user.User

//Shared view model to pass values between fragments and activities
class SharedViewModel : ViewModel() {
    var user = MutableLiveData<User>()

    var lightList = MutableLiveData<List<Light>>()
}