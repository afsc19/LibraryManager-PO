package bci.util.user.notifications;

import bci.util.user.Notification;
import bci.util.work.Work;

public class ReturnNotification extends Notification {
    public ReturnNotification(Work work){
        super("DISPONIBILIDADE: " + work.toString());
    }
}
