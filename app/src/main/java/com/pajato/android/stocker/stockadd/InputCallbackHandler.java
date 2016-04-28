package com.pajato.android.stocker.stockadd;

import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.pajato.android.stocker.event.MessageEvent;
import com.pajato.android.stocker.event.MessageEvent.Type;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Provide a singleton handler to process a result obtained from the new symbol dialog.  Subscribes to the event bus.
 *
 * @author Paul Michael Reilly
 */
public enum InputCallbackHandler implements MaterialDialog.InputCallback {
    // The singleton and sole enumeration value.
    instance;

    // Private constants.

    /** The logcat tag. */
    private static final String TAG = InputCallbackHandler.class.getSimpleName();

    // Private instance variables.

    /** Track the currently active stock symbols. */
    private List<String> mSymbolList = new ArrayList<>();

    /** The view causing the dialog to exist. */
    private View mView;

    /** Process the new symbol by ensuring it does not already exist. */
    @Override public void onInput(MaterialDialog dialog, CharSequence input) {
        // Determine if the new symbol is really a new symbol.
        if (mSymbolList.contains(input.toString())) {
            // Invoke a snackbar to the event the input is already registered.
            Snackbar.make(mView, "The symbol is already registered.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        } else {
            // Post the new symbol.
            List<String> payload = new ArrayList<>();
            payload.add(input.toString());
            EventBus.getDefault().post(new MessageEvent(Type.ADD, payload));
        }
    }

    /** Capture stock symbols to be added and removed via the event bus. */
    @Subscribe public void onMessageEvent(final MessageEvent event) {
        // Add the entire collection of symbols in the event payload.
        try {
            switch (event.getType()) {
            case ADD:
                // Add zero or more stock symbols in order to detect subsequent attempt to create duplicate symbols.
                mSymbolList.addAll(event.getPayload());
                break;

            case REMOVE:
                // Remove zero or more stock symbols.
                mSymbolList.removeAll(event.getPayload());
                break;

            default:
                // Ignore unsupported events entirely.
                break;
            }
        } catch (UnsupportedOperationException exc) {
            Log.e(TAG, "This Android implementation of ArrayList does not support bulk entry removal!", exc);
            Snackbar.make(mView, "Stock symbols cannot be removed on this device.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        } catch (NullPointerException exc) {
            // Log but otherwise ignore the extremely unlikely NPE.
            Log.e(TAG, "Unexpected software error: null payload.", exc);
        }
    }

    /** Capture the view invoking the dialog. */
    public void setView(final View view) {
        mView = view;
    }
}
