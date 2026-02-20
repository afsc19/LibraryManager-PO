package bci.util.user.notifications;

import bci.util.user.Notification;
import bci.util.work.Work;

public class RequestNotification extends Notification {
    public RequestNotification(Work work){
        super("REQUISIÇÃO: " + work.toString());
    }

}
