package com.petsearch.petsearchtask

import android.app.Application
import android.support.multidex.MultiDexApplication
import com.petsearch.petsearchtask.networking.Networking

class PetSearchtaskApplication: MultiDexApplication() {


    override fun onCreate() {
        super.onCreate()
        Networking.initialize(BuildConfig.BASE_URL)
    }


}