/**
 * Represents a User in the system, with properties to manage their following list, posts, liked posts,
 * and seen posts.
 */
public class User {

    /** Unique identifier for the user */
    private String userId;

    /** A collection of users that this user is following */
    private MyHashMap<String, User> following;

    /** A collection of posts created by this user */
    private MyHashMap<String, Post> userPosts;

    /** A collection of posts liked by this user */
    private MyHashMap<String, Post> likedPosts;

    /** A collection of posts seen by this user */
    private MyHashMap<String, Post> seenPosts;

    /**
     * Constructs a User with the specified userId.
     *
     * @param userId the unique identifier for the user
     */
    public User(String userId) {
        this.userId = userId;
        this.following = new MyHashMap<>();
        this.userPosts = new MyHashMap<>();
        this.seenPosts = new MyHashMap<>();
        this.likedPosts = new MyHashMap<>();
    }


    /**
     * Adds a User to the following list, indicating this User is now following the specified User.
     *
     * @param user the User to follow
     */
    public void follow(User user) {
        // Add the user to the "following" map using their userId as the key
        following.put(user.userId, user);
    }

    /**
     * Removes a User from the following list, indicating this User is no longer following the specified User.
     *
     * @param user the User to unfollow
     */
    public void unfollow(User user) {
        // Remove the user from the "following" map using their userId as the key
        following.remove(user.userId);
    }

    /**
     * Checks if this User is following the specified User.
     *
     * @param user the User to check
     * @return true if this User is following the specified User, false otherwise
     */
    public boolean isFollowing(User user) {
        // Check if the "following" map contains the userId of the specified user
        return following.containsKey(user.userId);
    }

    /**
     * Adds a post to the user's collection of posts.
     *
     * @param post the Post object to be added
     */
    public void addPost(Post post) {
        // Add the post to the userPosts map using its postId as the key
        userPosts.put(post.getPostId(), post);
    }

    /**
     * Marks a post as seen by the user.
     * Adds the post to the user's collection of seen posts.
     *
     * @param post the Post object that the user has seen
     */
    public void see(Post post) {
        // Add the post to the seenPosts map using its postId as the key
        seenPosts.put(post.getPostId(), post);
    }

    /**
     * Adds a post to the user's collection of liked posts.
     *
     * @param post the Post object to be liked
     */
    public void likePost(Post post) {
        // Add the post to the likedPosts map using its postId as the key
        likedPosts.put(post.getPostId(), post);
    }

    /**
     * Removes a post from the user's collection of liked posts.
     *
     * @param post the Post object to be unliked
     */
    public void unlikePost(Post post) {
        // Remove the post from the likedPosts map using its postId as the key
        likedPosts.remove(post.getPostId());
    }

    // Getters

    public String getUserId() {
        return userId;
    }


    public MyHashMap<String, User> getFollowing() {
        return following;
    }


    public MyHashMap<String, Post> getUserPosts() {
        return userPosts;
    }


    public MyHashMap<String, Post> getLikedPosts() {
        return likedPosts;
    }


    public MyHashMap<String, Post> getSeenPosts() {
        return seenPosts;
    }


}
