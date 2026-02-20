package bci.util.user;

import java.io.Serializable;
import bci.util.user.classifications.NormalClassification;
import bci.util.user.statuses.ActiveStatus;
import java.util.ArrayList;
import java.util.Collection;

public class User implements Serializable {
    private int _uid;
    private String _name;
    private String _email;
    private Status _status = new ActiveStatus();
    private Classification _classification = new NormalClassification(this);
    // private Map<Integer, Integer> _fine = new HashMap<Integer, Integer>(); // = empty
    private int _fine = 0;
    private Collection<Notification> _notifications = new ArrayList<Notification>();

    public User(int uid, String name, String email) {
        _uid = uid;
        _name = name;
        _email = email;
    }

    public int getUID() {
        return _uid;
    }

    public String getName() {
        return _name;
    }

    public Status getStatus() {
        return _status;
    }

    public void setStatus(Status status) {
        _status = status;
    }

    public Classification getClassification() {
        return _classification;
    }

    public void setClassification(Classification classification) {
        _classification = classification;
    }

    /**
     * Add a fine related to a work request.
     * 
     * @param value The value of the fine, in EUR.
     */
    public void addFine(int value) {
        _fine += value;
    }

    /**
     * Pay the fines related to a work request.
     */
    public void payFine() {
        _fine = 0;
    }

    /**
     * Get the fine associated to the user.
     * 
     * @returns The fine in EUR
     */
    public int getFine() {
        return _fine;
    }



    /**
     * Get all notifications targeting the user.
     * 
     * @returns A String representation of all notifications.
     */
    public Collection<String> showNotifications() {
        Collection<String> out = _notifications.stream().map(Notification::getMessage).toList();
        _notifications.clear();
        return out;
    }

    public void addNotification(Notification noti) {
        _notifications.add(noti);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof User) {
            User u = (User) o;
            return _uid == u.getUID();
        }
        return false;
    }

    @Override
    public String toString() {
        String toShow =
                _uid + " - " + _name + " - " + _email + " - " + _classification + " - " + _status;
        if (_status.isEligible()) {
            return toShow;
        }
        return toShow + " - EUR " + _fine;
    }
}
