package fmrsabino.moviesdb;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import fmrsabino.moviesdb.data.model.search.Result;
import fmrsabino.moviesdb.data.model.search.Search;
import fmrsabino.moviesdb.ui.main.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    public final ActivityTestRule main = new ActivityTestRule<>(MainActivity.class, false, false);

    @Test
    public void checkInput() {
        main.launchActivity(null);
        onView(withId(R.id.activity_main_search_text)).perform(typeText("Hello World!"));
        onView(withId(R.id.activity_main_search_text)).check(matches(withText("Hello World! ")));
    }

    @Test
    public void searchResultsTest() {
        MainActivity activity = (MainActivity) main.launchActivity(null);
        List<Result> results = new ArrayList<>();
        results.add(Result.builder().id(0).title("Mock Title 0").build());
        results.add(Result.builder().id(1).title("Mock Title 1").build());
        Search search = Search.builder().results(results).page(1).build();

        runOnUiSync(()-> activity.showSearchResults(search, true));

        int position = 0;
        for (Result result : results) {
            onView(withId(R.id.activity_main_list))
                    .perform(RecyclerViewActions.scrollToPosition(position));
            onView(withText(result.title())).check(matches(isDisplayed()));
            position++;
        }
    }

    //Execute a call on the application's main thread, blocking until it is complete. Use
    private void runOnUiSync(Runnable r) {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(r);
    }
}
