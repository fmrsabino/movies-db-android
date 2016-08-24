package fmrsabino.moviesdb;

import android.database.Cursor;
import android.os.Build;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import fmrsabino.RxSchedulersOverrideRule;
import fmrsabino.moviesdb.data.local.DatabaseHelper;
import fmrsabino.moviesdb.data.local.DbOpenHelper;
import fmrsabino.moviesdb.data.local.table.ImageTable;
import fmrsabino.moviesdb.data.model.configuration.Image;
import rx.observers.TestSubscriber;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.M)
public class DatabaseHelperTest {
    private DatabaseHelper databaseHelper;

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        databaseHelper = new DatabaseHelper(new DbOpenHelper(RuntimeEnvironment.application));
    }

    @Test
    public void setImageTest() {
        Image image = Image.builder().baseUrl("mock").build();

        //Verify Image emission
        TestSubscriber<Image> testSubscriber = new TestSubscriber<>();
        databaseHelper.setImage(image).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValue(image);

        //Verify object storage
        Cursor c = databaseHelper.getDb().query("SELECT * FROM " + ImageTable.TABLE_NAME);
        assertEquals(c.getCount(), 1);
        while (c.moveToNext()) {
            Image img = ImageTable.parseCursor(c);
            assertEquals(image, img);
        }
    }

    @Test
    public void setImageReplacesOldImage() {
        Image oldImage = Image.builder().baseUrl("oldImage").build();
        Image newImage = Image.builder().baseUrl("newImage").build();
        databaseHelper.setImage(oldImage).subscribe(new TestSubscriber<>());
        databaseHelper.setImage(newImage).subscribe(new TestSubscriber<>());
        Cursor c = databaseHelper.getDb().query("SELECT * FROM " + ImageTable.TABLE_NAME);
        assertEquals(c.getCount(), 1);
        while (c.moveToNext()) {
            Image img = ImageTable.parseCursor(c);
            assertEquals(newImage, img);
        }
    }

    @Test
    public void observeImageDoesNotEnd() {
        TestSubscriber<Image> testSubscriber = new TestSubscriber<>();
        databaseHelper.observeImage().subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertNotCompleted();
    }

    @Test
    public void observeImageEmitsValues() {
        Image image = Image.builder().baseUrl("mock").build();
        databaseHelper.setImage(image).subscribe(new TestSubscriber<>());
        TestSubscriber<Image> testSubscriber = new TestSubscriber<>();
        databaseHelper.observeImage().subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValue(image);
    }
}