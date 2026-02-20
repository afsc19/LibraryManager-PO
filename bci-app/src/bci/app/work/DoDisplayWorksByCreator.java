package bci.app.work;

import bci.LibraryManager;
import bci.app.exceptions.NoSuchCreatorException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import bci.exceptions.CreatorNotFoundException;

/**
 * 4.3.3. Display all works by a specific creator.
 */
class DoDisplayWorksByCreator extends Command<LibraryManager> {

    DoDisplayWorksByCreator(LibraryManager receiver) {
        super(Label.SHOW_WORKS_BY_CREATOR, receiver);
        addStringField("name", Prompt.creatorId());
    }

    @Override
    protected final void execute() throws CommandException {
        String name = stringField("name");
        try{
            _display.addAll(_receiver.showWorksByCreator(name));
        } catch (CreatorNotFoundException e) {
            throw new NoSuchCreatorException(name);
        }
    }

}
