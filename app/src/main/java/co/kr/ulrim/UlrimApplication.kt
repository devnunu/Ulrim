package co.kr.ulrim

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class UlrimApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Default quotes will be synced when user navigates to BrowseQuotesScreen
    }
}
