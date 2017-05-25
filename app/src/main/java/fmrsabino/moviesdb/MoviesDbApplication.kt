package fmrsabino.moviesdb

import android.app.Application
import android.content.Context

import com.jakewharton.threetenabp.AndroidThreeTen

import fmrsabino.moviesdb.injection.component.ApplicationComponent
import fmrsabino.moviesdb.injection.component.DaggerApplicationComponent
import fmrsabino.moviesdb.injection.module.ApplicationModule
import timber.log.Timber

class MoviesDbApplication : Application() {
    val component: ApplicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    companion object {
        operator fun get(context: Context): MoviesDbApplication {
            return context.applicationContext as MoviesDbApplication
        }
    }
}
