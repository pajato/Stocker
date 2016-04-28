package com.pajato.android.stocker.stocklist;

/**
 * Provide a singleton manager in which to wrap the stock list adapter.
 *
 * @author Paul Michael Reilly
 */
public enum StockListManager {
    // The singleton and sole enumeration value.
    instance;

    // Private constants.

    /** The logcat tag. */
    private static final String TAG = StockListManager.class.getSimpleName();

    // Private instance variables.

    /** The wrapped stock list adapter. */
    private StockListAdapter mAdapter = new StockListAdapter();

    // Public instance methods

    /** The adapter accessor. */
    public StockListAdapter getAdapter() {
        return mAdapter;
    }
}
