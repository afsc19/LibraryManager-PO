package bci.app.main;

import java.io.IOException;

import bci.LibraryManager;
import bci.exceptions.MissingFileAssociationException;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;

/**
 * §4.1.1 Open and load files.
 */
class DoSaveFile extends Command<LibraryManager> {

    DoSaveFile(LibraryManager receiver) {
        super(Label.SAVE_FILE, receiver);
    }

    @Override
    protected final void execute() {
        try {
            try {
                _receiver.save();
            } catch (MissingFileAssociationException e) {
                _receiver.saveAs(Form.requestString(Prompt.newSaveAs()));
            }
        } catch (IOException e) {
            _display.popup(e.getMessage());
        }
    }

}
