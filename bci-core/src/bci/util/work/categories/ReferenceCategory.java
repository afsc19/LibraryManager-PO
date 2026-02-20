package bci.util.work.categories;

import bci.util.work.Category;

public class ReferenceCategory implements Category {

    public ReferenceCategory(){}

    public boolean isAllowedToRequest(){
        return false;
    }

    public String getCodename(){
        return "REFERENCE";
    }

    @Override
    public String toString(){
        return "Referência";
    }
}
