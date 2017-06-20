package fmrsabino.moviesdb.ui.detail

import fmrsabino.moviesdb.ui.base.uievents.UiEvent
import fmrsabino.moviesdb.ui.explore.ExploreUiModel
import io.reactivex.Observable
import io.reactivex.subjects.Subject

interface DetailContract {
    interface Presenter {
        val uiEvents: Subject<UiEvent>
        fun observeUiModel(): Observable<DetailUiModel>
        fun setMediaId(mediaId: Int)
    }
}