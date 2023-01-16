package observerpattern;

import fileio.Notification;

import java.util.ArrayList;

public interface Subject {
    /**
     * Notify all observers that respect given properties with the provided message
     * @param message       message for attached observers
     * @param properties    properties to be respected
     */
    void notif(Notification message, ArrayList<String> properties);

    /**
     * Attach observer to users that need to be notified
     * @param s         observer to be attached
     * @param property  property to be categorized by / property to be attached to
     */
    void attach(Observer s, String property);

    /**
     * Detach observer from notified observers
     * @param s         observer to be detached
     * @param property  property to be detached from
     */
    void detach(Observer s, String property);
}
