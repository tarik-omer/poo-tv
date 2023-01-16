package observer_pattern;

import fileio.Notification;

import java.util.ArrayList;

public interface Subject {
    void notif(Notification message, ArrayList<String> properties);

    void attach(Observer s, String property);

    void detach(Observer s, String property);
}
