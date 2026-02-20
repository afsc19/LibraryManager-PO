package bci.util.work.categories;

import bci.util.work.Category;

public class SciTechCategory implements Category {

    public SciTechCategory(){}

    public boolean isAllowedToRequest(){
        return true;
    }

    public String getCodename(){
        return "SCITECH";
    }

    @Override
    public String toString(){
        return "Técnica e Científica";
    }
    
}
