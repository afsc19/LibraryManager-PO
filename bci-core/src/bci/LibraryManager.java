package bci;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import bci.exceptions.*;
import bci.util.RuleSet;

/**
 * The façade class.
 */
public class LibraryManager {

    /** The object doing all the actual work. */

    private int _defMaxRequests[] = new int[] {3, 5, 1};
    private int _defDeadlines[][] = new int[][] {{3, 8, 2}, {8, 15, 2}, {15, 30, 2}};
    private int _defDeadlinesCopies[] = new int[] {1, 5, -1};
    private int _defMaxWorkPrice = 25;
    private int _defFineAmmount = 5;

    private RuleSet _defaultRules = new RuleSet(_defMaxRequests, _defDeadlines,
            _defDeadlinesCopies, _defMaxWorkPrice, _defFineAmmount);


    private Library _library = new Library(_defaultRules);



    public LibraryManager() {
        // Nothing to add for now.
    }

    public boolean isModified() {
        return _library.isModified();
    }

    /**
     * Save the current library state in the associated file (the one last used for saving or
     * loading).
     * 
     * @throws MissingFileAssociationException If there is no associated file yet.
     * @throws IOException When there is a problem handling the file.
     */
    public void save() throws MissingFileAssociationException, IOException {
        if (_library.getFilename() == null)
            throw new MissingFileAssociationException();
        if (_library.isModified()) {
            try (ObjectOutputStream out = new ObjectOutputStream(
                    new BufferedOutputStream(new FileOutputStream(_library.getFilename())))) {
                out.writeObject(_library);
                _library.setModified(false);
            }
        }
    }

    /**
     * Save the current library state in a new file.
     * 
     * @throws IOException When there is a problem handling the file.
     */
    public void saveAs(String filename) throws IOException { // Removed
                                                             // MissingFileAssociationException for
                                                             // being unnecessary
        _library.setFilename(filename);
        try {
            save();
        } catch (MissingFileAssociationException e) {
            e.printStackTrace(); // Should never happen
        }
    }

    /**
     * Load the library state from a specified file.
     * 
     * @throws UnavailableFileException If the requested file is unavailable.
     */
    public void load(String filename) throws UnavailableFileException {
        try (ObjectInputStream in =
                new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
            _library = (Library) in.readObject();
            _library.setFilename(filename);
            _library.setModified(false);
        } catch (IOException | ClassNotFoundException e) {
            throw new UnavailableFileException(filename);
        }
    }

    /**
     * Read text input file and initializes the current library (which should be empty) with the
     * domain entities representeed in the import file.
     *
     * @param filename name of the text input file
     * @throws ImportFileException if some error happens during the processing of the import file.
     */
    public void importFile(String filename) throws ImportFileException {
        try {
            if (filename != null && !filename.isEmpty())
                _library.importFile(filename);
        } catch (IOException | UnrecognizedEntryException | UnrecognizedLineException e) {
            throw new ImportFileException(filename, e);
        }
    }


    public int getCurrentDate() {
        return _library.getCurrentDate();
    }

    /**
     * Advance the date by a specified number of days.
     * 
     * @param days The days to advance.
     * @throws InvalidDaysToAdvanceException If the input is not positive.
     */
    public void advanceDate(int days) throws InvalidDaysToAdvanceException {
        if (days <= 0)
            throw new InvalidDaysToAdvanceException();
        _library.advanceDate(days);
    }

    /**
     * Get a specific work.
     * 
     * @param wid The work ID.
     * @throws WorkNotFoundException If there is no work with a specified work ID.
     * @returns A String representation of the specified work .
     */
    public String showWork(int wid) throws WorkNotFoundException {
        return _library.showWork(wid);
    }

    /**
     * @see Library#showWorks()
     */
    public Collection<String> showWorks() {
        return _library.showWorks();
    }

    /**
     * Get all works from a specific creator.
     * 
     * @param name The name of the creator.
     * @throws CreatorNotFoundException If the creator was not found.
     * @returns A collection with String representations of each work from the creator.
     */
    public Collection<String> showWorksByCreator(String name) throws CreatorNotFoundException {
        Collection<String> res = _library.showWorksByCreator(name);
        if (res == null)
            throw new CreatorNotFoundException();
        return res;
    }

    /**
     * Get a specific user.
     * 
     * @param uid The user ID.
     * @throws UserNotFoundException If the user was not found.
     * @returns A String representation of the specified user.
     */
    public String showUser(int uid) throws UserNotFoundException {
        return _library.showUser(uid);
    }

    /**
     * @see Library#showUsers()
     */
    public Collection<String> showUsers() {
        return _library.showUsers();
    }


    /**
     * Register a new user.
     * 
     * @param name The user's name.
     * @param email The user's email.
     * @throws InvalidUserFieldException If the name or email are empty.
     * @returns The new user ID.
     */
    public int registerUser(String name, String email) throws InvalidUserFieldException {
        if (name.strip().isEmpty() || email.strip().isEmpty())
            throw new InvalidUserFieldException();
        return _library.registerUser(name, email);
    }


    /**
     * 
     * @see Library#showUserNotifications(int)
     *
     */
    public Collection<String> showUserNotifications(int uid) throws UserNotFoundException {
        return _library.showUserNotifications(uid);
    }

    /**
     * @see Library#payAllFine(int)
     */
    public void payFine(int uid) throws UserNotFoundException, NoFineToPayException {
        _library.payFine(uid);
    }




    /**
     * @see Library#changeWorkInventory(int, int)
     */
    public void changeWorkInventory(int wid, int amount)
            throws WorkNotFoundException, NotEnoughInventoryException {
        _library.changeWorkInventory(wid, amount);
    }

    public Collection<String> performSearch(String term) {
        return _library.searchWorks(term);
    }

    /**
     * @see Library#requestWork(int, int);
     */
    public int requestWork(int uid, int wid)
            throws UserNotFoundException, WorkNotFoundException, CanNotBorrowException {
        return _library.requestWork(uid, wid);
    }

    /**
     * @see Library#returnWork(int, int)
     */
    public int returnWork(int uid, int wid)
            throws UserNotFoundException, WorkNotFoundException, RequestNotFoundException {
        return _library.returnWork(uid, wid);
    }

    public void  addReturnInterestedUser(int uid, int wid)
            throws UserNotFoundException, WorkNotFoundException {
        _library.addReturnInterestedUser(uid, wid);
    }

    public void addRequestInsterestedUser(int uid, int wid)
            throws UserNotFoundException, WorkNotFoundException {
        _library.addRequestInterestedUser(uid, wid);
    }

    public void removeRequestInterestedUser(int uid, int wid)
            throws UserNotFoundException, WorkNotFoundException {
        _library.removeRequestInterestedUser(uid, wid);
    }

}
