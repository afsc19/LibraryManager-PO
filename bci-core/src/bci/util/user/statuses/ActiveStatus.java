package bci.util.user.statuses;

import bci.util.user.Status;

public class ActiveStatus implements Status {
    public ActiveStatus(){}

    public boolean isEligible(){
        return true;
    }

    public String toString(){
        return "ACTIVO";
    }
}
