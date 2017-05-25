package fmrsabino.moviesdb.injection.component

import dagger.Component
import fmrsabino.moviesdb.data.DataManager
import fmrsabino.moviesdb.injection.module.ViewModule
import fmrsabino.moviesdb.injection.scope.ForView
import fmrsabino.moviesdb.ui.explore.ExploreActivity
import fmrsabino.moviesdb.ui.explore.ExploreContract

@ForView
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(ViewModule::class))
interface ViewComponent {
    fun inject(activity: ExploreActivity)
    fun explorePresenter(): ExploreContract.Presenter

    fun dataManager(): DataManager
}