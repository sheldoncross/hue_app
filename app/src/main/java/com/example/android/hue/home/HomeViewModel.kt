package com.example.android.hue.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.android.hue.database.light.Light
import com.example.android.hue.database.user.User

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    var user = MutableLiveData<User>()

    var lightList = MutableLiveData<List<Light>>()
}