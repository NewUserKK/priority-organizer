package priorg.main.tasks;


/**
 * @author Konstantin Kostin
 * */
public class DuplicateNameException extends Exception {

    public DuplicateNameException() {
    }

    public DuplicateNameException(String message) {
        super(message);
    }

    public DuplicateNameException(Throwable cause) {
        super(cause);
    }

    public DuplicateNameException(String message, Throwable cause) {
        super(message, cause);
    }

    protected DuplicateNameException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
