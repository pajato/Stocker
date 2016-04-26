package com.pajato.android.stocker;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Provide a set of tests that augment the do nothing tests to get the percent of covered code over the 80% bar.
 *
 * @author Paul Michael Reilly
 */
@RunWith(AndroidJUnit4.class)
public class BasicCoverageTest {

    /** The rule used to launch the activity under test. */
    @Rule public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    /** Test that the FAB button invokes the snackbar message.  Then make it disappear using a swipe right gesture. */
    @Test public void testSnackbarViaFabButton() {
        // Ensure that the FAB button is visible and click on it, verifying that a snackbar message is displayed and
        // removing the snackbar message.
        onView(withId(R.id.fab)).perform(click());
        onView(withId(android.support.design.R.id.snackbar_text))
            .check(matches(isDisplayed()))
            .perform(swipeRight());
    }

    /** Test that the menu can be invoked correctly. */
    @Test public void testSettingsMenuItem() {
        // Open the overflow menu, verify that the Settings menu choice is displayed and click on it.
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText("Settings"))
            .check(matches(isDisplayed()))
            .perform(click());
    }
}
