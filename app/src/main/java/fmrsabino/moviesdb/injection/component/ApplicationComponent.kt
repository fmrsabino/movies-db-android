package fmrsabino.moviesdb.injection.component

import android.app.Application
import android.content.Context
import com.squareup.picasso.Picasso
import dagger.Component
import fmrsabino.moviesdb.data.DataManager
import fmrsabino.moviesdb.injection.module.ApplicationModule
import fmrsabino.moviesdb.injection.scope.ApplicationContext
import fmrsabino.moviesdb.ui.explore.ExplorePresenter
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {
    fun application(): Application
    @ApplicationContext fun context(): Context
    fun dataManager(): DataManager
    fun picasso(): Picasso

    fun inject(explorePresenter: ExplorePresenter)
}
