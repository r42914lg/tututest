package com.r42914lg.tutu.test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static com.r42914lg.tutu.test.RecyclerViewItemCountAssertion.withItemCount;

import com.r42914lg.tutu.R;

import com.r42914lg.tutu.domain.Category;
import com.r42914lg.tutu.domain.CategoryDetailed;
import com.r42914lg.tutu.domain.Clue;
import com.r42914lg.tutu.service.API_JService;
import com.r42914lg.tutu.service.RestClient;
import com.r42914lg.tutu.ui.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import retrofit2.mock.Calls;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.anyInt;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

public class TuTuSampleTest {

    @Mock
    API_JService mockApi;

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        RestClient.getInstance().setApi(mockApi);
    }

    /**
     * Simple instrumental test using Mockito to substitute real API by test data, Espresso to initiate
     * API calls from UI; asserts # of items in adapter matches # of items in categories test list;
     * checks if detailed  fragment shows proper values matching category details test data
     */
    @Test
    public void shouldFillAdapterWithCategoriesFromApi() {

        // create fake list of 8 categories
        List<Category> testCategories = new ArrayList<>();
        testCategories.add(new Category(111, "test_title_1", 11));
        testCategories.add(new Category(222, "test_title_2", 12));
        testCategories.add(new Category(333, "test_title_3", 13));
        testCategories.add(new Category(444, "test_title_4", 14));
        testCategories.add(new Category(555, "test_title_5", 15));
        testCategories.add(new Category(666, "test_title_6", 16));
        testCategories.add(new Category(777, "test_title_7", 17));
        testCategories.add(new Category(888, "test_title_8", 18));

        // mock getCategories() API method by returning a list of 8 categories
        Mockito.when(mockApi.getCategories(anyInt(), anyInt())).thenReturn(Calls.response(testCategories));

        // create 1 fake detailed category
        List<Clue> clues = new ArrayList<>();
        clues.add(new Clue(1001, "answer_1", "question_1"));
        clues.add(new Clue(1002, "answer_2", "question_2"));
        clues.add(new Clue(1003, "answer_3", "question_3"));

        CategoryDetailed categoryDetailed = new CategoryDetailed(111, "test_title_1", 99, clues);

        // mock getDetailedCategory() API  method by returning detailed category
        Mockito.when(mockApi.getDetailedCategory(111)).thenReturn(Calls.response(categoryDetailed));

        // use espresso to initiate API call to getCategories() - FAB click
        onView(withId(R.id.fab)).perform(click());

        // check if # of items in adapter & testCategories list match
        onView(withId(R.id.feed_recycler)).check(withItemCount(testCategories.size()));

        // use espresso to initiate API call to getDetailedCategory() - select 1st row in RecyclerView
        onView(withId(R.id.feed_recycler)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // check if detailed fragment shows proper values matching category details test data
        onView(withId(R.id.detail_category_id))
                .check(matches(withText(containsString("" + categoryDetailed.getId()))));

        onView(withId(R.id.detail_category_title))
                .check(matches(withText(containsString(categoryDetailed.getTitle()))));

        onView(withId(R.id.detail_clues_count))
                .check(matches(withText(containsString("" + categoryDetailed.getCluesCount()))));
    }

}
