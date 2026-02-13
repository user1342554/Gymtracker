package com.example.gymtracker

import android.app.Application
import com.example.gymtracker.data.GymDatabase

class GymTrackerApp : Application() {

    val database: GymDatabase by lazy {
        GymDatabase.getInstance(this)
    }
}
