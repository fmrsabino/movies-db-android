package fmrsabino.moviesdb.ui.explore

import fmrsabino.moviesdb.data.DataManager
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ExplorePresenterTest {
    @Mock lateinit var dataManager: DataManager
    lateinit var presenter: ExplorePresenter

    @Before
    fun setUp() {
        presenter = ExplorePresenter(dataManager)
    }

    @Test
    fun testTransformer() {
        Assert.assertNotNull(presenter)
    }
}
