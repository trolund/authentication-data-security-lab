package shared.exceptions;

public class NotStarted extends Exception {

    public NotStarted(String errorMessage) {
        super(errorMessage);
    }
    public NotStarted(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

}
