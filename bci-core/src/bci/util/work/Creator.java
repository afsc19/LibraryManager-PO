package bci.util.work;

import java.io.Serializable;
import java.util.TreeSet;
import java.util.Collection;
import java.util.Comparator;

public class Creator implements Serializable {
    private String _name;
    private Collection<Work> _works = new TreeSet<Work>();

    public Creator(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public void appendWork(Work newWork) {
        _works.add(newWork);
    }

    public void removeWork(Work oldWork) {
        _works.remove(oldWork);
    }

    public Collection<Work> getWorks() {
        return _works;
    }

    public boolean containsWorks() {
        return !_works.isEmpty();
    }

    @Override
    public String toString() {
        return _name;
    }


    @Override
    public boolean equals(Object o) {
        if (o instanceof Creator) {
            Creator c = (Creator) o;
            return _name.equals(c.getName());
        }
        return false;
    }

}
