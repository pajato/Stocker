package com.pajato.android.stocker.stockadd;

import android.text.InputType;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.pajato.android.stocker.R;

/**
 * Provide a singleton handler to process a FAB button click and post a dialog allowing the User to specify a new stock
 * symbol.
 *
 * @author Paul Michael Reilly
 */
public enum NewSymbolHandler implements View.OnClickListener {
    // The singleton and sole enumeration value.
    instance;

    // Private constants.

    /** The logcat tag. */
    private static final String TAG = NewSymbolHandler.class.getSimpleName();

    /** Handle the button click by invoking a dialog to capture the new stock symbol. */
    @Override public void onClick(final View view) {
        // Create the dialog for specifying the new stock symbol and present it to the User.
        InputCallbackHandler.instance.setView(view);
        new MaterialDialog.Builder(view.getContext())
            .title(R.string.symbol_search_title)
            .content(R.string.symbol_search_text)
            .inputType(InputType.TYPE_CLASS_TEXT)
            .input(R.string.symbol_search_hint, R.string.symbol_search_prefill, InputCallbackHandler.instance)
            .show();
    }
}
