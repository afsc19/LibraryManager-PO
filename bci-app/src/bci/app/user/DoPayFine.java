package bci.app.user;

import bci.LibraryManager;
import bci.app.exceptions.NoSuchUserException;
import bci.app.exceptions.UserIsActiveException;
import bci.exceptions.NoFineToPayException;
import bci.exceptions.UserNotFoundException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * 4.2.5. Settle a fine.
 */
class DoPayFine extends Command<LibraryManager> {

    DoPayFine(LibraryManager receiver) {
        super(Label.PAY_FINE, receiver);
        addIntegerField("uid", Prompt.userId());
    }

    @Override
    protected final void execute() throws CommandException {
        int uid = integerField("uid");
        try {
            _receiver.payFine(uid);
        } catch (UserNotFoundException e){
            throw new NoSuchUserException(uid);
        } catch (NoFineToPayException e){
            throw new UserIsActiveException(uid);
        }
    }

}
