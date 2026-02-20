package bci.util.rules;

import bci.util.Rule;
import bci.util.user.User;
import bci.util.work.Work;

public class DuplicateRequestRule implements Rule {

    public boolean canBorrow(User user, Work work, int[] inspection) {
        return inspection[0] == 0;
    }
}
