/**
 * Manages post-related operations, such as creating posts, viewing posts, and liking/unliking posts.
 */
public class PostManager {

    private MyHashMap<String, Post> posts; // A collection of posts stored by their unique IDs
    private Logger logger; // Logger for tracking operations
    private UserManager userManager; // Manages user-related data and operations

    /**
     * Constructs a PostManager with the specified logger and user manager.
     *
     * @param logger      the Logger instance for logging post operations
     * @param userManager the UserManager instance to handle user data
     */
    public PostManager(Logger logger, UserManager userManager) {
        posts = new MyHashMap<>();
        this.logger = logger;
        this.userManager = userManager;
    }

    /**
     * Creates a new post authored by a user.
     *
     * @param userID  the ID of the user creating the post
     * @param postID  the unique ID of the post
     * @param content the content of the post
     */
    public void createPost(String userID, String postID, String content) {
        // Check if the user exists and the post ID is unique
        if (!userManager.userExists(userID) || postExists(postID)) {
            logger.log("Some error occurred in create_post.");
            return;
        }

        User author = userManager.getUser(userID); // Retrieve the author (user) object
        Post post = new Post(postID, content, author); // Create a new post

        posts.put(postID, post); // Add the post to the global collection
        author.addPost(post); // Add the post to the author's list of posts

        logger.log(userID + " created a post with Id " + postID + ".");
    }

    /**
     * Marks a post as seen by a user.
     *
     * @param userID the ID of the user viewing the post
     * @param postID the ID of the post being viewed
     */
    public void seePost(String userID, String postID) {
        // Check if the user and post exist
        if (!userManager.userExists(userID) || !postExists(postID)) {
            logger.log("Some error occurred in see_post.");
            return;
        }

        User viewer = userManager.getUser(userID); // Retrieve the viewer (user) object
        Post post = posts.get(postID); // Retrieve the post

        viewer.see(post); // Mark the post as seen by the user
        logger.log(userID + " saw " + postID + ".");
    }

    /**
     * Marks all posts from a specific user as seen by another user.
     *
     * @param viewerID the ID of the user viewing the posts
     * @param authorID the ID of the user whose posts are being viewed
     */
    public void seeAllPostsFromUser(String viewerID, String authorID) {
        // Check if both the viewer and the author exist
        if (!userManager.userExists(viewerID) || !userManager.userExists(authorID)) {
            logger.log("Some error occurred in see_all_posts_from_user.");
            return;
        }

        User author = userManager.getUser(authorID); // Retrieve the author (user) object
        User viewer = userManager.getUser(viewerID); // Retrieve the viewer (user) object

        // Mark all posts from the author as seen by the viewer
        for (Post post : author.getUserPosts().values()) {
            viewer.see(post);
        }

        logger.log(viewerID + " saw all posts of " + authorID + ".");
    }

    /**
     * Toggles the like status of a post for a user.
     * If the user already likes the post, it unlikes it; otherwise, it likes the post.
     *
     * @param userID the ID of the user liking/unliking the post
     * @param postID the ID of the post being liked/unliked
     */
    public void toggleLike(String userID, String postID) {
        // Check if the user and post exist
        if (!userManager.userExists(userID) || !postExists(postID)) {
            logger.log("Some error occurred in toggle_like.");
            return;
        }

        User viewer = userManager.getUser(userID); // Retrieve the user toggling the like
        Post post = posts.get(postID); // Retrieve the post

        if (viewer.getLikedPosts().containsKey(postID)) {
            // If the post is already liked, unlike it
            viewer.unlikePost(post);
            post.getUnliked();
            logger.log(userID + " unliked " + postID + ".");
        } else {
            // If the post is not liked, like it
            viewer.see(post); // Ensure the post is marked as seen
            viewer.likePost(post);
            post.getLiked();
            logger.log(userID + " liked " + postID + ".");
        }
    }

    /**
     * Generates a feed of posts for a user based on posts from followed users.
     * The feed is limited to a specified number of posts and excludes already seen posts and the user's own posts.
     *
     * @param userID the ID of the user requesting the feed
     * @param num    the maximum number of posts to include in the feed
     */
    public void generateFeed(String userID, int num) {
        // Check if the user exists
        if (!userManager.userExists(userID)) {
            logger.log("Some error occurred in generate_feed.");
            return;
        }

        User viewer = userManager.getUser(userID); // Retrieve the viewer (user) object
        MyHashMap<String, Post> userPosts = viewer.getUserPosts(); // User's own posts
        MyHashMap<String, Post> seenPosts = viewer.getSeenPosts(); // Posts the user has already seen
        MyHashMap<String, User> followedUsers = viewer.getFollowing(); // Users followed by the viewer

        // Initialize a min-heap to prioritize posts
        MyMinHeap<Post> minHeap = new MyMinHeap<>(num);

        // Iterate over posts from followed users and add eligible posts to the heap
        for (User followedUser : followedUsers.values()) {
            for (Post post : followedUser.getUserPosts().values()) {
                // Exclude posts that are already seen or are the viewer's own posts
                if (!seenPosts.containsKey(post.getPostId()) && !userPosts.containsKey(post.getPostId())) {
                    if (minHeap.currentSize < num) {
                        minHeap.insert(post); // Insert if heap is not full
                    } else if (comparePosts(post, minHeap.findMin()) > 0) {
                        // Replace the smallest post if the new post has higher priority
                        minHeap.deleteMin();
                        minHeap.insert(post);
                    }
                }
            }
        }

        // Sort posts in the heap
        Post[] sortedArray = minHeap.heapSort();

        // Log the generated feed
        logger.log("Feed for " + userID + ":");
        for (Post post : sortedArray) {
            logger.log("Post ID: " + post.getPostId() + ", Author: " + post.getAuthor().getUserId()
                    + ", Likes: " + post.getLikeCount());
        }

        if (sortedArray.length < num) {
            logger.log("No more posts available for " + userID + ".");
        }
    }

    /**
     * Allows a user to scroll through their feed and optionally like posts.
     * The feed is generated based on posts from followed users and excludes already seen posts and the user's own posts.
     *
     * @param userID the ID of the user scrolling through the feed
     * @param num    the number of posts to include in the feed
     * @param nums   an array indicating whether each post is liked (1 for like, 0 for no action)
     */
    public void scrollThroughFeed(String userID, int num, int[] nums) {
        // Check if the user exists
        if (!userManager.userExists(userID)) {
            logger.log("Some error occurred in scroll_through_feed.");
            return;
        }

        User viewer = userManager.getUser(userID); // Retrieve the viewer (user) object
        MyHashMap<String, Post> userPosts = viewer.getUserPosts(); // User's own posts
        MyHashMap<String, Post> seenPosts = viewer.getSeenPosts(); // Posts the user has already seen
        MyHashMap<String, User> followedUsers = viewer.getFollowing(); // Users followed by the viewer

        // Initialize a min-heap to prioritize posts
        MyMinHeap<Post> minHeap = new MyMinHeap<>(num);

        // Iterate over posts from followed users and add eligible posts to the heap
        for (User followedUser : followedUsers.values()) {
            for (Post post : followedUser.getUserPosts().values()) {
                // Exclude posts that are already seen or are the viewer's own posts
                if (!seenPosts.containsKey(post.getPostId()) && !userPosts.containsKey(post.getPostId())) {
                    if (minHeap.currentSize < num) {
                        minHeap.insert(post); // Insert if heap is not full
                    } else if (comparePosts(post, minHeap.findMin()) > 0) {
                        // Replace the smallest post if the new post has higher priority
                        minHeap.deleteMin();
                        minHeap.insert(post);
                    }
                }
            }
        }

        // Sort posts in the heap
        Post[] sortedArray = minHeap.heapSort();

        int i = 0;
        // Log the feed as the user scrolls through it
        logger.log(userID + " is scrolling through feed:");
        for (Post post : sortedArray) {
            viewer.getSeenPosts().put(post.getPostId(), post); // Mark the post as seen
            if (nums[i] == 1) {
                // User likes the post
                viewer.likePost(post);
                post.getLiked();
                logger.log(userID + " saw " + post.getPostId() + " while scrolling and clicked the like button.");
            } else {
                // User views the post without liking
                logger.log(userID + " saw " + post.getPostId() + " while scrolling.");
            }
            i++;
        }

        if (sortedArray.length < num) {
            logger.log("No more posts in feed.");
        }
    }


    /**
     * Sorts the posts of a given user by the number of likes in ascending order.
     *
     * This method retrieves all posts associated with the provided user ID,
     * validates that the user exists and has posts, then sorts these posts
     * using a custom merge sort algorithm. After sorting, the sorted posts
     * and their like counts are logged.
     *
     * @param userID the ID of the user whose posts need to be sorted.
     */
    public void sortPosts(String userID) {
        // Check if the user exists
        if (!userManager.userExists(userID)) {
            // Log an error message and terminate if the user does not exist
            logger.log("Some error occurred in sort_posts.");
            return;
        }

        // Retrieve the User object associated with the user ID
        User user = userManager.getUser(userID);

        // Retrieve the user's posts stored in a MyHashMap
        MyHashMap<String, Post> userPostsMap = user.getUserPosts();

        // Check if the user has no posts
        if (userPostsMap.isEmpty()) {
            // Log a message indicating no posts exist and terminate
            logger.log("No posts from " + userID + ".");
            return;
        }

        // Log the beginning of the sorting process
        logger.log("Sorting " + userID + "'s posts:");

        // Convert the HashMap values (posts) to an array for sorting
        Post[] postsArray = userPostsMap.values().toArray(new Post[0]);

        // Sort the posts using a custom merge sort algorithm
        mergeSort(postsArray, 0, postsArray.length - 1);

        // Log the sorted posts with their like counts
        for (Post post : postsArray) {
            logger.log(post.getPostId() + ", Likes: " + post.getLikeCount());
        }
    }


    /**
     * Recursively performs merge sort on a given array of Post objects.
     *
     * The method divides the array into halves, sorts each half recursively,
     * and merges the sorted halves using the merge() method.
     *
     * @param array the array of Post objects to be sorted.
     * @param left the starting index of the subarray to be sorted.
     * @param right the ending index of the subarray to be sorted.
     */
    private void mergeSort(Post[] array, int left, int right) {
        if (left < right) {
            // Calculate the middle index of the array
            int mid = left + (right - left) / 2;

            // Recursively sort the first and second halves
            mergeSort(array, left, mid);
            mergeSort(array, mid + 1, right);

            // Merge the sorted halves
            merge(array, left, mid, right);
        }
    }

    /**
     * Merges two sorted subarrays into a single sorted subarray.
     *
     * The method takes two sorted subarrays from the given array:
     *  - The first subarray is from indices `left` to `mid`.
     *  - The second subarray is from indices `mid + 1` to `right`.
     * It merges these subarrays into a single sorted subarray.
     *
     * @param array the original array containing the subarrays to be merged.
     * @param left the starting index of the first subarray.
     * @param mid the ending index of the first subarray and the middle of the original array.
     * @param right the ending index of the second subarray.
     */
    private void merge(Post[] array, int left, int mid, int right) {
        // Sizes of the two subarrays to be merged
        int n1 = mid - left + 1; // Size of the left subarray
        int n2 = right - mid;    // Size of the right subarray

        // Create temporary arrays for the two subarrays
        Post[] leftArray = new Post[n1];
        Post[] rightArray = new Post[n2];

        // Copy data to temporary arrays
        System.arraycopy(array, left, leftArray, 0, n1);
        System.arraycopy(array, mid + 1, rightArray, 0, n2);

        // Merge the temporary arrays back into the original array

        int i = 0; // Initial index of the left subarray
        int j = 0; // Initial index of the right subarray
        int k = left; // Initial index of the merged subarray

        // Compare elements from both subarrays and insert the smaller element into the merged array
        while (i < n1 && j < n2) {
            if (comparePostsForMergeSort(leftArray[i], rightArray[j]) <= 0) {
                array[k] = leftArray[i];
                i++;
            } else {
                array[k] = rightArray[j];
                j++;
            }
            k++;
        }

        // Copy any remaining elements from the left subarray
        while (i < n1) {
            array[k] = leftArray[i];
            i++;
            k++;
        }

        // Copy any remaining elements from the right subarray
        while (j < n2) {
            array[k] = rightArray[j];
            j++;
            k++;
        }
    }


    /**
     * Helper method to compare two Post objects based on the number of likes
     * and, if likes are equal, their post IDs in ascending order.
     *
     * The comparison is done as follows:
     * - First, compare the like counts of the two posts in ascending order.
     * - If the like counts are the same, compare their post IDs lexicographically.
     *
     * @param p1 the first Post object to compare.
     * @param p2 the second Post object to compare.
     * @return a negative integer, zero, or a positive integer if the first Post object
     *         is less than, equal to, or greater than the second, respectively.
     */
    private int comparePosts(Post p1, Post p2) {
        int likeCompare = Integer.compare(p1.getLikeCount(), p2.getLikeCount());
        if (likeCompare != 0) {
            return likeCompare;
        }
        return p1.getPostId().compareTo(p2.getPostId());
    }

    /**
     * Helper method to compare two Post objects for sorting in descending order
     * of likes and, if likes are equal, by their post IDs in descending order.
     *
     * The comparison is done as follows:
     * - First, compare the like counts of the two posts in descending order.
     * - If the like counts are the same, compare their post IDs lexicographically in descending order.
     *
     * @param p1 the first Post object to compare.
     * @param p2 the second Post object to compare.
     * @return a negative integer, zero, or a positive integer if the first Post object
     *         is greater than, equal to, or less than the second, respectively.
     */
    private int comparePostsForMergeSort(Post p1, Post p2) {
        int likeCompare = Integer.compare(p2.getLikeCount(), p1.getLikeCount());
        if (likeCompare != 0) {
            return likeCompare;
        }
        return p2.getPostId().compareTo(p1.getPostId());
    }

    /**
     * Checks if a post with the given post ID exists in the collection.
     *
     * @param postID the ID of the post to check.
     * @return true if the post exists; false otherwise.
     */
    public boolean postExists(String postID) {
        return posts.containsKey(postID);
    }

}
