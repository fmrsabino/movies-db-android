package fmrsabino.moviesdb.injection.component

import dagger.Component
import fmrsabino.moviesdb.injection.module.ViewModule
import fmrsabino.moviesdb.injection.scope.ForView
import fmrsabino.moviesdb.ui.detail.DetailActivity
import fmrsabino.moviesdb.ui.explore.ExploreActivity

@ForView
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(ViewModule::class))
interface ViewComponent {
    fun inject(activity: ExploreActivity)
    fun inject(activity: DetailActivity)
}
