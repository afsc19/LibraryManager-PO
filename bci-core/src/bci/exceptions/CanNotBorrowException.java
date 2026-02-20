package bci.exceptions;

public class CanNotBorrowException extends Exception {

    private int _failedRule;

    public CanNotBorrowException(int failedRule){
        _failedRule = failedRule;
    }

    public int getFailedRule(){
        return _failedRule;
    }
    
}
