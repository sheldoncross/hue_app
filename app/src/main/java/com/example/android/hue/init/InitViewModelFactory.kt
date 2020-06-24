package com.example.android.hue.init

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.hue.database.light.LightDatabaseDao
import com.example.android.hue.database.user.UserDatabaseDao

class InitViewModelFactory (
    private val userDataSource: UserDatabaseDao,
    private val lightDataSource: LightDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InitViewModel::class.java)) {
            return InitViewModel(userDataSource, lightDataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}