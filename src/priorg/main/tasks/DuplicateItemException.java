package priorg.main.tasks;


/**
 * @author Konstantin Kostin
 * */
public class DuplicateItemException extends Exception {

    public DuplicateItemException() {
    }

    public DuplicateItemException(String message) {
        super(message);
    }

    public DuplicateItemException(Throwable cause) {
        super(cause);
    }

    public DuplicateItemException(String message, Throwable cause) {
        super(message, cause);
    }

    protected DuplicateItemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
