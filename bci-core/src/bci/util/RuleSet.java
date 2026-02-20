package bci.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import bci.util.rules.AllowedCategoryRule;
import bci.util.rules.DuplicateRequestRule;
import bci.util.rules.MaxRequestsRule;
import bci.util.rules.StockRule;
import bci.util.rules.UserStatusRule;
import bci.util.rules.ClassificationPriceRule;
import bci.util.user.Classification;
import bci.util.user.User;
import bci.util.work.Work;


public class RuleSet implements Serializable {

    
    private ArrayList<Rule> _rules = new ArrayList<Rule>();
    private int _deadlines[][];
    private int _deadlinesCopies[];
    private int _fineAmmount;

    public RuleSet(int maxRequests[], int deadlines[][], int deadlinesCopies[],
            int maxWorkPrice, int fineAmmount) {
        
        _rules.add(new DuplicateRequestRule());
        _rules.add(new UserStatusRule());
        _rules.add(new StockRule());
        _rules.add(new MaxRequestsRule(maxRequests));
        _rules.add(new AllowedCategoryRule());
        _rules.add(new ClassificationPriceRule(maxWorkPrice));
        _deadlines = deadlines;
        _deadlinesCopies = deadlinesCopies;
        _fineAmmount = fineAmmount;
    }


    public int calculateFine(int currentDate, int deadline) {
        if (currentDate <= deadline)
            return 0;
        return (currentDate - deadline) * _fineAmmount;
    }


    /**
     * Evaluate whether a user is allowed to request a specific work.
     * 
     * @param user The requesting user.
     * @param work The requested work.
     * @param requests The current library's requests.
     * @returns Zero (0) if the user is allowed, or the number of the first rule that denied the
     *          request.
     */
    public int canBorrow(User user, Work work, Collection<Request> requests) {
        int inspection[] = inspectRequests(user, work, requests);

        for (int i = 0; i < _rules.size(); i++){
            if (!_rules.get(i).canBorrow(user, work, inspection))
                return i+1;
        }
        return 0;
    }


    /**
     * Get the number of days the user is allowed to borrow a work.
     * 
     * @param copies The number of the copies available.
     * @param classification The user's classification.
     * @returns The number of days the user is allowed to borrow a work.
     */
    public int getDeadline(int currentDay, int copies, Classification classification) {
        for (int i = 0; i < _deadlines.length; i++) {
            if (copies <= _deadlinesCopies[i] || _deadlinesCopies[i] < 0)
                return currentDay + _deadlines[i][classification.getClassification()];
        }
        return currentDay + _deadlines[_deadlines.length - 1][classification.getClassification()];
    }


    /**
     * Evaluate the ammount of active requests made by the user and whether the user has already
     * requested a copy of the specified work.
     * 
     * @param user The requesting user.
     * @param work The requested work.
     * @param requests The current active requests.
     * @returns An array[2] with {(number of active requests for the book by the user), (0 if the user already
     *          requested a copy of the specified book, 1 otherwise)}.
     */
    private int[] inspectRequests(User user, Work work, Collection<Request> requests) {
        int results[] = {0, 0};
        for (Request r : requests) {
            if (r.getUser().equals(user)) {
                results[1]++;
                if (r.getWork().equals(work))
                    results[0] = 1;
            }
        }
        return results;
    }




}
