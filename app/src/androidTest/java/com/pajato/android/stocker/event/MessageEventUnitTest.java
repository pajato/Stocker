package com.pajato.android.stocker.event;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.pajato.android.stocker.MainActivity;
import com.pajato.android.stocker.event.EventBus;
import com.pajato.android.stocker.event.MessageEvent.Type;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

/**
 * Provide a set of device based unit tests for the message event class.
 *
 * @author Paul Michael Reilly
 */
@RunWith(AndroidJUnit4.class)
public class MessageEventUnitTest {

    // Private class constants.

    /** The logcat tag. */
    private static final String TAG = MessageEventUnitTest.class.getSimpleName();

    /** The rule used to launch the activity under test. */
    @Rule public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    /** Provide some extensive unit testing on Lombok generated functionality. */
    @Test public void unitTest() {
        List<String> payload1 = new ArrayList<>();
        payload1.add("a");
        payload1.add("b");
        List<String> payload2 = new ArrayList<>();
        payload2.add("c");
        payload2.add("d");
        MessageEvent a = new MessageEvent(Type.ADD_SYMBOLS, payload1);
        MessageEvent b = new MessageEvent(Type.ADD_SYMBOLS, payload1);
        MessageEvent c = new MessageEvent(Type.REPORT_STATUS, payload1);
        MessageEvent d = new MessageEvent(null, payload1);
        MessageEvent e = new MessageEvent(Type.REPORT_IO_ERROR, null);
        MessageEvent f = new MessageEvent(Type.REPORT_IO_ERROR, payload1);
        MessageEvent g = new MessageEvent(Type.REPORT_IO_ERROR, payload2);
        MessageEvent h = new MessageEvent(null, payload2);
        MessageEvent i = new MessageEvent(null, payload2);
        MessageEvent j = new MessageEvent(Type.ADD_SYMBOLS, null);
        MessageEvent k = new MessageEvent(Type.ADD_SYMBOLS, null);

        // Test hashCode.
        int ha = a.hashCode();  // Two non-null member values.
        int hb = b.hashCode();
        int hc = c.hashCode();  // Null type and non-null payload.
        int hd = d.hashCode();
        int he = e.hashCode();  // Non-null type and null payload.
        Assert.assertTrue("Test that the hash code for the same object is equal.",
                          ha == a.hashCode());
        Assert.assertTrue("Test that the hash code for two different (but equal) objects are the same.",
                          ha == hb);
        Assert.assertFalse("Test that the hash code for two different and unequal objects are different.",
                           ha == hc);

        // Test basic equality
        Assert.assertTrue("Check that a MessageEvent object is equal to itself.",
                          a.equals(a));
        Assert.assertTrue("Check that two MessageEvent objects with the same member data are equal.",
                          a.equals(b));
        Assert.assertFalse("Check that a MessageEvent object cannot be equal to a String.",
                           a.equals("GOOG"));
        Assert.assertFalse("Check that two equal objects are not the same object.",
                           a == b);

        // Test equals()
        Assert.assertFalse("Check that a null type and a non-null type generates not equal.",
                           d.equals(a));
        Assert.assertFalse("Check that a non-null type and a null type generates not equal.",
                           a.equals(d));
        Assert.assertFalse("Check that two different non-null types not equal.", a.equals(c));
        Assert.assertFalse("Check that a null payload and a non-null payload generates not equal.",
                           e.equals(f));
        Assert.assertFalse("Check that a non-null payload and a null payload generates not equal.",
                           f.equals(e));
        Assert.assertFalse("Check that two different non-null payloads and same types are not equal.",
                           f.equals(g));
        Assert.assertTrue("Check that two null types with non-null payloads are equal",
                          h.equals(i));
        Assert.assertTrue("Check that two non-null matching types with null payloads are equal",
                          j.equals(k));

        // Test setters.
        MessageEvent test = new MessageEvent(null, null);
        Assert.assertTrue("Check that the test message event type is empty.",
                          test.getType() == null);
        Assert.assertTrue("Check that the test message event payload is empty.",
                          test.getPayload() == null);
        test.setType(Type.ADD_SYMBOLS);
        test.setPayload(payload1);
        Assert.assertTrue("Check that the test message event type is not empty.",
                          test.getType() == Type.ADD_SYMBOLS);
        Assert.assertTrue("Check that the test message event payload is not empty.",
                          test.getPayload() == payload1);

        // Test the toString method.
        final String expected = "MessageEvent(type=ADD_SYMBOLS, payload=[a, b])";
        final String actual = a.toString();
        Assert.assertEquals("Test that toString generates the expected text.",
                            actual, expected);

        // The the type enum values.
        Assert.assertTrue("Test the add symbos action type.",
                          Type.valueOf("ADD_SYMBOLS") == Type.ADD_SYMBOLS);
        Assert.assertTrue("Test the remove symbols action type.",
                          Type.valueOf("REMOVE_SYMBOLS") == Type.REMOVE_SYMBOLS);
        Assert.assertTrue("Test the report status action type.",
                          Type.valueOf("REPORT_STATUS") == Type.REPORT_STATUS);
        Assert.assertTrue("Test the report I/O error action type.",
                          Type.valueOf("REPORT_IO_ERROR") == Type.REPORT_IO_ERROR);
        Assert.assertTrue("Test the report symbol error action type.",
                          Type.valueOf("REPORT_SYMBOL_ERROR") == Type.REPORT_SYMBOL_ERROR);
    }

}
