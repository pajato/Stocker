package com.pajato.android.stocker.event;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Provide a event object class for disseminating actions and arguments.display information about stocks, such as the symbol, current price and the change from the
 * previous price.
 *
 * @author Paul Michael Reilly
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class MessageEvent {

    public enum Type {
        ADD, REMOVE, STATUS, IO_ERROR, SYMBOL_ERROR;
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
