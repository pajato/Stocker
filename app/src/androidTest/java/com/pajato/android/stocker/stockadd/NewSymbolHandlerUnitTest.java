package com.pajato.android.stocker.stockadd;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.pajato.android.stocker.MainActivity;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Provide a set of device based unit tests for the basic add new stock symbol feature.
 *
 * @author Paul Michael Reilly
 */
@RunWith(AndroidJUnit4.class)
public class NewSymbolHandlerUnitTest {

    /** The rule used to launch the activity under test. */
    @Rule public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test public void unitTest() {
        // Test that the instance value is accessible.
        Assert.assertTrue("Test the instance value.", NewSymbolHandler.valueOf("instance") == NewSymbolHandler.instance);

        // Cover the values() method.
        Assert.assertTrue("Test that there is only a single value.", NewSymbolHandler.values().length == 1);
    }

}
