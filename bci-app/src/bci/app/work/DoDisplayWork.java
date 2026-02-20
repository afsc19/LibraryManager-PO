package bci.app.work;

import bci.LibraryManager;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import bci.app.exceptions.NoSuchWorkException;
import bci.exceptions.WorkNotFoundException;

/**
 * 4.3.1. Display work.
 */
class DoDisplayWork extends Command<LibraryManager> {

    DoDisplayWork(LibraryManager receiver) {
        super(Label.SHOW_WORK, receiver);
        addIntegerField("wid", Prompt.workId());
    }

    @Override
    protected final void execute() throws CommandException {
        int wid = integerField("wid");
        try {
            _display.addLine(_receiver.showWork(wid));
        } catch (WorkNotFoundException e){
            throw new NoSuchWorkException(wid);
        }
    }

}
