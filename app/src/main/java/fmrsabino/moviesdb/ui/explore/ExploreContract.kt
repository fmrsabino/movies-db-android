package fmrsabino.moviesdb.ui.explore

import fmrsabino.moviesdb.ui.base.uievents.UiEvent
import io.reactivex.Observable
import io.reactivex.subjects.Subject

interface ExploreContract {
    interface Presenter {
        val uiEvents: Subject<UiEvent>
        fun observeUiModel(): Observable<ExploreUiModel>
        fun initialRequest()
    }
}
