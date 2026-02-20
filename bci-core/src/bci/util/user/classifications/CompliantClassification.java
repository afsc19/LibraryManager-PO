package bci.util.user.classifications;

import java.util.List;
import bci.util.user.Classification;
import bci.util.user.User;

public class CompliantClassification extends Classification {

    public CompliantClassification(User parentUser, List<Boolean> returns) {
        super(parentUser, returns);
    }


    public void processReturns() {
        if (areLast(1, true))
            _parentUser.setClassification(new NormalClassification(_parentUser, _returns));
        else
            maintainLast(5);
    }

    public int getClassification() {
        return 1;
    }

    @Override
    public String toString() {
        return "CUMPRIDOR";
    }
}
