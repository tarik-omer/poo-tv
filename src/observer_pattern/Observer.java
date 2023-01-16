package observer_pattern;

import fileio.Notification;

public interface Observer {
    void update(Notification message);
}
