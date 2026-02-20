package bci.util.work.works;

import bci.util.work.Work;
import bci.util.work.Creator;
import java.util.ArrayList;
import java.util.Collection;
import bci.util.work.Category;

public class DVD extends Work {
    private String _IGAC;
    private Creator _creator;

    public DVD(int wid, String title, int copies, int price, Category category, Creator creator,
            String IGAC) {
        super(wid, title, copies, price, category);
        _creator = creator;
        _IGAC = IGAC;
    }

    public String getType() {
        return "DVD";
    }

    @Override
    public String toString() {
        return super.toString() + " - " + _IGAC;
    }

    public String getCreatorString(){
        return _creator.toString();
    }

    public Collection<String> getCreatorStrings(){
        return getCreators().stream().map(Creator::toString).toList();
    }

    public Collection<Creator> getCreators(){
        Collection<Creator> a = new ArrayList<Creator>();
        a.add(_creator);
        return a;
    }

}
