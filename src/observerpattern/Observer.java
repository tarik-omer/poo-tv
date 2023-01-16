package observerpattern;

import fileio.Notification;

public interface Observer {
    /**
     * Update observer with given message
     * @param message   message for the observer
     */
    void update(Notification message);
}
