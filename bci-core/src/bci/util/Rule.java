package bci.util;

import java.io.Serializable;
import bci.util.user.User;
import bci.util.work.Work;

public interface Rule extends Serializable {
    boolean canBorrow(User u, Work w, int[] inspection);
}
