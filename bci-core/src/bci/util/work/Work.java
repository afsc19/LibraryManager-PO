package bci.util.work;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import bci.exceptions.NotEnoughInventoryException;
import bci.util.user.User;
import bci.util.user.notifications.RequestNotification;
import bci.util.user.notifications.ReturnNotification;

public abstract class Work implements Serializable, Comparable<Work> {
    private int _wid;
    private int _copies;
    private int _stock;
    private String _title;
    private int _price;
    private Category _category;
    private Collection<User> _requestInterestedUsers = new HashSet<User>();
    private Collection<User> _returnInterestedUsers = new HashSet<User>();

    protected Work(int wid, String title, int copies, int price, Category category) {
        _wid = wid;
        _title = title;
        _copies = copies;
        _stock = copies;
        _price = price;
        _category = category;
    }

    protected abstract String getType();

    public abstract Collection<String> getCreatorStrings();

    public abstract Collection<Creator> getCreators();

    public int getWID() {
        return _wid;
    }

    public int getCopies() {
        return _copies;
    }

    public String getTitle() {
        return _title;
    }

    public Category getCategory() {
        return _category;
    }

    protected abstract String getCreatorString();

    public int getPrice() {
        return _price;
    }


    public void incrementStock() {
        _stock++;
        if (_stock==1)
            informReturnInterestedUsers();
    }

    public void decrementStock() {
        _stock--;
    }

    /**
     * Add a specified number of copies to the work.
     * 
     * @param value The number of copies.
     * @throws NotEnoughInventoryException When there aren't enough available copies to remove.
     */
    public void addCopies(int value) throws NotEnoughInventoryException {
        if (_stock < -value)
            throw new NotEnoughInventoryException();
        if (_stock == 0 && value > 0)
            informReturnInterestedUsers();
        _copies += value;
        _stock += value;
    }

    public void informReturnInterestedUsers() {
        for (User u : _returnInterestedUsers) {
            u.addNotification(new ReturnNotification(this));
        }
    }

    public void informRequestInterestedUsers() {
        for (User u : _requestInterestedUsers) {
            u.addNotification(new RequestNotification(this));
        }
    }

    // changed argument to user:User
    public void addRequestInterestedUser(User user) {
        _requestInterestedUsers.add(user);
    }

    // changed argument to user:User
    public void removeRequestInterestedUser(User user) {
        _requestInterestedUsers.remove(user);
    }

    // changed argument to user:User
    public void addReturnInterestedUser(User user) {
        _returnInterestedUsers.add(user);
    }

    public void removeReturnInterestedUser(User user) {
        _returnInterestedUsers.remove(user);
    }

    /**
     * Evaluate whether there are available copies to borrow or not.
     * 
     * @returns True if there are available copies, false otherwise.
     */
    public boolean inStock() {
        return _stock > 0;
    }


    @Override
    public String toString() {
        return _wid + " - " + _stock + " de " + _copies + " - " + getType() + " - " + _title + " - "
                + _price + " - " + _category + " - " + getCreatorString();
    }


    @Override
    public boolean equals(Object o) {
        if (o instanceof Work) {
            Work w = (Work) o;
            return _wid == w.getWID();
        }
        return false;
    }

    // To use with Creator#_works' TreeSet.
    @Override
    public int compareTo(Work other) {
        int comp = _title.compareToIgnoreCase(other.getTitle());
        if (comp != 0)
            return comp;
        return Integer.compare(_wid, other.getWID());
    }

}
