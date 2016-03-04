package exception.friends;

/**
 * Gives the feedback for a friend request.
 */
public class FriendRequestException extends Exception {
    public FriendRequestException() {
    }

    public FriendRequestException(String message) {
        super(message);
    }
}
