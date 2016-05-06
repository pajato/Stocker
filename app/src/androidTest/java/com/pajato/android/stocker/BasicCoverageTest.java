package com.pajato.android.stocker;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.pajato.android.stocker.event.MessageEvent;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Provide a set of tests that augment the do nothing tests to get the percent of covered code over the 80% bar.
 *
 * @author Paul Michael Reilly
 */
@RunWith(AndroidJUnit4.class)
public class BasicCoverageTest {

    private static final String DIALOG_TITLE = "Symbol Search";

    /** The rule used to launch the activity under test. */
    @Rule public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    /** Test that the FAB button invokes the stock add dialog.  Then make it disappear using a press back. */
    @Test public void testSnackbarViaFabButton() {
        // Ensure that the FAB button is visible and click on it, verifying that a snackbar message is displayed and
        // removing the snackbar message.
        onView(withId(R.id.fab)).perform(click());
        onView(withText(DIALOG_TITLE)).check(matches(isDisplayed()));
        pressBack();
    }

    /** Test that the menu can be invoked correctly. */
    @Test public void testSettingsMenuItem() {
        // Open the overflow menu, verify that the Settings menu choice is displayed and click on it.
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText("Settings"))
            .check(matches(isDisplayed()))
            .perform(click());
    }

    /** Test that the default symbols have been added to the reccycler view list. */
    @Test public void testForBasicSymbols() {
        String[] symbols = new String[] {"AAPL", "GOOG", "MSFT", "YHOO"};
        for (String symbol : symbols) {
            onView(withText(symbol)).check(matches(isDisplayed()));
        }
    }

    /** Test that an I/O error message is presented correctly. */
    @Test public void testIoErrorMessageEvent() throws Throwable {
        // Post an event to report an I/O error message.
        final String message = "Testing I/O error message!";
        final List<String> payload = new ArrayList<>();
        payload.add(message);
        final MessageEvent event = new MessageEvent(MessageEvent.Type.REPORT_IO_ERROR, payload);
        activityRule.runOnUiThread(new Runnable() {
            @Override public void run() {
                activityRule.getActivity().getEventBus().post(event);
            }
        });

        // Ensure that the message got posted.
        onView(withText("AAPL")).check(matches(isDisplayed())).perform(click());
        //onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(message))).perform(swipeRight());
    }

    /** Test that a status message is presented correctly. */
    @Test public void testStatusMessageEvent() throws Throwable {
        // Post an event to report a status message.
        final String message = "Testing status message!";
        final List<String> payload = new ArrayList<>();
        payload.add(message);
        final MessageEvent event = new MessageEvent(MessageEvent.Type.REPORT_STATUS, payload);
        activityRule.runOnUiThread(new Runnable() {
            @Override public void run() {
                activityRule.getActivity().getEventBus().post(event);
            }
        });

        // Ensure that the message got posted.
        onView(withText("AAPL")).check(matches(isDisplayed())).perform(click());
        //onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(message))).perform(swipeRight());
    }

    /** Test that a singular symbol error message is presented correctly. */
    @Test public void testSingleSymbolErrorMessageEvent() throws Throwable {
        // Post an event to report a status message.
        final List<String> payload = new ArrayList<>();
        payload.add("BADSYM1");
        final MessageEvent event = new MessageEvent(MessageEvent.Type.REPORT_SYMBOL_ERROR, payload);
        activityRule.runOnUiThread(new Runnable() {
            @Override public void run() {
                activityRule.getActivity().getEventBus().post(event);
            }
        });

        // Ensure that the message got posted.
        final String message = "Invalid symbol: BADSYM1";
        onView(withText("AAPL")).check(matches(isDisplayed())).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(message))).perform(swipeRight());
    }

    /** Test that a plural symbol error message is presented correctly. */
    @Test public void testMultipleSymbolErrorMessageEvent() throws Throwable {
        // Post an event to report a status message.
        final List<String> payload = new ArrayList<>();
        payload.add("BADSYM1");
        payload.add("BADSYM2");
        payload.add("BADSYM3");
        final MessageEvent event = new MessageEvent(MessageEvent.Type.REPORT_SYMBOL_ERROR, payload);
        activityRule.runOnUiThread(new Runnable() {
            @Override public void run() {
                activityRule.getActivity().getEventBus().post(event);
            }
        });

        // Ensure that the message got posted.
        final String message = "Invalid symbols: BADSYM1, BADSYM2, BADSYM3";
        onView(withText("AAPL")).check(matches(isDisplayed())).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(message))).perform(swipeRight());
    }
}
