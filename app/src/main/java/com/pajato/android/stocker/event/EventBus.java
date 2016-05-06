package com.pajato.android.stocker.event;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Provide a singleton and very simple event bus. The bus will allow subcribers to be registered and unregistered.
 * Message events posted to the bus will be presented to the subscribers.
 *
 * @author Paul Michael Reilly
 */
public enum EventBus {
    // The singleton and sole enumeration value.
    instance;

    public interface Subscriber {
        void onPost(MessageEvent event);
    }

    // Private constants.

    /** The logcat tag. */
    private static final String TAG = EventBus.class.getSimpleName();

    // Private instance variables.

    /** The list of subscribers to event messages. */
    private Set<Subscriber> mSubscribers = Collections.synchronizedSet(new HashSet<Subscriber>());

    // Public instance methods

    /** Determine if a given compment is a subscriber. */
    public boolean isRegistered(final Object component) {
        if (!(component instanceof Subscriber)) return false;
        return mSubscribers.contains((Subscriber) component);
    }

    /** Post an event to all subscribers. */
    public void post(final MessageEvent event) {
        for (Subscriber subscriber : mSubscribers) {
            subscriber.onPost(event);
        }
    }

    /** Register one or more subscribers. */
    public void register(final Subscriber... subscribers) {
        for (Subscriber subscriber : subscribers) {
            mSubscribers.add(subscriber);
        }
    }

    /** Clear out the current subscribers. */
    public void reset() {
        mSubscribers.clear();
    }

    /** Unregister one or more subscribers. */
    public void unregister(final Subscriber... subscribers) {
        for (Subscriber subscriber : subscribers) {
            mSubscribers.remove(subscriber);
        }
    }
}
