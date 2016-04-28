package com.pajato.android.stocker.stocklist;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pajato.android.stocker.R;
import com.pajato.android.stocker.event.MessageEvent;
import com.pajato.android.stocker.event.MessageEvent.Type;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

/**
 * Provide a basic adapter class for the stock list activity.
 *
 * @author Paul Michael Reilly
 */
public class StockListAdapter extends RecyclerView.Adapter<StockListAdapter.StockListViewHolder> {

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

    /** Handle one or more new stock entries. */
    @Subscribe public void onMessageEvent(final MessageEvent event) {
        if (event.getType() != Type.ADD) {
            // Ignore all other events.
            return;
        }

        // Create a place holder for each entry in the event and notify the adapter that the data set has changed,
        // albeit with no data yet.
        List<String> list = event.getPayload();
        for (String symbol : list) {
            // Create a default stock entry as a placeholder until the actual finance data is returned from Yahoo.
            mStockList.add(new StockModel(symbol, 0.0, 0.0));
        }
        notifyDataSetChanged();

        // Kick off a background task to get the initial stock data (bid price, change from the last price).
        GetStockQuoteTask task = new GetStockQuoteTask();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, list.toArray(new String[0]));
    }

    // Private classes

    /** The async task used to get stock quote data. */
    private class GetStockQuoteTask extends AsyncTask<String, Object, Map<String, Stock>> {

        /** Process the background task results in the foreground. */
        @Override protected void onPreExecute() {
            // Indicate that we are going to talk to Yahoo...
            String message = "Starting stock data fetch...";
            List<String> messageList = new ArrayList<>();
            messageList.add(message);
            MessageEvent event = new MessageEvent(Type.STATUS, messageList);
            EventBus.getDefault().post(event);
        }

        @Override protected Map<String, Stock> doInBackground(String... symbols) {
            Map<String, Stock> result = new HashMap<>();

            // Obtain the list of stocks in bulk.
            try {
                result = YahooFinance.get(symbols);
            } catch (IOException exc) {
                // Report on io issues by posting an appropriate event.
                List<String> messageList = new ArrayList<>();
                messageList.add(exc.getMessage());
                MessageEvent event = new MessageEvent(Type.IO_ERROR, messageList);
                EventBus.getDefault().post(event);
            }

            return result;
        }

        /** Process the background task results in the foreground. */
        @Override protected void onPostExecute(final Map<String, Stock> result) {
            // Walk the list of results to update the adapter model and notify it of the changes.  Collect any invalid
            // stock symbols for subsequent processing.
            List<String> badStockList = new ArrayList<>();
            for (String symbol : result.keySet()) {
                Stock stock = result.get(symbol);
                StockModel model = findStockBySymbol(symbol);
                if (model != null) {
                    // There is, as expected, a model for this stock symbol.  Detect an invalid stock using the price.
                     if (stock.getQuote().getPrice() == null) {
                        // Deal with an invalid stock symbol by collecting the symbols and popping up a snackbar after
                        // all the other stocks have been processed.
                        badStockList.add(symbol);
                    } else {
                         // Populate the model entry.
                         model.setPrice(stock.getQuote().getPrice().doubleValue());
                         model.setDelta(stock.getQuote().getChange().doubleValue());
                    }
                } else {
                    // Log but otherwise ignore stocks which have disappeared from the adapter model.
                    Log.w(TAG, String.format("Missing stock {%s}, presumed swiped away.", symbol));
                }
            }
            notifyDataSetChanged();

            // Process invalid stock symbols, if any, by posting a message to have the activity report on invalid
            // stocks.
            if (badStockList.size() > 0) {
                MessageEvent event = new MessageEvent(Type.SYMBOL_ERROR, badStockList);
                EventBus.getDefault().post(event);
            }
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
