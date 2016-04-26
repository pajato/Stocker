package com.pajato.android.stocker.stocklist;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Provide a model class for display information about stocks, such as the symbol, current price and the change from the
 * previous price.
 *
 * @author Paul Michael Reilly
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class StockModel {

    // Private constants.

    /** The logger tag. */
    private static final String TAG = StockModel.class.getSimpleName();

    // Private instance variables.

    /** The stock symbol. */
    private String symbol;

    /** The current price. */
    private double price;

    /** The change from the last price. */
    private double delta;
}
