package bci.app.user;

import bci.LibraryManager;
import bci.app.exceptions.NoSuchUserException;
import bci.exceptions.UserNotFoundException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * 4.2.3. Show notifications of a specific user.
 */
class DoShowUserNotifications extends Command<LibraryManager> {

    DoShowUserNotifications(LibraryManager receiver) {
        super(Label.SHOW_USER_NOTIFICATIONS, receiver);
        addIntegerField("uid", Prompt.userId());
    }

    @Override
    protected final void execute() throws CommandException {
        
        int uid = integerField("uid");
        try {
            _display.addAll(_receiver.showUserNotifications(uid));
        } catch (UserNotFoundException e) {
            throw new NoSuchUserException(uid);
        }
    }

}
