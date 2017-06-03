package fmrsabino.moviesdb;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SearchViewTest {
    //public final ActivityTestRule main = new ActivityTestRule<>(SearchView.class, false, false);

    /*@Test
    public void checkInput() {
        main.launchActivity(null);
        onView(withId(R.id.activity_search_query_field)).perform(typeText("Hello World!"));
        onView(withId(R.id.activity_search_query_field)).check(matches(withText("Hello World! ")));
    }*/

    /*@Test
    public void searchResultsTest() {
        SearchView activity = (SearchView) main.launchActivity(null);
        List<Result> results = new ArrayList<>();
        results.add(Result.builder().id(0).title("Mock Title 0").build());
        results.add(Result.builder().id(1).title("Mock Title 1").build());
        Search search = Search.builder().results(results).page(1).build();

        runOnUiSync(()-> activity.showSearchResults(search, true));

        int position = 0;
        for (Result result : results) {
            onView(withId(R.id.activity_search_list))
                    .perform(RecyclerViewActions.scrollToPosition(position));
            onView(withText(result.title())).check(matches(isDisplayed()));
            position++;
        }
    }*/

    //Execute a call on the application's main thread, blocking until it is complete. Use
    private void runOnUiSync(Runnable r) {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(r);
    }
}
