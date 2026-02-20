package bci.util.rules;

import bci.util.Rule;
import bci.util.user.User;
import bci.util.work.Work;

public class MaxRequestsRule implements Rule {
    private int[] _maxRequests;
    public MaxRequestsRule(int[] maxRequests){
        _maxRequests = maxRequests;
    }
    public boolean canBorrow(User user, Work work, int[] inspection){
        return inspection[1] < _maxRequests[user.getClassification().getClassification()];
    }
}
