package com.pajato.android.stocker.stocklist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pajato.android.stocker.R;

import java.util.List;

/**
 * Provide a basic adapter class for the stock list activity.
 *
 * @author Paul Michael Reilly
 */
public class StockListAdapter extends RecyclerView.Adapter<StockListAdapter.StockListViewHolder> {

    private List<StockModel> stockList;

    public class StockListViewHolder extends RecyclerView.ViewHolder {
        public TextView symbol, delta, price;

        public StockListViewHolder(View view) {
            super(view);
            symbol = (TextView) view.findViewById(R.id.symbol);
            price = (TextView) view.findViewById(R.id.price);
            delta = (TextView) view.findViewById(R.id.delta);
        }
    }

    public StockListAdapter(List<StockModel> stockList) {
        this.stockList = stockList;
    }

    @Override public StockListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_list_row, parent, false);

        return new StockListViewHolder(itemView);
    }

    @Override public void onBindViewHolder(StockListViewHolder holder, int position) {
        StockModel model = stockList.get(position);
        holder.symbol.setText(model.getSymbol());
        holder.price.setText(String.format("%10.2f", model.getPrice()));
        holder.delta.setText(String.format("%+10.2f", model.getDelta()));
    }

    @Override public int getItemCount() {
        return stockList.size();
    }
}
