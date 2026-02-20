package bci.util.rules;

import bci.util.Rule;
import bci.util.user.User;
import bci.util.work.Work;

public class AllowedCategoryRule implements Rule {
    public boolean canBorrow(User user, Work work, int[] inspection){
        return work.getCategory().isAllowedToRequest();
    }
}
