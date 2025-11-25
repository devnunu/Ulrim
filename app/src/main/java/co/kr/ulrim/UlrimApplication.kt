package co.kr.ulrim

import android.app.Application
import co.kr.ulrim.domain.QuotesSyncManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class UlrimApplication : Application() {

    @Inject
    lateinit var quotesSyncManager: QuotesSyncManager

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        // Sync default quotes on app start
        applicationScope.launch {
            quotesSyncManager.syncDefaultQuotes()
        }
    }
}
