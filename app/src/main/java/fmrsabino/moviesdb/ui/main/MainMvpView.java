package fmrsabino.moviesdb.ui.main;

import fmrsabino.moviesdb.data.model.search.Search;
import fmrsabino.moviesdb.ui.base.MvpView;

public interface MainMvpView extends MvpView {
    void showSearchResults(Search search, boolean newResults);
    void getPosterImageUrl(String posterPath);
}
