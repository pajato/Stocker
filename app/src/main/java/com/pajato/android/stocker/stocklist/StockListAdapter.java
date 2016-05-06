package com.pajato.android.stocker.stocklist;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pajato.android.stocker.R;
import com.pajato.android.stocker.event.EventBus;
import com.pajato.android.stocker.event.MessageEvent;
import com.pajato.android.stocker.event.MessageEvent.Type;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provide a basic adapter class for the stock list activity.
 *
 * @author Paul Michael Reilly
 */
public class StockListAdapter extends RecyclerView.Adapter<StockListAdapter.StockListViewHolder>
    implements EventBus.Subscriber {

    // Private class constants.

    /** The logcat tag. */
    private static final String TAG = StockListAdapter.class.getSimpleName();

    /** The list of stocks being displayed. */
    private List<StockModel> mStockList = new ArrayList<>();

    public class StockListViewHolder extends RecyclerView.ViewHolder {
        public TextView symbol, delta, price;

        public StockListViewHolder(View view) {
            super(view);
            symbol = (TextView) view.findViewById(R.id.symbol);
            price = (TextView) view.findViewById(R.id.price);
            delta = (TextView) view.findViewById(R.id.delta);
        }
    }

    @Override public StockListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_list_row, parent, false);

        return new StockListViewHolder(itemView);
    }

    @Override public void onBindViewHolder(StockListViewHolder holder, int position) {
        StockModel model = mStockList.get(position);
        holder.symbol.setText(model.getSymbol());
        holder.price.setText(String.format("%10.2f", model.getPrice()));
        holder.delta.setText(String.format("%+10.2f", model.getDelta()));
    }

    @Override public int getItemCount() {
        return mStockList.size();
    }

    /** Implement the subscriber interface to Handle one or more new stock entries. */
    @Override public void onPost(final MessageEvent event) {
        // Case on the event action type to process symbol addition and removal.
        List<String> list = event.getPayload();
        List<String> updateList = new ArrayList<>();
        switch (event.getType()) {
        case ADD_SYMBOLS:
            // Create a place holder for each entry in the event and notify the adapter that the data set has changed,
            // albeit with no data yet.
            for (String symbol : list) {
                // Filter out symbols already in the model to create new entries as a placeholder (with empty data)
                // until the next update arrives from Yahoo.
                if (findStockBySymbol(symbol) == null) {
                    updateList.add(symbol);
                    mStockList.add(new StockModel(symbol, 0.0, 0.0));
                }
            }

            // If there has been new entries added, force an update to the recycler view and fetch real data using a
            // background task..
            if (updateList.size() > 0) {
                notifyDataSetChanged();
                GetStockQuoteTask task = new GetStockQuoteTask();
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, updateList.toArray(new String[0]));
            }
            break;

        case REMOVE_SYMBOLS:
            // Quietly remove the given symbols and update the data model.
            boolean update = false;
            for (String symbol : list) {
                StockModel model = findStockBySymbol(symbol);
                update = mStockList.remove(model);
            }
            if (update) notifyDataSetChanged();
            break;

        default:
            // Ignore all other actions as they are handled elsewhere.
            break;
        }
    }

    // Private classes

    /** The async task used to get stock quote data. */
    private class GetStockQuoteTask extends AsyncTask<String, Object, Map<String, Stock>> {

        /** The saved error message event; non-null when errors have occurred. */
        MessageEvent mErrorEvent = null;

        @Override protected Map<String, Stock> doInBackground(String... symbols) {
            Map<String, Stock> result = new HashMap<>();

            // Obtain the list of stocks in bulk.
            try {
                // Obtain the result and determine if any invalid symbols were detected.
                result = YahooFinance.get(symbols);
                List<String> invalidSymbolList = new ArrayList<>();
                for (String symbol : symbols) {
                    // Collect symbols for which no data has been returned per the YahooFinance API documentation and
                    // for which the returned stock name is null.  The latter are also pruned from the return value.
                    Stock stock = result.get(symbol);
                    if (stock == null || stock.getName() == null) invalidSymbolList.add(symbol);
                    if (stock != null && stock.getName() == null) result.remove(symbol);
                }
                if (invalidSymbolList.size() > 0) {
                    mErrorEvent = new MessageEvent(Type.REPORT_SYMBOL_ERROR, invalidSymbolList);
                }
            } catch (IOException exc) {
                // Report on io issues by posting an appropriate event.
                List<String> messageList = new ArrayList<>();
                messageList.add(exc.getMessage());
                mErrorEvent = new MessageEvent(Type.REPORT_IO_ERROR, messageList);
            }

            return result;
        }

        /** Process the background task result in the foreground. */
        @Override protected void onPostExecute(final Map<String, Stock> result) {
            // Post any errors that might have occurred occurred while fetching stock data and walk the list of results
            // to update the adapter model and notify it of the changes.
            if (mErrorEvent != null) EventBus.instance.post(mErrorEvent);
            boolean hasUpdates = false;
            for (String symbol : result.keySet()) {
                Stock stock = result.get(symbol);
                StockModel model = findStockBySymbol(symbol);
                if (model != null) {
                    // Populate the model entry.
                    model.setPrice(stock.getQuote().getPrice().doubleValue());
                    model.setDelta(stock.getQuote().getChange().doubleValue());
                    hasUpdates = true;
                } else {
                    // Log but otherwise ignore stocks which have disappeared from the adapter model.  They have
                    // probably been removed.
                    Log.w(TAG, String.format("Missing stock {%s}, presumed swiped away.", symbol));
                }
            }
            if (hasUpdates) notifyDataSetChanged();
        }
    }

    /** Find a stock model entry by symbol. */
    private StockModel findStockBySymbol(final String symbol) {
        StockModel result = null;
        for (StockModel model : mStockList) {
            if (symbol.equals(model.getSymbol())) {
                result = model;
                break;
            }
        }

        return result;
    }
}
