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
public class StockModelUnitTest {

    /** The rule used to launch the activity under test. */
    @Rule public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test public void unitTest() {
        StockModel a = new StockModel("GOOG", 721.22, 0.3);
        StockModel b = new StockModel("GOOG", 721.22, 0.3);
        StockModel c = new StockModel("AAPL", 52.97, -0.83);
        StockModel d = new StockModel(null, 0.0, 0.0);
        StockModel e = new StockModel(null, 0.0, 0.0);

        // Test hashCode.
        int ha = a.hashCode();
        int hb = b.hashCode();
        int hc = c.hashCode();
        int hd = d.hashCode();
        Assert.assertTrue("Test that the hash code for the same object is equal.", ha == a.hashCode());
        Assert.assertTrue("Test that the hash code for two different (but equal) objects are the same.", ha == hb);
        Assert.assertFalse("Test that the hash code for two different (but not equal) objects are different.", ha == hc);

        // Test equals and setters.
        Assert.assertTrue("Check that a StockModel object is equal to itself.", a.equals(a));
        Assert.assertFalse("Check that a StockModel object cannot be equal to a String.", a.equals("GOOG"));
        Assert.assertFalse("Check that two equal objects are not the same object.", a == b);
        Assert.assertTrue("Check that two StockModel objects with the same member data are equal.", a.equals(b));
        Assert.assertFalse("Check that a null symbol generates not equal.", d.equals(a));
        Assert.assertFalse("Check that a null symbol generates not equal.", a.equals(d));
        Assert.assertTrue("Check that two null symbols will be equal.", d.equals(e));
        d.setSymbol("GOOG");
        Assert.assertNotEquals("Check that a partially equal object is still unequal.", a, d);
        d.setPrice(721.22);
        Assert.assertNotEquals("Check that a partially equal object is still unequal.", a, d);
        d.setDelta(0.3);
        Assert.assertEquals("Check that a fully equal object is now equal.", a, d);

        // Test setters.
        d.setPrice(b.getPrice());
        d.setDelta(b.getDelta());
        Assert.assertEquals("Check that a and d now have the same values.", a, d);

        // Test the toString method.
        final String expectedText = "StockModel(symbol=GOOG, price=721.22, delta=0.3)";
        Assert.assertEquals("Test that toString generates the expected text.", a.toString(), expectedText);
    }

}
