package com.pajato.android.stocker;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.pajato.android.stocker.stocklist.StockListAdapter;
import com.pajato.android.stocker.stocklist.StockModel;

import java.util.ArrayList;
import java.util.List;

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

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Private instance methods.

    /** Initialize the add symbol button using a Material Design floating action button. */
    private void initializeAddSymbolButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add new symbol action coming soon...", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            }
        });
    }

    /** Initialize the stock list with sample data. */
    private void initializeStockList() {
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        final List<StockModel> stockList = getSampleData();
        final StockListAdapter adapter = new StockListAdapter(stockList);

        // Set up the recycler view to hold the sample stock data.
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /** Initialize some sample data. */
    private List<StockModel> getSampleData() {
        List<StockModel> result = new ArrayList<>();
        result.add(new StockModel("YHOO", 37.29, -0.48));
        result.add(new StockModel("GOOG", 720.61, 0.32));
        result.add(new StockModel("AAPL", 104.90, -0.74));
        result.add(new StockModel("MSFT", 51.98, 0.40));

        return result;
    }
}
