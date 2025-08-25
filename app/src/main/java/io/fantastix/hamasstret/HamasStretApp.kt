package io.fantastix.hamasstret

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.HiltAndroidApp
import io.fantastix.hamasstret.utils.DataStorePreferences

//@HiltAndroidApp
class HamasStretApp : Application() {
    companion object {
        lateinit var dataStorePreferences: DataStorePreferences
            private set
    }

    override fun onCreate() {
        super.onCreate()
        dataStorePreferences = DataStorePreferences(applicationContext)

        // Keep it simple for now stupid
    }
}