package bci.util;

import java.io.Serializable;
import bci.util.user.User;
import bci.util.work.Work;

public class Request implements Serializable {
    private User _user;
    private Work _work;
    private int _deadline;

    public Request(User user, Work work, int deadline){
        _user = user;
        _work = work;
        _deadline = deadline;
    }

    public User getUser(){
        return _user;
    }

    public Work getWork(){
        return _work;
    }

    /**
     * Evaluate whether the deadline is overdue.
     * 
     * @param date The current date.
     * @returns True if it's overdue, false otherwise.
     */
    public boolean isOverDue(int date) {
        return date > _deadline;
    }

    public int getDeadline() {
        return _deadline;
    }

}
