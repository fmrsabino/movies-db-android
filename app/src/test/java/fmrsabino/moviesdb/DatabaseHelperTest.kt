package fmrsabino.moviesdb

import fmrsabino.moviesdb.data.local.DatabaseHelper
import fmrsabino.moviesdb.data.local.DbOpenHelper
import fmrsabino.moviesdb.data.local.table.ImageTable
import fmrsabino.moviesdb.data.model.configuration.Image
import io.reactivex.observers.TestObserver
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class DatabaseHelperTest {
    lateinit var databaseHelper: DatabaseHelper

    @JvmField @Rule val trampolineSchedulerRule = TrampolineSchedulerRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        databaseHelper = DatabaseHelper(DbOpenHelper(RuntimeEnvironment.application))
    }

    @Test
    fun setImageTest() {
        val image = Image(baseUrl = "mockUrl")

        //Verify Image emission
        val testObserver = TestObserver<Image>()
        databaseHelper.setImage(image).subscribe(testObserver)
        testObserver.assertNoErrors()
        testObserver.assertValue(image)

        //Verify object storage
        val c = databaseHelper.db.query("SELECT * FROM " + ImageTable.TABLE_NAME)
        assertNotNull(c)
        c?.let {
            assertEquals(it.count, 1)
            it.moveToNext()
            val img = ImageTable.parseCursor(it)
            assertEquals(image, img)
        }
    }

    @Test
    fun setImageReplacesOldImage() {
        val oldImage = Image(baseUrl = "oldImage")
        val newImage = Image(baseUrl = "newImage")
        databaseHelper.setImage(oldImage).subscribe()
        databaseHelper.setImage(newImage).subscribe()
        val c = databaseHelper.db.query("SELECT * FROM " + ImageTable.TABLE_NAME)
        assertNotNull(c)
        c?.let {
            assertEquals(it.count, 1)
            val img = ImageTable.parseCursor(it)
            assertEquals(newImage, img)
        }
    }

    @Test
    fun observeImageDoesNotEnd() {
        val testObserver = TestObserver<Image>()
        databaseHelper.observeImage().subscribe(testObserver)
        testObserver.assertNoErrors()
        testObserver.assertNotTerminated()
    }

    @Test
    fun observeImageEmitsValues() {
        val image = Image()
        databaseHelper.setImage(image).subscribe(TestObserver())
        val testObserver = TestObserver<Image>()
        databaseHelper.observeImage().subscribe(testObserver)
        testObserver.assertNoErrors()
        testObserver.assertValue(image)
        testObserver.assertNotTerminated()
    }
}
