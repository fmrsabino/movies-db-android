package fmrsabino.moviesdb.ui.explore

import fmrsabino.moviesdb.ui.base.uievents.UiEvent
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

interface ExploreContract {
    interface Presenter {
        var requested: Boolean
        val uiEvents: PublishSubject<UiEvent>
        fun observeUiModel(): Observable<ExploreUiModel>
    }
}
