package bci.util.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Classification implements Serializable {

    protected List<Boolean> _returns = new ArrayList<Boolean>();
    protected User _parentUser;

    protected Classification(User parentUser) {
        _parentUser = parentUser;
    }

    protected Classification(User parentUser, List<Boolean> returns) {
        _parentUser = parentUser;
        _returns.addAll(returns);
    }

    public abstract int getClassification();

    public abstract void processReturns();

    public void registerReturn(boolean isOverDue) {
        _returns.add(isOverDue);
        processReturns();
    }

    protected void maintainLast(int n) {
        while (_returns.size() > n)
             _returns.remove(0);
    }

    protected boolean areLast(int n, boolean b) {
        if (_returns.size() < n) {
            return false;
        }

        for (int i = _returns.size() - n; i < _returns.size(); i++) {
            if (_returns.get(i) != b) {
                return false;
            }
        }
        return true;
    }


    protected void setClassification(Classification c) {
        _parentUser.setClassification(c);
    }
}
