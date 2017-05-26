package fmrsabino.moviesdb.ui.explore

import android.app.Application
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ExplorePresenterTest {
    @Mock lateinit var application: Application
    lateinit var presenter: ExplorePresenter

    @Before
    fun setUp() {
        presenter = ExplorePresenter(application)
    }

    @Test
    fun testTransformer() {
        Assert.assertNotNull(presenter.transformer)
    }
}