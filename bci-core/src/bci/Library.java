package bci;

import java.io.IOException;
import java.io.Serializable;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collection;
import java.util.Comparator;
import bci.util.RuleSet;
import bci.util.work.Work;
import bci.util.user.User;
import bci.util.user.statuses.ActiveStatus;
import bci.util.user.statuses.SuspendedStatus;
import bci.util.work.Creator;
import bci.util.Request;
import bci.util.work.Category;
import bci.util.work.categories.*;
import bci.util.work.works.*;
import bci.exceptions.CanNotBorrowException;
import bci.exceptions.NoFineToPayException;
import bci.exceptions.NotEnoughInventoryException;
import bci.exceptions.RequestNotFoundException;
import bci.exceptions.UnrecognizedEntryException;
import bci.exceptions.UnrecognizedLineException;
import bci.exceptions.UserNotFoundException;
import bci.exceptions.WorkNotFoundException;

/** Class that represents the library as a whole. */
class Library implements Serializable {

    /**
     * The rules that define request restrictions.
     */
    private RuleSet _rules;

    /**
     * The library's registered users.
     */
    private Collection<User> _users = new ArrayList<User>();

    /**
     * The library's collection of works.
     */
    private Collection<Work> _works = new ArrayList<Work>();

    /**
     * The library's creators.
     */
    private Collection<Creator> _creators = new HashSet<Creator>();

    /**
     * The library's active requests.
     */
    private Collection<Request> _requests = new ArrayList<Request>();

    /**
     * The current date, in days, since the creation of the library.
     */
    private int _currentDate = 1;

    /**
     * The next user ID to be assigned.
     */
    private int nextUID = 1;

    /**
     * The next work ID to be assigned.
     */
    private int nextWID = 1;

    private boolean _modified = false;

    private String _filename = null;


    @java.io.Serial
    private static final long serialVersionUID = 202507171003L;

    public Library(RuleSet rules) {
        _rules = rules;
    }

    public int getCurrentDate() {
        return _currentDate;
    }

    public boolean isModified() {
        return _modified;
    }

    public void setModified(boolean modified) {
        _modified = modified;
    }

    public String getFilename() {
        return _filename;
    }

    public void setFilename(String filename) {
        _filename = filename;
    }

    /**
     * Request a work from the library.
     * 
     * @param uid The ID of the requesting user.
     * @param wid The ID of the requested work.
     * @throws UserNotFoundException When the requesting user is not found.
     * @throws WorkNotFoundException When the requested work is not found.
     * @throws CanNotBorrowException When the work cannot be borrowed to that user.
     * @returns The number of days until the user pays a fine for not returning the requested book.
     */
    public int requestWork(int uid, int wid)
            throws UserNotFoundException, WorkNotFoundException, CanNotBorrowException {
        User u = getUserByUID(uid);
        Work w = getWorkByWID(wid);

        int result = _rules.canBorrow(u, w, _requests);
        if (result != 0)
            throw new CanNotBorrowException(result);

        // Borrow
        _modified = true;
        int deadline = _rules.getDeadline(_currentDate, w.getCopies(), u.getClassification());
        _requests.add(new Request(u, w, deadline));
        w.decrementStock();
        w.removeReturnInterestedUser(u);
        w.informRequestInterestedUsers();
        return deadline;
    }

    /**
     * Return a work to the library.
     * 
     * @param uid The ID of the requesting user.
     * @param wid The ID of the requested work.
     * @throws UserNotFoundException When no user is found.
     * @throws WorkNotFoundException When no work is found.
     * @throws RequestNotFoundException When no request matching the specifications is found.
     * @returns The ammount of the fine, if aplicable.
     */
    public int returnWork(int uid, int wid)
            throws UserNotFoundException, WorkNotFoundException, RequestNotFoundException {
        User u = getUserByUID(uid);
        Work w = getWorkByWID(wid);
        Request r = findRequest(u, w);

        int fine = _rules.calculateFine(_currentDate, r.getDeadline());
        u.addFine(fine);
        u.getClassification().registerReturn(fine > 0);
        _requests.remove(findRequest(u, w));
        w.incrementStock();
        return u.getFine();
    }


    /**
     * Finds a request.
     * 
     * @param uid The ID of the requesting user.
     * @param wid The ID of the requested work.
     * @throws RequestNotFoundException When no request is found.
     */
    private Request findRequest(User u, Work w) throws RequestNotFoundException {
        for (Request r : _requests)
            if (r.getUser() == u && r.getWork() == w)
                return r;
        throw new RequestNotFoundException();
    }



    /**
     * Read the text input file at the beginning of the program and populates the instances of the
     * various possible types (books, DVDs, users).
     *
     * @param filename The name of the file to load.
     * @throws UnrecognizedEntryException If an entry is invalid.
     * @throws UnrecognizedLineException If an invalid line is found.
     * @throws IOException When there is a problem opening the file.
     */
    void importFile(String filename)
            throws UnrecognizedEntryException, UnrecognizedLineException, IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(":");
                if (fields.length == 7) {
                    parseNewWork(fields);
                } else if (fields.length == 3 && fields[0].trim().equals("USER")) {
                    parseNewUser(fields);
                } else {
                    throw new UnrecognizedLineException();
                }
            }
        }
        _modified = true;
    }

    /**
     * Parse and create a new User from the ImportFile fields.
     * 
     * @param fields The fields from the imported file.
     * @throws UnrecognizedEntryException When an entry is invalid.
     */
    private void parseNewUser(String fields[]) throws UnrecognizedEntryException {
        // Prepare name
        String name = fields[1];
        if (name.isEmpty())
            throw new UnrecognizedEntryException("name");

        // Prepare email
        String email = fields[2].trim();
        if (email.isEmpty())
            throw new UnrecognizedEntryException("email");

        registerUser(name, email);
    }

    /**
     * Parse and create a new Work from the ImportFile fields.
     * 
     * @param fields The fields from the imported file.
     * @throws UnrecognizedEntryException When an entry is invalid.
     */
    private void parseNewWork(String fields[]) throws UnrecognizedEntryException {
        // Prepare title
        String title = fields[1];
        if (title.isEmpty())
            throw new UnrecognizedEntryException("title");

        // Prepare price
        int price = parseInt(fields[3], "price");
        if (price < 0)
            throw new UnrecognizedEntryException("price");

        // Prepare copies
        int copies = parseInt(fields[6].trim(), "copies");
        if (copies < 0)
            throw new UnrecognizedEntryException("copies");

        // Prepare Category
        Category category = parseCategory(fields[4]);
        if (category == null)
            throw new UnrecognizedEntryException("category");

        Work work = null;

        if (fields[0].trim().equals("DVD")) {
            // Prepare Creator
            if (fields[2].trim().isEmpty())
                throw new UnrecognizedEntryException("creator");
            Creator creator = searchAddCreator(fields[2], true);

            // Prepare IGAC
            String IGAC = fields[5];

            // Create the new DVD.
            work = new DVD(nextWID++, title, copies, price, category, creator, IGAC);

            creator.appendWork(work);

        } else if (fields[0].trim().equals("BOOK")) {
            // Prepare Creators
            String names[] = fields[2].split(",");
            if (names.length == 0 || names[0].trim().isEmpty())
                throw new UnrecognizedEntryException("creators");
            Collection<Creator> creators = new ArrayList<Creator>();
            for (int i = 0; i < names.length; i++)
                creators.add(searchAddCreator(names[i], true));

            // Prepare ISBN
            String ISBN = fields[5];
            if (ISBN.length() != 10 && ISBN.length() != 13)
                throw new UnrecognizedEntryException("isbn");

            // Create the new Book.
            work = new Book(nextWID++, title, copies, price, category, creators, ISBN);

            for (Creator c : creators)
                c.appendWork(work);
        } else
            throw new UnrecognizedEntryException("worktype");

        // Add the created work to the library
        _works.add(work);

    }


    /**
     * Convert a string to an integer. This is used for simplification and organization of
     * parseNewWork.
     * 
     * @param number The string containing the integer.
     * @param entrySpecification The entrySpecification for the UnrecognizedEntryException's throw.
     * @throws UnrecognizedEntryException In case the convertion fails.
     * @returns The converted integer.
     */
    private int parseInt(String number, String entrySpecification)
            throws UnrecognizedEntryException {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new UnrecognizedEntryException(entrySpecification, e);
        }
    }

    /**
     * Searches and returns a Creator by it's name. Optionally create and add a new Creator in case
     * no results are found.
     * 
     * @param name The creator's name.
     * @param addCreator Wether to create and add a new Creator if needed.
     * @returns A Creator (if found or newly created) or null if not found.
     */
    private Creator searchAddCreator(String name, boolean addCreator) {
        for (Creator c : _creators) {
            if (c.getName().equals(name))
                return c;
        }
        if (addCreator) {
            Creator creator = new Creator(name);
            _creators.add(creator);
            return creator;
        }
        return null;
    }

    /**
     * Parse a category based on it's codename.
     * 
     * @param codename The category's codename.
     * @throws UnrecognizedEntryException If the codename is not associated with a category.
     * @returns The corresponding category.
     */
    private Category parseCategory(String codename) throws UnrecognizedEntryException {
        switch (codename) {
            case "FICTION" -> {
                return new FictionCategory();
            }
            case "REFERENCE" -> {
                return new ReferenceCategory();
            }
            case "SCITECH" -> {
                return new SciTechCategory();
            }
            default -> throw new UnrecognizedEntryException("category");
        }
    }



    /**
     * Advance the date by a specified number of days.
     * 
     * @param days The days to advance.
     */
    public void advanceDate(int days) {
        _modified = true;
        _currentDate += days;
        for (Request r : _requests)
            if (r.isOverDue(_currentDate))
                r.getUser().setStatus(new SuspendedStatus());
    }


    /**
     * Get a specific work.
     * 
     * @param wid The work ID.
     * @throws WorkNotFoundException When no work is found.
     * @returns A String representation of the specified work if it exists, or null if it doesn't.
     */
    public String showWork(int wid) throws WorkNotFoundException {
        return getWorkByWID(wid).toString();
    }

    /**
     * Get all works in the library.
     * 
     * @returns A collection with a String representation of each work in the library.
     */
    public Collection<String> showWorks() {
        return _works.stream().map(Work::toString).toList();
    }

    /**
     * Get all works from a specific creator.
     * 
     * @param name The name of the creator
     * @returns A collection with String representations of each work from the creator if the
     *          creator exists, null otherwise.
     */
    public Collection<String> showWorksByCreator(String name) {
        Creator c = searchAddCreator(name, false);
        if (c == null)
            return null;
        return c.getWorks().stream().map(Work::toString).toList();
    }

    /**
     * Get a specific user.
     * 
     * @param uid The user ID.
     * @throws UserNotFoundException When on user is found.
     * @returns A String representation of the specified user if it exists, or null if it doesn't.
     */
    public String showUser(int uid) throws UserNotFoundException {
        return getUserByUID(uid).toString();
    }


    /**
     * Get all users in the library.
     * 
     * @returns A collection with String representations of each user in the library.
     */
    public Collection<String> showUsers() {
        return _users.stream()
                .sorted(Comparator.comparing(User::getName).thenComparingInt(User::getUID))
                .map(User::toString).toList();
    }

    /**
     * Register a new user.
     * 
     * @param name The user's name.
     * @param email The user's email.
     * @returns The new user ID.
     */
    public int registerUser(String name, String email) {
        _modified = true;
        User u = new User(nextUID++, name, email);
        _users.add(u);
        return u.getUID();
    }

    /**
     * Search for a Work using it's ID.
     * 
     * @param wid The Work ID.
     * @throws WorkNotFoundException When no work is found.
     * @returns The Work.
     */
    private Work getWorkByWID(int wid) throws WorkNotFoundException {
        for (Work w : _works)
            if (w.getWID() == wid)
                return w;
        throw new WorkNotFoundException();

    }

    /**
     * Search for a User using it's ID.
     * 
     * @param uid The User ID.
     * @throws UserNotFoundException When no user is found.
     * @returns The User.
     */
    private User getUserByUID(int uid) throws UserNotFoundException {
        for (User u : _users)
            if (u.getUID() == uid)
                return u;
        throw new UserNotFoundException();
    }

    /**
     * Shows all notifications related to the user.
     * 
     * @param uid The user's id.
     * @throws UserNotFoundException When no user is found.
     * @returns All string representations of each notification.
     */
    public Collection<String> showUserNotifications(int uid) throws UserNotFoundException {
        User u = getUserByUID(uid);
        return u.showNotifications();
    }

    /**
     * Pays the fines associated with a User.
     * 
     * @param uid The User's ID.
     * @throws UserNotFoundException When no user is found.
     * @throws NoFineToPayException When the user is active and there are no fines to pay.
     */
    public void payFine(int uid) throws UserNotFoundException, NoFineToPayException {
        User u = getUserByUID(uid);
        if (u.getStatus().isEligible())
            throw new NoFineToPayException();
        u.payFine();
        if (!isUserStillSuspended(u))
            u.setStatus(new ActiveStatus());
        _modified = true;
    }

    private boolean isUserStillSuspended(User u){
        for (Request r : _requests)
            if (r.getUser()==u && r.isOverDue(_currentDate))
                return true;
        return false;
    }



    /**
     * Changes a work's inventory.
     * 
     * @param wid The Work's ID.
     * @param amount The amount to change.
     * @throws WorkNotFoundException When no work is found.
     * @throws NotEnoughInventoryException When there isn't enough inventory.
     */
    public void changeWorkInventory(int wid, int amount)
            throws WorkNotFoundException, NotEnoughInventoryException {
        Work w = getWorkByWID(wid);
        w.addCopies(amount);
        if (w.getCopies() <= 0)
            removeWork(w);
        _modified = true;
    }


    /**
     * Deletes a Work.
     * 
     * @param Work w
     */
    private void removeWork(Work w) {
        for (Creator c : w.getCreators()) {
            c.removeWork(w);
            if (!c.containsWorks())
                _creators.remove(c);
        }
        _works.remove(w);
    }

    public Collection<String> searchWorks(String term) {
        Collection<String> out = new ArrayList<String>();
        for (Work w : _works) {
            // Gather fields to perform the comparison.
            Collection<String> searchFields = new ArrayList<String>();
            searchFields.add(w.getTitle());
            searchFields.addAll(w.getCreatorStrings());

            // Term comparison.
            for (String s : searchFields) {
                if (s.toLowerCase().contains(term.toLowerCase())) {
                    out.add(w.toString());
                    continue;
                }
            }
        }
        return out;
    }

    public void addReturnInterestedUser(int uid, int wid)
            throws UserNotFoundException, WorkNotFoundException {
        User u = getUserByUID(uid);
        Work w = getWorkByWID(wid);

        w.addReturnInterestedUser(u);
        _modified = true;

    }

    public void addRequestInterestedUser(int uid, int wid)
            throws UserNotFoundException, WorkNotFoundException {
        User u = getUserByUID(uid);
        Work w = getWorkByWID(wid);

        w.addRequestInterestedUser(u);
    }

    public void removeRequestInterestedUser(int uid, int wid)
            throws UserNotFoundException, WorkNotFoundException {
        User u = getUserByUID(uid);
        Work w = getWorkByWID(wid);

        w.removeRequestInterestedUser(u);
    }



}
