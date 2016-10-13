package fmrsabino.moviesdb.ui.search;

import fmrsabino.moviesdb.data.model.search.Search;
import fmrsabino.moviesdb.ui.base.MvpView;

public interface SearchMvpView extends MvpView {
    void showSearchResults(Search search, boolean newResults);
    void getPosterImageUrl(String posterPath);
}
