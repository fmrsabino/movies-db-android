package fmrsabino.moviesdb.ui.explore

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import fmrsabino.moviesdb.data.DataManager
import fmrsabino.moviesdb.injection.scope.ForView
import fmrsabino.moviesdb.ui.base.results.Result
import fmrsabino.moviesdb.ui.base.uievents.RefreshEvent
import fmrsabino.moviesdb.ui.base.uievents.UiEvent
import fmrsabino.moviesdb.ui.base.uievents.ViewActiveEvent
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class ExplorePresenter(dataManager: DataManager) : ViewModel(), ExploreContract.Presenter {
    var currentState = ExploreUiModel()
    override val uiEvents: PublishSubject<UiEvent> = PublishSubject.create<UiEvent>()
    override var requested = false


    private val transformer: ObservableTransformer<UiEvent, ExploreUiModel> = ObservableTransformer { events ->
        events.publish { shared ->
            Observable.merge(
                    shared.ofType(ViewActiveEvent::class.java).compose(refreshDataTransformer),
                    shared.ofType(RefreshEvent::class.java).compose(refreshDataTransformer))
                    .scan(currentState, { previous, result -> stateReducer(previous, result) })
        }
    }

    private val refreshDataTransformer: ObservableTransformer<UiEvent, Result> = ObservableTransformer { events ->
        events.flatMap {
            dataManager.discoverMovies()
                    .doOnSubscribe { requested = true }
                    .map { DiscoverMoviesResult(movies = it) }
                    .onErrorReturn { DiscoverMoviesResult(error = it) }
                    .startWith(DiscoverMoviesResult(inProgress = true))
        }
    }

    private val uiModelObservable: Observable<ExploreUiModel> = uiEvents.compose(transformer).replay(1).autoConnect()

    override fun observeUiModel() = uiModelObservable

    private fun stateReducer(previousState: ExploreUiModel, result: Result): ExploreUiModel {
        when (result) {
            is DiscoverMoviesResult -> {
                currentState = previousState.copy(
                        movies = if (result.inProgress || result.error != null) previousState.movies else result.movies,
                        inProgress = result.inProgress, error = result.error)
            }
        }
        return currentState
    }

    companion object {
        @ForView
        class Factory @Inject constructor(val dataManager: DataManager) : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>?): T {
                //This is ugly
                @Suppress("UNCHECKED_CAST")
                return ExplorePresenter(dataManager) as T
            }
        }
    }
}
