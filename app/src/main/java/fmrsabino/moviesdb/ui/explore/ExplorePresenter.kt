package fmrsabino.moviesdb.ui.explore

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import fmrsabino.moviesdb.data.DataManager
import fmrsabino.moviesdb.injection.scope.ForView
import fmrsabino.moviesdb.ui.base.results.Result
import fmrsabino.moviesdb.ui.base.uievents.RefreshEvent
import fmrsabino.moviesdb.ui.base.uievents.UiEvent
import fmrsabino.moviesdb.util.debounceExceptFirst
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ExplorePresenter(val dataManager: DataManager) : ViewModel(), ExploreContract.Presenter {
    private var currentState = ExploreUiModel()
    override val uiEvents: Subject<UiEvent> = BehaviorSubject.create<UiEvent>()

    init {
        uiEvents.onNext(RefreshEvent())
    }

    private val transformer: ObservableTransformer<UiEvent, ExploreUiModel> = ObservableTransformer { events ->
        events.publish { shared ->
            Observable.merge(
                    shared.ofType(RefreshEvent::class.java).compose(discoverTvTransformer),
                    shared.ofType(RefreshEvent::class.java).compose(configurationTransformer),
                    shared.ofType(RefreshEvent::class.java).compose(discoverMoviesTransformer))
        }.scan(currentState, { previous, result -> stateReducer(previous, result) })
    }

    private val discoverMoviesTransformer: ObservableTransformer<UiEvent, Result> = ObservableTransformer { events ->
        events.flatMap {
            dataManager.discoverMovies()
                    .map { DiscoverMoviesResult(movies = it) }
                    .onErrorReturn { DiscoverMoviesResult(error = it) }
                    .startWith(DiscoverMoviesResult(inProgress = true))
        }
    }

    private val configurationTransformer: ObservableTransformer<UiEvent, Result> = ObservableTransformer { events ->
        events.flatMap {
            dataManager.getConfiguration()
                    .map { ConfigurationResult(imageConfiguration = it) }
                    .onErrorReturn { ConfigurationResult(error = it) }
                    .startWith(ConfigurationResult(inProgress = true))
        }
    }

    private val discoverTvTransformer: ObservableTransformer<UiEvent, Result> = ObservableTransformer { events ->
        events.flatMap {
            dataManager.discoverTv()
                    .map { DiscoverTvResult(series = it) }
                    .onErrorReturn { DiscoverTvResult(error = it) }
                    .startWith(DiscoverTvResult(inProgress = true))
        }
    }

    private val uiModelObservable: Observable<ExploreUiModel> = uiEvents.compose(transformer)
            .replay(1).autoConnect().debounceExceptFirst(500, TimeUnit.MILLISECONDS)

    override fun observeUiModel() = uiModelObservable

    private fun stateReducer(previousState: ExploreUiModel, result: Result): ExploreUiModel {
        when (result) {
            is DiscoverMoviesResult -> {
                currentState = previousState.copy(
                        movies = if (result.inProgress || result.error != null) previousState.movies else result.movies,
                        discoverMoviesInProgress = result.inProgress, discoverMoviesError = result.error)
            }
            is ConfigurationResult -> {
                currentState = previousState.copy(
                        configuration = result.imageConfiguration,
                        configurationInProgress = result.inProgress)
            }
            is DiscoverTvResult -> {
                currentState = previousState.copy(
                        tv = if (result.inProgress || result.error != null) previousState.tv else result.series,
                        discoverTvInProgress = result.inProgress, discoverTvError = result.error)
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
