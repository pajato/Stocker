package com.pajato.android.stocker.stocklist;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.pajato.android.stocker.MainActivity;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Provide a set of device based unit tests for the basic stocklist stock model class.
 *
 * @author Paul Michael Reilly
 */
@RunWith(AndroidJUnit4.class)
public class StockListManagerUnitTest {

    /** The rule used to launch the activity under test. */
    @Rule public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test public void unitTest() {
        // Test that the instance value is accessible.
        Assert.assertTrue("Test the instance value.", StockListManager.valueOf("instance") == StockListManager.instance);

        // Test that there is an adapter to be had.
        Assert.assertTrue("Test for a non-null adapter object.", StockListManager.instance.getAdapter() != null);

        // Cover the values() method.
        Assert.assertTrue("Test that there is only a single value.", StockListManager.values().length == 1);
    }

}
