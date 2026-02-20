package bci.app.request;

import bci.LibraryManager;
import bci.app.exceptions.NoSuchUserException;
import bci.app.exceptions.NoSuchWorkException;
import bci.app.exceptions.WorkNotBorrowedByUserException;
import bci.exceptions.NoFineToPayException;
import bci.exceptions.RequestNotFoundException;
import bci.exceptions.UserNotFoundException;
import bci.exceptions.WorkNotFoundException;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * 4.4.2. Return a work.
 */
class DoReturnWork extends Command<LibraryManager> {

    DoReturnWork(LibraryManager receiver) {
        super(Label.RETURN_WORK, receiver);
        addIntegerField("uid", bci.app.user.Prompt.userId());
        addIntegerField("wid", bci.app.work.Prompt.workId());
    }

    @Override
    protected final void execute() throws CommandException {
        int uid = integerField("uid");
        int wid = integerField("wid");
        try {
            int fine = _receiver.returnWork(uid, wid);
            if (fine > 0) {
                _display.popup(Message.showFine(uid, fine));
                if (Form.requestString(Prompt.finePaymentChoice()).equals("s"))
                    _receiver.payFine(uid);
            }
        } catch (UserNotFoundException e) {
            throw new NoSuchUserException(uid);
        } catch (WorkNotFoundException e) {
            throw new NoSuchWorkException(wid);
        } catch (RequestNotFoundException e) {
            throw new WorkNotBorrowedByUserException(wid, uid);
        } catch (NoFineToPayException e) { // Should never happen.
            e.printStackTrace();
        }
    }

}
