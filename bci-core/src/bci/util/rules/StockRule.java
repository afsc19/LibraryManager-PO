package bci.util.rules;

import bci.util.Rule;
import bci.util.user.User;
import bci.util.work.Work;

public class StockRule implements Rule {
    public boolean canBorrow(User user, Work work, int[] inspection){
        return work.inStock();
    }
}
