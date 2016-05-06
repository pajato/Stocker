package com.pajato.android.stocker;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.pajato.android.stocker.event.EventBus;
import com.pajato.android.stocker.event.MessageEvent;
import com.pajato.android.stocker.event.MessageEvent.Type;
import com.pajato.android.stocker.stockadd.InputCallbackHandler;
import com.pajato.android.stocker.stockadd.NewSymbolHandler;
import com.pajato.android.stocker.stocklist.StockListManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Provide a main activity to show the basic stock information for the set of stocks, including symbol, current price
 * and most recent price change.
 *
 * @author Paul Michael Reilly
 */
public final class MainActivity extends AppCompatActivity implements EventBus.Subscriber {

    @Override protected void onCreate(Bundle savedInstanceState) {
        // Create the default UI.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        // Initialize the event bus and the app features.
        EventBus.instance.reset();
        EventBus.instance.register(this);
        initializeStockAdd();
        initializeStockList();
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /** Place holder for handling Learn More, Help and Settings menu operations. */
    @Override public boolean onOptionsItemSelected(MenuItem item) {
        // Default support from AS is for a Settings menu item that does nothing.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /** Provide a handler for displaying status and error messages posted to the event bus. */
    @Override public void onPost(final MessageEvent event) {
        // Handle status and error events by hanging a snackbar message off the FAB button.
        String message = null;
        List<String> payload = event.getPayload();
        switch (event.getType()) {

        case REPORT_DUPLICATE_SYMBOLS:
            // Display a snackbar message showing the duplicate symbols.
            message = getMessage(R.string.format_duplicate_symbol, payload);
            break;

        case REPORT_STATUS:
        case REPORT_IO_ERROR:
            // Display the status/error message supplied in the one and only argument entry.
            message = event.getPayload().get(0);
            break;

        case REPORT_SYMBOL_ERROR:
            // Show the symbols for which Yahoo has no information.
            message = getMessage(R.string.format_invalid_symbol, payload);
            break;

        default:
            // Ignore other event types.
            break;
        }

        // Process a message, if one exists, by displaying it in a snackbar.
        if (message != null) {
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            Snackbar.make(fab, message, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    /** Provide the default event bus implementation in support of testing. */
    public EventBus getEventBus() {
        return EventBus.instance;
    }

    // Private instance methods.

    /** Set up to view some initial stocks. */
    private void addInitialStocks() {
        // Post the initial set of stocks to use.
        List<String> payload = new ArrayList<>();
        payload.add("AAPL");
        payload.add("GOOG");
        payload.add("MSFT");
        payload.add("YHOO");
        EventBus.instance.post(new MessageEvent(Type.ADD_SYMBOLS, payload));
    }

    /** Obtain a message string to display in a snackbar. */
    private String getMessage(final int baseId, final List<String> payload) {
        // Plug the payload into a message format string as a sorted list and return the result.
        String format = getResources().getString(baseId);
        String plural = payload.size() > 1 ? "s" : "";
        Collections.sort(payload);
        String list = TextUtils.join(", ", payload);
        return String.format(format, plural, list);
    }

    /** Initialize the stock add feature. */
    private void initializeStockAdd() {
        // Register the event bus subscribers, obtain the FAB and set the button handler to invoke the symbol search
        // dialog.
        EventBus.instance.register(InputCallbackHandler.instance);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(NewSymbolHandler.instance);
    }

    /** Initialize the stock list feature. */
    private void initializeStockList() {
        // Register the event bus subscribers, set up the recycler view to hold the sample stock data and add some
        // initial stocks.
        EventBus.instance.register(StockListManager.instance.getAdapter());
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(StockListManager.instance.getAdapter());
        addInitialStocks();
    }

}
