package com.pajato.android.stocker.event;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.pajato.android.stocker.MainActivity;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Provide a set of device based unit tests for event bus implementation.
 *
 * @author Paul Michael Reilly
 */
@RunWith(AndroidJUnit4.class)
public class EventBusUnitTest implements EventBus.Subscriber {

    /** The rule used to launch the activity under test. */
    @Rule public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    /** Provide a dummy subscriber to test isRegistered(). */
    @Override public void onPost(final MessageEvent event) {}

    /** Run some basic singleton via enum code coverage unit tests. */
    @Test public void unitTest() {
        // Test that the instance value is accessible.
        Assert.assertTrue("Test the instance value.", EventBus.valueOf("instance") == EventBus.instance);

        // Cover the values() method.
        Assert.assertTrue("Test that there is only a single value.", EventBus.values().length == 1);
    }

    /** Test basic isRegistered() functionality. */
    @Test public void isRegisteredTest() {
        // Register the class and find it registered.
        EventBus.instance.register(this);
        Assert.assertTrue("Failed to register the test class!", EventBus.instance.isRegistered(this));

        // Unregister the class and find it not registered.
        EventBus.instance.unregister(this);
        Assert.assertFalse("Found the test class still registered!", EventBus.instance.isRegistered(this));

        // Find a class that does not implement the Subscriber interface to be not registered.
        Object foo = new Object();
        Assert.assertFalse("A plain old object did not fail to be found registered!", EventBus.instance.isRegistered(foo));
    }
}
