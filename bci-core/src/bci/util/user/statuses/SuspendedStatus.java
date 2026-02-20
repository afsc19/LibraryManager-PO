package bci.util.user.statuses;

import bci.util.user.Status;

public class SuspendedStatus implements Status {
    public SuspendedStatus(){}
    
    public boolean isEligible(){
        return false;
    }

    @Override
    public String toString(){
        return "SUSPENSO";
    }
}
