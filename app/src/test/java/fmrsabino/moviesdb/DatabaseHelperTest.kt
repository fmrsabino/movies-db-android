package fmrsabino.moviesdb

import fmrsabino.RxSchedulersOverrideRule
import fmrsabino.moviesdb.data.local.DatabaseHelper
import fmrsabino.moviesdb.data.local.DbOpenHelper
import fmrsabino.moviesdb.data.local.table.ImageTable
import fmrsabino.moviesdb.data.model.configuration.Image
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
import rx.observers.TestSubscriber

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class DatabaseHelperTest {
    lateinit var databaseHelper: DatabaseHelper

    @JvmField @Rule val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        databaseHelper = DatabaseHelper(DbOpenHelper(RuntimeEnvironment.application))
    }

    @Test
    fun setImageTest() {
        val image = Image(baseUrl = "mockUrl")

        //Verify Image emission
        val testSubscriber = TestSubscriber<Image>()
        databaseHelper.setImage(image)?.subscribe(testSubscriber)
        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(image)

        //Verify object storage
        val c = databaseHelper.db.query("SELECT * FROM " + ImageTable.TABLE_NAME)
        assertNotNull(c)
        c?.let {
            assertEquals(it.count, 1)
            while (it.moveToNext()) {
                val img = ImageTable.parseCursor(it)
                assertEquals(image, img)
            }
        }
    }

    @Test
    fun setImageReplacesOldImage() {
        val oldImage = Image(baseUrl = "oldImage")
        val newImage = Image(baseUrl = "newImage")
        databaseHelper.setImage(oldImage)?.subscribe(TestSubscriber<Any>())
        databaseHelper.setImage(newImage)?.subscribe(TestSubscriber<Any>())
        val c = databaseHelper.db.query("SELECT * FROM " + ImageTable.TABLE_NAME)
        assertNotNull(c)
        c?.let {
            assertEquals(it.count, 1)
            while (it.moveToNext()) {
                val img = ImageTable.parseCursor(it)
                assertEquals(newImage, img)
            }
        }
    }

    @Test
    fun observeImageDoesNotEnd() {
        val testSubscriber = TestSubscriber<Image>()
        databaseHelper.observeImage().subscribe(testSubscriber)
        testSubscriber.assertNoErrors()
        testSubscriber.assertNotCompleted()
    }

    @Test
    fun observeImageEmitsValues() {
        val image = Image(baseUrl = "mock")
        databaseHelper.setImage(image)?.subscribe(TestSubscriber<Any>())
        val testSubscriber = TestSubscriber<Image>()
        databaseHelper.observeImage().subscribe(testSubscriber)
        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(image)
    }
}
