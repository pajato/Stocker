package com.pajato.android.stocker.stockadd;

import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.pajato.android.stocker.event.EventBus;
import com.pajato.android.stocker.event.MessageEvent;
import com.pajato.android.stocker.event.MessageEvent.Type;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Provide a singleton handler to process a result obtained from the new symbol dialog.  Subscribes to the event bus.
 *
 * @author Paul Michael Reilly
 */
public enum InputCallbackHandler implements MaterialDialog.InputCallback, EventBus.Subscriber {
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
        // Parse out the separators (commas, colons, tab or space characters);
        String regex = "[ ,:]+";
        String repl = ":";
        String parsedString = input.toString().replaceAll(regex, repl);
        String[] symbols = parsedString.split(repl);

        // Collect the unique symbols that are not already in the displayed stock list.
        Set<String> uniqueSymbols = new HashSet<>();
        Set<String> duplicates = new HashSet<>();
        for (String symbol : symbols) {
            // Ensure that this symbol is not already in the stock list.
            if (mSymbolList.contains(symbol)) {
                // Collect the duplicates to be posted as a status message.
                duplicates.add(symbol);
            } else {
                // Collect the new symbols.
                uniqueSymbols.add(symbol);
            }
        }

        // Process the new stock symbols, if any.
        if (uniqueSymbols.size() > 0) {
            // There are symbols to be processed.  Post them.
            List<String> payload = new ArrayList<>();
            payload.addAll(uniqueSymbols);
            EventBus.instance.post(new MessageEvent(Type.ADD_SYMBOLS, payload));
        }

        // Finally, process the duplicates, if any.
        if (duplicates.size() > 0) {
            // There are duplicates.  Post a warning via status.
            List<String> payload = new ArrayList<>();
            payload.addAll(duplicates);
            EventBus.instance.post(new MessageEvent(Type.REPORT_DUPLICATE_SYMBOLS, payload));
        }
    }

    /** Implement the Subscriber interface to capture stock symbols to be added and removed via the event bus. */
    @Override public void onPost(final MessageEvent event) {
        // Add the entire collection of symbols in the event payload.
        try {
            switch (event.getType()) {
            case ADD_SYMBOLS:
                // Add zero or more stock symbols in order to detect subsequent attempt to create duplicate symbols.
                mSymbolList.addAll(event.getPayload());
                break;

            case REMOVE_SYMBOLS:
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
