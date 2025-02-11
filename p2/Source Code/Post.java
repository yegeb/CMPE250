/**
 * Represents a Post in the system, authored by a User, with properties for content, likes, and comparisons.
 */
public class Post implements Comparable<Post> {

    /** Unique identifier for the post */
    private String postId;

    /** The content of the post */
    private String content;

    /** The author of the post */
    private User author;

    /** The number of likes the post has received */
    private int likeCount;


    /**
     * Constructs a Post with the specified ID, content, and author.
     *
     * @param postId  the unique identifier for the post
     * @param content the content of the post
     * @param author  the User who authored the post
     */
    public Post(String postId, String content, User author) {
        this.postId = postId;
        this.content = content;
        this.author = author;
    }

    /**
     * Compares this Post to another Post based on the number of likes and post ID.
     * <ul>
     *     <li>Posts with fewer likes come first.</li>
     *     <li>If two posts have the same number of likes, the one with the lexicographically smaller ID comes first.</li>
     * </ul>
     *
     * @param post the other Post to compare to
     * @return a negative integer, zero, or a positive integer as this Post is less than,
     *         equal to, or greater than the specified Post
     */
    @Override
    public int compareTo(Post post) {
        // Compare by like count first
        if (this.likeCount != post.likeCount) {
            return Integer.compare(this.likeCount, post.likeCount);
        } else {
            // If likes are equal, compare by post ID
            return this.postId.compareTo(post.postId);
        }
    }


    /**
     * Increments the like count for this post by 1.
     * This method is called when a user likes the post.
     */
    public void getLiked() {
        likeCount += 1;
    }

    /**
     * Decrements the like count for this post by 1.
     * This method is called when a user unlikes the post.
     */
    public void getUnliked() {
        likeCount -= 1;
    }


    // Getters

    public String getPostId() {
        return postId;
    }


    public User getAuthor() {
        return author;
    }


    public int getLikeCount() {
        return likeCount;
    }

}
