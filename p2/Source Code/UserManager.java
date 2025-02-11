/**
 * Manages operations related to Users, such as creating users, following, and unfollowing.
 * Uses a custom hash map to store and retrieve users and logs operations.
 */
public class UserManager {

    /** A collection of all users in the system, mapped by their user ID */
    private MyHashMap<String, User> users = new MyHashMap<>();

    /** Logger instance for logging operations */
    private Logger logger;

    /**
     * Constructs a UserManager with the specified logger.
     *
     * @param logger the Logger instance for logging user operations
     */
    public UserManager(Logger logger) {
        this.logger = logger;
    }

    /**
     * Creates a new user with the specified user ID.
     * Logs a message on successful creation or if the user already exists.
     *
     * @param userID the unique ID of the user to be created
     */
    public void createUser(String userID) {
        // Check if the user already exists
        if (userExists(userID)) {
            logger.log("Some error occurred in create_user."); // Log error if user exists
        } else {
            // Create a new user and log the operation
            users.put(userID, new User(userID));
            logger.log("Created user with Id " + userID + ".");
        }
    }

    /**
     * Allows a user to follow another user.
     * Logs appropriate messages based on the operation's outcome.
     *
     * @param userID1 the ID of the user who wants to follow
     * @param userID2 the ID of the user to be followed
     */
    public void followUser(String userID1, String userID2) {
        // Check if either user does not exist
        if (!userExists(userID1) || !userExists(userID2)) {
            logger.log("Some error occurred in follow_user.");
            return;
        }

        User user1 = users.get(userID1); // Retrieve the user who wants to follow
        User user2 = users.get(userID2); // Retrieve the user to be followed

        // Check if the user is trying to follow themselves
        if (userID1.equals(userID2)) {
            logger.log("Some error occurred in follow_user.");
            return;
        }

        // Check if the user is already following the other user
        if (user1.isFollowing(user2)) {
            logger.log("Some error occurred in follow_user."); // Log error if already following
        } else {
            // Add the user to the following list and log the operation
            user1.follow(user2);
            logger.log(userID1 + " followed " + userID2 + ".");
        }
    }

    /**
     * Allows a user to unfollow another user.
     * Logs appropriate messages based on the operation's outcome.
     *
     * @param userID1 the ID of the user who wants to unfollow
     * @param userID2 the ID of the user to be unfollowed
     */
    public void unfollowUser(String userID1, String userID2) {
        // Check if either user does not exist
        if (!userExists(userID1) || !userExists(userID2)) {
            logger.log("Some error occurred in unfollow_user.");
            return;
        }

        User user1 = users.get(userID1); // Retrieve the user who wants to unfollow
        User user2 = users.get(userID2); // Retrieve the user to be unfollowed

        // Check if the user is trying to unfollow themselves
        if (userID1.equals(userID2)) {
            logger.log("Some error occurred in unfollow_user.");
            return;
        }

        // Check if the user is not currently following the other user
        if (!user1.isFollowing(user2)) {
            logger.log("Some error occurred in unfollow_user."); // Log error if not following
        } else {
            // Remove the user from the following list and log the operation
            user1.unfollow(user2);
            logger.log(userID1 + " unfollowed " + userID2 + ".");
        }
    }


    /**
     * Checks if a user exists in the system.
     *
     * @param userID the ID of the user to check
     * @return true if the user exists, false otherwise
     */
    public boolean userExists(String userID) {
        return users.containsKey(userID);
    }


    public User getUser(String userID) {
        return users.get(userID);
    }
}
