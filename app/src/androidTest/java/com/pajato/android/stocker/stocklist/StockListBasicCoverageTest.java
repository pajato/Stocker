package com.pajato.android.stocker.stocklist;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.pajato.android.stocker.MainActivity;
import com.pajato.android.stocker.event.MessageEvent;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Provide a set of basic stock list tests to drive code coverage.
 *
 * @author Paul Michael Reilly
 */
@RunWith(AndroidJUnit4.class)
public class StockListBasicCoverageTest {

    private boolean isRegistered = false;

    /** The rule used to launch the activity under test. */
    @Rule public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    /** Test the most basic function, the initial list of stocks. */
    @Test public void testInitialStockList() throws Throwable {
        // Ensure that the stocks have first been added.
        onView(withText("AAPL")).check(matches(isDisplayed()));
        onView(withText("GOOG")).check(matches(isDisplayed()));
        onView(withText("MSFT")).check(matches(isDisplayed()));
        onView(withText("YHOO")).check(matches(isDisplayed()));
    }

    /** Test that the initial set of stocks can be removed. */
    @Test public void testStockRemoval() throws Throwable {
        // Post an event to clear out the current set of stocks.
        List<String> payload = new ArrayList<>();
        payload.add("AAPL");
        payload.add("GOOG");
        payload.add("MSFT");
        payload.add("YHOO");
        final MessageEvent event = new MessageEvent(MessageEvent.Type.REMOVE_SYMBOLS, payload);
        activityRule.runOnUiThread(new Runnable() {
            @Override public void run() {
                activityRule.getActivity().getEventBus().post(event);
            }
        });

        // TODO: Fix this to ensure that the stocks are now gone.  The theorized problem is that Espresso is testing
        // before the UI events have "settled" after the adapter has been notified of the changes.  This will entail
        // adding a IdlingResource.
        onView(withText("AAPL"));
    }

    /** Test that the main activity is registered with the event bus. */
    @Test public void testStockListAdapterRegistration() throws Throwable {
        // Examine the event bus state to ensure the stock list adapter is registered.
        activityRule.runOnUiThread(new Runnable() {
            @Override public void run() {
                Object component = StockListManager.instance.getAdapter();
                setRegistration(activityRule.getActivity().getEventBus().isRegistered(component));
            }
        });
        Assert.assertTrue("The stock list adapter is not registered!", isRegistered);
    }

    // Private instance methods.

    /** Provide a mutator for the registration value. */
    private void setRegistration(final boolean value) {
        isRegistered = value;
    }

}
