package com.example.chicoutexplore
import android.app.Application
import org.osmdroid.config.Configuration
import java.io.File

class CacheMap : Application() {
    override fun onCreate() {
        super.onCreate()
        // Configure le répertoire de cache
        val osmdroidBasePath = File(cacheDir, "osmdroid")
        Configuration.getInstance().osmdroidBasePath = osmdroidBasePath
    }
}


