package fmrsabino.moviesdb

import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class MainPresenterTest {
    /*  @Mock lateinit var dataManager: DataManager
      @Mock lateinit var mainMvpView: SearchMvpView
      @Mock lateinit var context: Context

      lateinit var mainPresenter: SearchPresenter

      @Before
      fun setUp() {
          MockitoAnnotations.initMocks(this)
          val imageConfiguration = ImageConfiguration(baseUrl = "mockUrl")
          `when`(dataManager.observeImage()).thenReturn(Observable.just(imageConfiguration))

          mainPresenter = SearchPresenter(context)
          mainPresenter.onViewAttached(mainMvpView)
      }

      @After
      fun tearUp() {
          mainPresenter.onViewDetached()
      }

      @Test
      fun getSearchReturnsCachedResponse() {
          val query = "mockQuery"
          mainPresenter.previousQuery = query

          mainPresenter.getSearch(query)
          verify(mainMvpView).showSearchResults(any(Search::class.java), eq(false))
          verify(mainMvpView, never()).showSearchResults(any(Search::class.java), eq(true))
      }

      @Test
      fun getSearchReturnsNewResults() {
          val query = "mockQuery"
          val search = Search(page = 1, results = anyList())
          `when`(dataManager.getRemoteSearch(query)).thenReturn(Observable.just(search))
          mainPresenter.getSearch(query)

          verify(mainMvpView).showSearchResults(any(Search::class.java), eq(true))
          verify(mainMvpView, never()).showSearchResults(any(Search::class.java), eq(false))
      }

      @Test
      fun getSearchOnApiFailure() {
          val query = "mockQuery"
          `when`(dataManager.getRemoteSearch(query)).thenReturn(Observable.error(RuntimeException()))
          mainPresenter.getSearch(query)
          verify(mainMvpView).showSearchResults(any(Search::class.java), eq(true))
      }

      @Test
      fun loadPosterImageUrlEmpty() {
          `when`(dataManager.observeImage()).thenReturn(Observable.empty())
          mainPresenter.loadPosterImageUrl()
          verify(mainMvpView, never()).getPosterImageUrl(anyString())
      }

      @Test
      fun loadPosterImageUrlError() {
          `when`(dataManager.observeImage()).thenReturn(Observable.error(RuntimeException()))
          mainPresenter.loadPosterImageUrl()
          verify(mainMvpView, never()).getPosterImageUrl(anyString())
      }*/
}
