package com.pajato.android.stocker.event;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Provide a event object class for disseminating actions and arguments to display information about stocks, such as the
 * symbol, current price and the change from the previous price.
 *
 * @author Paul Michael Reilly
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public final class MessageEvent {

    /** Provide a set of action types. */
    public enum Type {
        ADD_SYMBOLS,
        REMOVE_SYMBOLS,
        REPORT_DUPLICATE_SYMBOLS,
        REPORT_IO_ERROR,
        REPORT_STATUS,
        REPORT_SYMBOL_ERROR;
    }

    // Private constants.

    /** The logger tag. */
    private static final String TAG = MessageEvent.class.getSimpleName();

    // Private instance variables.

    /** The action/event type . */
    private Type type;

    /** The event payload. */
    private List<String> payload;
}
