package bci.app.user;

import bci.LibraryManager;
import bci.app.exceptions.NoSuchUserException;
import bci.exceptions.UserNotFoundException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * 4.2.2. Show specific user.
 */
class DoShowUser extends Command<LibraryManager> {

    DoShowUser(LibraryManager receiver) {
        super(Label.SHOW_USER, receiver);
        addIntegerField("uid", Prompt.userId());
    }

    @Override
    protected final void execute() throws CommandException {
        int uid = integerField("uid");
        try {
            _display.addLine(_receiver.showUser(uid));
        } catch (UserNotFoundException e) {
            throw new NoSuchUserException(uid);
        }
    }

}
