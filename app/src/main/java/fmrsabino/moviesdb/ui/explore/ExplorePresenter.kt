package fmrsabino.moviesdb.ui.explore

import fmrsabino.moviesdb.data.DataManager
import fmrsabino.moviesdb.injection.scope.ForView
import fmrsabino.moviesdb.ui.base.results.Result
import fmrsabino.moviesdb.ui.base.uievents.RefreshEvent
import fmrsabino.moviesdb.ui.base.uievents.UiEvent
import fmrsabino.moviesdb.ui.base.uievents.ViewActiveEvent
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import javax.inject.Inject

@ForView
class ExplorePresenter
@Inject constructor(val dataManager: DataManager) : ExploreContract.Presenter {
    override val transformer: ObservableTransformer<UiEvent, ExploreUiModel> = ObservableTransformer { events ->
        events.publish { shared ->
            Observable.merge(
                    shared.ofType(ViewActiveEvent::class.java).compose(refreshDataTransformer),
                    shared.ofType(RefreshEvent::class.java).compose(refreshDataTransformer)
            ).scan(ExploreUiModel(), { previous, result -> stateReducer(previous, result) })
        }
    }

    val refreshDataTransformer: ObservableTransformer<UiEvent, Result> = ObservableTransformer { events ->
        events.flatMap {
            dataManager.discoverMovies()
                    .map { DiscoverMoviesResult(movies = it) }
                    .onErrorReturn { DiscoverMoviesResult(error = it) }
                    .startWith(DiscoverMoviesResult(inProgress = true))
        }
    }

    private fun stateReducer(previousState: ExploreUiModel, result: Result): ExploreUiModel {
        when (result) {
            is DiscoverMoviesResult -> {
                return previousState.copy(
                        movies = if (result.error != null) previousState.movies else result.movies,
                        inProgress = result.inProgress, error = result.error)
            }
        }
        return previousState
    }
}
