package fmrsabino.moviesdb.ui.explore

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import fmrsabino.moviesdb.MoviesDbApplication
import fmrsabino.moviesdb.data.DataManager
import fmrsabino.moviesdb.ui.base.results.Result
import fmrsabino.moviesdb.ui.base.uievents.RefreshEvent
import fmrsabino.moviesdb.ui.base.uievents.UiEvent
import fmrsabino.moviesdb.ui.base.uievents.ViewActiveEvent
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import javax.inject.Inject

class ExplorePresenter(application: Application) : AndroidViewModel(application), ExploreContract.Presenter {
    @Inject lateinit var dataManager: DataManager

    var currentState = ExploreUiModel()
    override var requested = false

    init {
        (application as MoviesDbApplication).component.inject(this)
    }

    override val transformer: ObservableTransformer<UiEvent, ExploreUiModel> = ObservableTransformer { events ->
        events.publish { shared ->
            Observable.merge(
                    shared.ofType(ViewActiveEvent::class.java).compose(refreshDataTransformer),
                    shared.ofType(RefreshEvent::class.java).compose(refreshDataTransformer))
                    .scan(currentState, { previous, result -> stateReducer(previous, result) })
        }
    }

    val refreshDataTransformer: ObservableTransformer<UiEvent, Result> = ObservableTransformer { events ->
        events.flatMap {
            dataManager.discoverMovies()
                    .doOnSubscribe { requested = true }
                    .map { DiscoverMoviesResult(movies = it) }
                    .onErrorReturn { DiscoverMoviesResult(error = it) }
                    .startWith(DiscoverMoviesResult(inProgress = true))
        }
    }

    private fun stateReducer(previousState: ExploreUiModel, result: Result): ExploreUiModel {
        when (result) {
            is DiscoverMoviesResult -> {
                currentState = previousState.copy(
                        movies = if (result.error != null) previousState.movies else result.movies,
                        inProgress = result.inProgress, error = result.error)
            }
        }
        return currentState
    }
}
