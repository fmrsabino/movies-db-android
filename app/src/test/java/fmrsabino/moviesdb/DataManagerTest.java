package fmrsabino.moviesdb;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import fmrsabino.moviesdb.data.DataManager;
import fmrsabino.moviesdb.data.local.DatabaseHelper;
import fmrsabino.moviesdb.data.model.configuration.Configuration;
import fmrsabino.moviesdb.data.model.configuration.Image;
import fmrsabino.moviesdb.data.remote.MovieService;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataManagerTest {
    @Mock MovieService movieService;
    @Mock DatabaseHelper databaseHelper;
    private DataManager dataManager;

    @Before
    public void setUp() {
        dataManager = new DataManager(movieService, databaseHelper);
    }

    @Test
    public void syncImageEmitsItems() {
        Image image = Image.builder().baseUrl("mock").build();
        Configuration configuration = Configuration.builder().images(image).build();

        when(movieService.getConfiguration()).thenReturn(Observable.just(configuration));
        when(databaseHelper.setImage(configuration.images())).thenReturn(Observable.just(configuration.images()));

        TestSubscriber<Image> testSubscriber = new TestSubscriber<>();
        dataManager.syncImage().subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValue(image);
    }

    @Test
    public void syncImageDoesNotCallDatabaseOnApiFail() {
        when(movieService.getConfiguration()).thenReturn(Observable.error(new RuntimeException()));
        dataManager.syncImage().subscribe(new TestSubscriber<>());
        verify(movieService).getConfiguration();
        verify(databaseHelper, never()).setImage(any(Image.class));
    }

    @Test
    public void observeImageNotStoredTest() {
        Image image = Image.builder().baseUrl("mock").build();
        Configuration configuration = Configuration.builder().images(image).build();

        when(movieService.getConfiguration()).thenReturn(Observable.just(configuration));
        when(databaseHelper.setImage(configuration.images())).thenReturn(Observable.just(configuration.images()));
        when(databaseHelper.hasImage()).thenReturn(false);
        when(databaseHelper.observeImage()).thenReturn(Observable.just(image));

        TestSubscriber<Image> testSubscriber = new TestSubscriber<>();
        dataManager.observeImage().subscribe(testSubscriber);
        testSubscriber.assertValue(image);
    }

    @Test
    public void observeImageNoApiCallIfImagePresent() {
        Image image = Image.builder().baseUrl("mock").build();
        when(databaseHelper.hasImage()).thenReturn(true);
        when(databaseHelper.observeImage()).thenReturn(Observable.just(image));
        verify(movieService, never()).getConfiguration();
        dataManager.observeImage().subscribe();
    }
}
