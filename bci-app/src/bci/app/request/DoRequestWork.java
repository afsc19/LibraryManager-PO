package bci.app.request;

import bci.LibraryManager;
import bci.app.exceptions.BorrowingRuleFailedException;
import bci.app.exceptions.NoSuchUserException;
import bci.app.exceptions.NoSuchWorkException;
import bci.exceptions.CanNotBorrowException;
import bci.exceptions.UserNotFoundException;
import bci.exceptions.WorkNotFoundException;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * 4.4.1. Request work.
 */
class DoRequestWork extends Command<LibraryManager> {

    DoRequestWork(LibraryManager receiver) {
        super(Label.REQUEST_WORK, receiver);
        addIntegerField("uid", bci.app.user.Prompt.userId());
        addIntegerField("wid", bci.app.work.Prompt.workId());
    }

    @Override
    protected final void execute() throws CommandException {
        int uid = integerField("uid");
        int wid = integerField("wid");
        try {
            int returnDay = _receiver.requestWork(uid, wid);
            _display.addLine(Message.workReturnDay(wid, returnDay));
        } catch (UserNotFoundException e) {
            throw new NoSuchUserException(uid);
        } catch (WorkNotFoundException e) {
            throw new NoSuchWorkException(wid);
        } catch (CanNotBorrowException e) {
            if (e.getFailedRule() != 3)
                throw new BorrowingRuleFailedException(uid, wid, e.getFailedRule());
            if (Form.requestString(Prompt.returnNotificationPreference()).equals("s")) {
                try {
                    _receiver.addReturnInterestedUser(uid, wid);
                } catch (UserNotFoundException | WorkNotFoundException e2) { // Should never happen at this point.
                    e2.printStackTrace();
                }
            }
        }
    }

}
