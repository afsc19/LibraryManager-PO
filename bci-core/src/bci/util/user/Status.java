package bci.util.user;

import java.io.Serializable;

public interface Status extends Serializable {

    /**
     * Evaluates whether a user is eligible to make requests, according to it's status.
     * 
     * @returns True if eligible, false otherwise.
     */
    boolean isEligible();
}
