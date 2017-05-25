package fmrsabino.moviesdb

import fmrsabino.moviesdb.data.DataManager
import fmrsabino.moviesdb.data.local.DatabaseHelper
import fmrsabino.moviesdb.data.model.configuration.Configuration
import fmrsabino.moviesdb.data.model.configuration.Image
import fmrsabino.moviesdb.data.remote.MovieAPI
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DataManagerTest {
    @Mock lateinit var movieAPI: MovieAPI
    @Mock lateinit var databaseHelper: DatabaseHelper
    @Mock lateinit var dataManager: DataManager

    companion object {
        @BeforeClass
        fun setupClass() {
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        }
    }

    @Before
    fun setUp() {
        dataManager = DataManager(movieAPI, databaseHelper)
    }

    @Test
    fun syncImageEmitsItems() {
        val image = Image(baseUrl = "mock")
        val configuration = Configuration(images = image)

        `when`(movieAPI.getConfiguration()).thenReturn(Observable.just(configuration))
        `when`(databaseHelper.setImage(configuration.images)).thenReturn(Observable.just(configuration.images))

        val testObserver = TestObserver<Image>()
        dataManager.syncImage().subscribe(testObserver)
        testObserver.assertNoErrors()
        testObserver.assertValue(image)
    }

    @Test
    fun syncImageDoesNotCallDatabaseOnApiFail() {
        `when`(movieAPI.getConfiguration()).thenReturn(Observable.error(RuntimeException()))
        dataManager.syncImage().subscribe(TestObserver())
        verify(movieAPI).getConfiguration()
        verify(databaseHelper, never()).setImage(Image())
    }

    @Test
    fun observeImageTest() {
        `when`(databaseHelper.observeImage()).thenReturn(Observable.just(Image()))

        dataManager.observeImage().subscribe(TestObserver())

        verify(databaseHelper).observeImage()
    }
}
