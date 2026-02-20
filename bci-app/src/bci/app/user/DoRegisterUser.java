package bci.app.user;

import bci.LibraryManager;
import bci.app.exceptions.UserRegistrationFailedException;
import bci.exceptions.InvalidUserFieldException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * 4.2.1. Register new user.
 */
class DoRegisterUser extends Command<LibraryManager> {

    DoRegisterUser(LibraryManager receiver) {
        super(Label.REGISTER_USER, receiver);
        addStringField("name", Prompt.userName());
        addStringField("email", Prompt.userEMail());
    }

    @Override
    protected final void execute() throws CommandException {
        String name = stringField("name");
        String email = stringField("email");

        try {
            _display.addLine(Message.registrationSuccessful(_receiver.registerUser(name, email)));
        } catch (InvalidUserFieldException e){
            throw new UserRegistrationFailedException(name, email);
        }
    }

}
