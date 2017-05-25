package fmrsabino.moviesdb.ui.explore

import fmrsabino.moviesdb.ui.base.uievents.UiEvent
import io.reactivex.ObservableTransformer

interface ExploreContract {
    interface Presenter {
        val transformer: ObservableTransformer<UiEvent, ExploreUiModel>
    }
}