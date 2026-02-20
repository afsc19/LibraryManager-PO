package bci.util.user.classifications;

import java.util.List;
import bci.util.user.Classification;
import bci.util.user.User;

public class NormalClassification extends Classification {


    public NormalClassification(User parentUser) {
        super(parentUser);
    }

    public NormalClassification(User parentUser, List<Boolean> returns) {
        super(parentUser, returns);
    }



    public void processReturns() {
        maintainLast(5);

        if (areLast(5, false))
            _parentUser.setClassification(new CompliantClassification(_parentUser, _returns));
        else if (areLast(3, true))
            _parentUser.setClassification(new IrresponsibleClassification(_parentUser, _returns));
    }


    public int getClassification() {
        return 0;
    }

    @Override
    public String toString() {
        return "NORMAL";
    }
}
