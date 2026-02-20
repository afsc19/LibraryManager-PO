package bci.util.work;

import java.io.Serializable;

public interface Category extends Serializable{
    boolean isAllowedToRequest();
    String getCodename();
}
