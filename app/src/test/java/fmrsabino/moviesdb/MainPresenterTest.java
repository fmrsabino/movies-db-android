package fmrsabino.moviesdb;

import android.content.Context;
import android.os.Build;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import fmrsabino.RxSchedulersOverrideRule;
import fmrsabino.moviesdb.data.DataManager;
import fmrsabino.moviesdb.data.model.configuration.Image;
import fmrsabino.moviesdb.data.model.search.Result;
import fmrsabino.moviesdb.data.model.search.Search;
import fmrsabino.moviesdb.ui.search.SearchMvpView;
import fmrsabino.moviesdb.ui.search.SearchPresenter;
import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.M)
public class MainPresenterTest {

    @Mock DataManager dataManager;
    @Mock SearchMvpView mainMvpView;
    @Mock Context context;

    private SearchPresenter mainPresenter;

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Image image = Image.builder().baseUrl("mockUrl").build();
        when(dataManager.observeImage()).thenReturn(Observable.just(image));

        mainPresenter = new SearchPresenter(context);
        mainPresenter.onViewAttached(mainMvpView);
    }

    @After
    public void tearUp() {
        mainPresenter.onViewDetached();
    }

    @Test
    public void getSearchReturnsCachedResponse() {
        String query = "mockQuery";
        mainPresenter.setPreviousQuery(query);

        mainPresenter.getSearch(query);
        verify(mainMvpView).showSearchResults(any(Search.class), eq(false));
        verify(mainMvpView, never()).showSearchResults(any(Search.class), eq(true));
    }

    @Test
    public void getSearchReturnsNewResults() {
        String query = "mockQuery";
        Search search = Search.builder().page(1).results(anyListOf(Result.class)).build();
        when(dataManager.getRemoteSearch(query)).thenReturn(Observable.just(search));
        mainPresenter.getSearch(query);

        verify(mainMvpView).showSearchResults(any(Search.class), eq(true));
        verify(mainMvpView, never()).showSearchResults(any(Search.class), eq(false));
    }

    @Test
    public void getSearchOnApiFailure() {
        String query = "mockQuery";
        when(dataManager.getRemoteSearch(query)).thenReturn(Observable.error(new RuntimeException()));
        mainPresenter.getSearch(query);
        verify(mainMvpView).showSearchResults(any(Search.class), eq(true));
    }

    @Test
    public void loadPosterImageUrlEmpty() {
        when(dataManager.observeImage()).thenReturn(Observable.empty());
        mainPresenter.loadPosterImageUrl();
        verify(mainMvpView, never()).getPosterImageUrl(anyString());
    }

    @Test
    public void loadPosterImageUrlError() {
        when(dataManager.observeImage()).thenReturn(Observable.error(new RuntimeException()));
        mainPresenter.loadPosterImageUrl();
        verify(mainMvpView, never()).getPosterImageUrl(anyString());
    }
}
