package bci.util.work.categories;

import bci.util.work.Category;

public class FictionCategory implements Category{

    public FictionCategory(){}

    public boolean isAllowedToRequest(){
        return true;
    }

    public String getCodename(){
        return "FICTION";
    }

    @Override
    public String toString(){
        return "Ficção";
    }

    
}
