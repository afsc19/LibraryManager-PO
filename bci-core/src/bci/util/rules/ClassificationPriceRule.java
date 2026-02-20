package bci.util.rules;

import bci.util.Rule;
import bci.util.user.User;
import bci.util.work.Work;

public class ClassificationPriceRule implements Rule {

    private int _maxWorkPrice;

    public ClassificationPriceRule(int maxWorkPrice){
        _maxWorkPrice = maxWorkPrice;
    }


    public boolean canBorrow(User user, Work work, int[] inspection){
        return user.getClassification().getClassification() == 2 || work.getPrice() <= _maxWorkPrice;
    }
}
