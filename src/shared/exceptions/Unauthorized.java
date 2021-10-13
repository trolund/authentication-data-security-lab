package shared.exceptions;

public class Unauthorized extends Exception {

    public Unauthorized(String errorMessage) {
        super(errorMessage);
    }
    public Unauthorized(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

}
