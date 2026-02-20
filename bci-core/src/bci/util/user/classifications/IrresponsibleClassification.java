package bci.util.user.classifications;

import java.util.List;
import bci.util.user.Classification;
import bci.util.user.User;

public class IrresponsibleClassification extends Classification {

    public IrresponsibleClassification(User parentUser, List<Boolean> returns) {
        super(parentUser, returns);
    }

    public void processReturns() {
        maintainLast(3);

        if (areLast(3, false))
            _parentUser.setClassification(new NormalClassification(_parentUser, _returns));
    }


    public int getClassification() {
        return 2;
    }

    @Override
    public String toString() {
        return "FALTOSO";
    }
}
