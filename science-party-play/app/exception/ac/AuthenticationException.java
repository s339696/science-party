package exception.ac;

/**
 * A AuthenticationException is thrown if a AuthorConnect request is wrong or
 * the user has no permit for administration
 */
public class AuthenticationException extends Exception {
    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException() {
    }
}
