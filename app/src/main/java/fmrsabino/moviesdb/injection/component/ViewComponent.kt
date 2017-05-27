package fmrsabino.moviesdb.injection.component

import dagger.Component
import fmrsabino.moviesdb.data.DataManager
import fmrsabino.moviesdb.injection.scope.ForView
import fmrsabino.moviesdb.ui.explore.ExploreActivity

@ForView
@Component(dependencies = arrayOf(ApplicationComponent::class))
interface ViewComponent {
    fun inject(activity: ExploreActivity)

    fun dataManager(): DataManager
}