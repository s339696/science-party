package exception.perks;

/**
 * This exception is thrown if a perk fails to be added to a user
 */
public class GetPerkException extends Exception {
    public GetPerkException(String message) {
        super(message);
    }

    public GetPerkException() {
    }
}
