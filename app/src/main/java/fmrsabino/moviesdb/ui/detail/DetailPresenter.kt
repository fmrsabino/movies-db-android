package fmrsabino.moviesdb.ui.detail

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import fmrsabino.moviesdb.data.DataManager
import fmrsabino.moviesdb.injection.scope.ForView
import fmrsabino.moviesdb.ui.base.results.Result
import fmrsabino.moviesdb.ui.base.uievents.RefreshEvent
import fmrsabino.moviesdb.ui.base.uievents.UiEvent
import fmrsabino.moviesdb.ui.explore.ConfigurationResult
import fmrsabino.moviesdb.util.debounceExceptFirst
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DetailPresenter(val dataManager: DataManager) : ViewModel(), DetailContract.Presenter {
    override val uiEvents: Subject<UiEvent> = BehaviorSubject.create<UiEvent>()
    var mediaId: Int? = null

    init {
        uiEvents.onNext(RefreshEvent())
    }

    private val transformer: ObservableTransformer<UiEvent, DetailUiModel> = ObservableTransformer { events ->
        events.publish { shared ->
            Observable.merge(
                    shared.ofType(RefreshEvent::class.java).compose(movieInfoTransformer),
                    shared.ofType(RefreshEvent::class.java).compose(configurationTransformer))
        }.scan(DetailUiModel(), { previousState, result -> stateReducer(previousState, result) })
    }

    private val movieInfoTransformer: ObservableTransformer<UiEvent, Result> = ObservableTransformer { events ->
        events.flatMap {
            mediaId?.let {
                dataManager.getMovie(it)
                        .map { MovieResult(movie = it) }
                        .onErrorReturn { MovieResult(error = it) }
                        .startWith(MovieResult(inProgress = true))
            }
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

    override fun setMediaId(mediaId: Int) {
        this.mediaId = mediaId
    }

    private val uiModelObservable: Observable<DetailUiModel> = uiEvents.compose(transformer)
            .replay(1).autoConnect().debounceExceptFirst(500, TimeUnit.MILLISECONDS)

    override fun observeUiModel() = uiModelObservable

    private fun stateReducer(previousState: DetailUiModel, result: Result): DetailUiModel {
        return when (result) {
            is MovieResult -> {
                previousState.copy(
                        movie = if (result.inProgress || result.error != null) previousState.movie else result.movie,
                        movieInProgress = result.inProgress, movieError = result.error)
            }
            is ConfigurationResult -> {
                previousState.copy(
                        configuration = result.imageConfiguration,
                        configurationInProgress = result.inProgress)
            }
            else -> {
                previousState
            }
        }
    }

    companion object {
        @ForView
        class Factory @Inject constructor(val dataManager: DataManager) : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>?): T {
                //This is ugly
                @Suppress("UNCHECKED_CAST")
                return DetailPresenter(dataManager) as T
            }
        }
    }
}
