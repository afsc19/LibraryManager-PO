package bci.app.work;

import bci.LibraryManager;
import bci.app.exceptions.NoSuchWorkException;
import bci.exceptions.NotEnoughInventoryException;
import bci.exceptions.WorkNotFoundException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * 4.3.4. Change the number of exemplars of a work.
 */
class DoChangeWorkInventory extends Command<LibraryManager> {

    DoChangeWorkInventory(LibraryManager receiver) {
        super(Label.CHANGE_WORK_INVENTORY, receiver);
        addIntegerField("wid", Prompt.workId());
        addIntegerField("amount", Prompt.amountToUpdate());
    }

    @Override
    protected final void execute() throws CommandException {
        int wid = integerField("wid");
        int amount = integerField("amount");
        try {
            _receiver.changeWorkInventory(wid, amount);
        } catch (WorkNotFoundException e) {
            throw new NoSuchWorkException(wid);
        } catch (NotEnoughInventoryException e) {
            _display.addLine(Message.notEnoughInventory(wid, amount));
        }
    }

}
