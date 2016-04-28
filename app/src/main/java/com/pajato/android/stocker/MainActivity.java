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

import com.pajato.android.stocker.event.MessageEvent;
import com.pajato.android.stocker.event.MessageEvent.Type;
import com.pajato.android.stocker.stockadd.InputCallbackHandler;
import com.pajato.android.stocker.stockadd.NewSymbolHandler;
import com.pajato.android.stocker.stocklist.StockListManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Provide a main activity to show the basic stock information for the set of stocks, including symbol, current price
 * and most recent price change.
 *
 * @author Paul Michael Reilly
 */
public class MainActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        initializeAddSymbolButton();
        initializeStockList();
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /** Register the components which will be using the event bus. */
    @Override public void onStart() {
        super.onStart();
        // Due to a bug or questionable behavior from Greenrobot, ensure that the given classes are not already
        // registered before attempting to register them.  The failure to do this breaks the device tests.
        Object[] components = getComponents();
        for (Object component : components) {
            if (!EventBus.getDefault().isRegistered(component)) {
                // All clear, register the class to get posted events.
                EventBus.getDefault().register(component);
            }
        }

        // Setup to view a few stocks.
        addInitialStocks();
    }

    /** Unregister the components that have been using the event bus. */
    @Override public void onStop() {
        EventBus.getDefault().unregister(InputCallbackHandler.instance);
        super.onStop();
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
    @Subscribe public void onMessageEvent(final MessageEvent event) {
        // Handle status and error events by hanging a snackbar message off the FAB button.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        String message = "some default";
        switch (event.getType()) {

        case STATUS:
        case IO_ERROR:
            // Display the status/error message supplied in the one and only argument entry.
            message = event.getPayload().get(0);
            break;

        case SYMBOL_ERROR:
            // Show the symbols for which Yahoo has no information.
            String format = getResources().getString(R.string.invalid_symbol_format);
            String plural = event.getPayload().size() > 0 ? "s" : "";
            String list = TextUtils.join(",", event.getPayload());
            message = String.format(format, plural, list);
            break;

        default:
            // Ignore other event types.
            break;
        }
        Snackbar.make(fab, message, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    // Private instance methods.

    /** Use a procedural abstraction to collect the components using the event bus. */
    private Object[] getComponents() {
        return new Object[] {
            this,
            InputCallbackHandler.instance,
            StockListManager.instance.getAdapter()
        };
    }

    /** Initialize the add symbol button using a Material Design floating action button. */
    private void initializeAddSymbolButton() {
        // Obtain the FAB and set the button handler to invoke the symbol search dialog.  Also ensure that the dialog
        // input callback handler is registered with the event bus to track stock symbols as they are added and removed.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(NewSymbolHandler.instance);
    }

    /** Initialize the stock list diplay with some high tech stocks. */
    private void initializeStockList() {
        // Set up the recycler view to hold the sample stock data.
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(StockListManager.instance.getAdapter());
    }

    /** Set up to view some initial stocks. */
    private void addInitialStocks() {
        // Post the initial set of stocks to use.
        List<String> payload = new ArrayList<>();
        payload.add("AAPL");
        payload.add("GOOG");
        payload.add("MSFT");
        payload.add("YHOO");
        EventBus.getDefault().post(new MessageEvent(Type.ADD, payload));
    }

}
