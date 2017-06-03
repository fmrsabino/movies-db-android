package fmrsabino.moviesdb

import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DataManagerTest {
    /*@Mock lateinit var movieAPI: MovieAPI
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
        val imageConfiguration = ImageConfiguration(baseUrl = "mock")
        val imageConfiguration = Configuration(images = imageConfiguration)

        `when`(movieAPI.getImageConfiguration()).thenReturn(Observable.just(imageConfiguration))
        `when`(databaseHelper.setImage(imageConfiguration.images)).thenReturn(Observable.just(imageConfiguration.images))

        val testObserver = TestObserver<ImageConfiguration>()
        dataManager.syncImage().subscribe(testObserver)
        testObserver.assertNoErrors()
        testObserver.assertValue(imageConfiguration)
    }

    @Test
    fun syncImageDoesNotCallDatabaseOnApiFail() {
        `when`(movieAPI.getImageConfiguration()).thenReturn(Observable.error(RuntimeException()))
        dataManager.syncImage().subscribe(TestObserver())
        verify(movieAPI).getImageConfiguration()
        verify(databaseHelper, never()).setImage(ImageConfiguration())
    }

    @Test
    fun observeImageTest() {
        `when`(databaseHelper.observeImage()).thenReturn(Observable.just(ImageConfiguration()))

        dataManager.observeImage().subscribe(TestObserver())

        verify(databaseHelper).observeImage()
    }*/
}
