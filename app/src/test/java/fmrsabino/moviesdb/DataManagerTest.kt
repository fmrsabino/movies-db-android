package fmrsabino.moviesdb

import fmrsabino.moviesdb.data.DataManager
import fmrsabino.moviesdb.data.local.DatabaseHelper
import fmrsabino.moviesdb.data.model.configuration.Configuration
import fmrsabino.moviesdb.data.model.configuration.Image
import fmrsabino.moviesdb.data.remote.MovieAPI
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import rx.Observable
import rx.observers.TestSubscriber

@RunWith(MockitoJUnitRunner::class)
class DataManagerTest {
    @Mock lateinit var movieAPI: MovieAPI
    @Mock lateinit var databaseHelper: DatabaseHelper
    @Mock lateinit var dataManager: DataManager

    @Before
    fun setUp() {
        dataManager = DataManager(movieAPI, databaseHelper)
    }

    @Test
    fun syncImageEmitsItems() {
        val image = Image(baseUrl = "mock")
        val configuration = Configuration(images = image)

        `when`(movieAPI.getConfiguration()).thenReturn(Observable.just<Configuration>(configuration))
        `when`(databaseHelper.setImage(configuration.images)).thenReturn(Observable.just(configuration.images))

        val testSubscriber = TestSubscriber<Image>()
        dataManager.syncImage().subscribe(testSubscriber)
        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(image)
    }

    @Test
    fun syncImageDoesNotCallDatabaseOnApiFail() {
        `when`(movieAPI.getConfiguration()).thenReturn(Observable.error(RuntimeException()))
        dataManager.syncImage().subscribe(TestSubscriber<Any>())
        verify(movieAPI).getConfiguration()
        verify(databaseHelper, never()).setImage(any(Image::class.java))
    }

    @Test
    fun observeImageNotStoredTest() {
        val image = Image(baseUrl = "mock")
        val configuration = Configuration(images = image)

        `when`(movieAPI.getConfiguration()).thenReturn(Observable.just<Configuration>(configuration))
        `when`(databaseHelper.setImage(configuration.images)).thenReturn(Observable.just(configuration.images))
        `when`(databaseHelper.hasImage()).thenReturn(false)
        `when`(databaseHelper.observeImage()).thenReturn(Observable.just<Image>(image))

        val testSubscriber = TestSubscriber<Image>()
        dataManager.observeImage().subscribe(testSubscriber)
        testSubscriber.assertValue(image)
    }

    @Test
    fun observeImageNoApiCallIfImagePresent() {
        val image = Image(baseUrl = "mock")
        `when`(databaseHelper.hasImage()).thenReturn(true)
        `when`(databaseHelper.observeImage()).thenReturn(Observable.just<Image>(image))
        verify(movieAPI, never()).getConfiguration()
        dataManager.observeImage().subscribe()
    }
}
