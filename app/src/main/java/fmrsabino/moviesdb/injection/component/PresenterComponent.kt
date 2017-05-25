package fmrsabino.moviesdb.injection.component

import dagger.Component
import fmrsabino.moviesdb.data.DataManager
import fmrsabino.moviesdb.injection.module.ApplicationModule
import fmrsabino.moviesdb.injection.scope.ForView
import fmrsabino.moviesdb.ui.detail.DetailPresenter
import fmrsabino.moviesdb.ui.search.SearchPresenter

@ForView
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(ApplicationModule::class))
interface PresenterComponent {
    fun inject(mainPresenter: SearchPresenter)
    fun inject(detailPresenter: DetailPresenter)

    fun dataManager(): DataManager
}
