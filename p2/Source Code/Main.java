import java.io.*;


public class Main {

    public static void main(String[] args) throws IOException {

        String inputFile = args[0];
        String outputFile = args[1];

        Logger logger = new Logger(outputFile);
        UserManager userManager = new UserManager(logger);
        PostManager postManager = new PostManager(logger, userManager);


        Main mainApp = new Main();
        mainApp.fileReader(inputFile, userManager, postManager, logger);


    }

    /**
     * Reads and processes commands from a specified input file.
     *
     * This method uses a BufferedReader to read the file line by line. Each line contains a command
     * and its parameters, which are processed based on the command type. The method interacts with
     * UserManager and PostManager to execute the corresponding operations.
     *
     * @param fileName      the name of the file containing the commands to process.
     * @param userManager   an instance of UserManager to manage user-related operations.
     * @param postManager   an instance of PostManager to manage post-related operations.
     * @param logger        an instance of Logger for logging operations.
     * @throws IOException  if an error occurs while reading the file.
     */
    public void fileReader(String fileName, UserManager userManager, PostManager postManager, Logger logger) throws IOException {
        // Use BufferedReader for efficient file reading
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;

            // Read the file line by line
            while ((line = reader.readLine()) != null) {
                // Split the line into words to extract command and its parameters
                String[] words = line.split(" ");

                try {
                    // Process each command based on the first word
                    switch (words[0]) {

                        case "create_user":
                            // Create a new user
                            String userID = words[1];
                            userManager.createUser(userID);
                            break;

                        case "follow_user":
                            // Make userID1 follow userID2
                            String userID1 = words[1];
                            String userID2 = words[2];
                            userManager.followUser(userID1, userID2);
                            break;

                        case "unfollow_user":
                            // Make userID1 unfollow userID2
                            userID1 = words[1];
                            userID2 = words[2];
                            userManager.unfollowUser(userID1, userID2);
                            break;

                        case "create_post":
                            // Create a new post for a user
                            userID = words[1];
                            String postID = words[2];
                            String content = words[3];
                            postManager.createPost(userID, postID, content);
                            break;

                        case "see_post":
                            // Mark a post as seen by a user
                            userID = words[1];
                            postID = words[2];
                            postManager.seePost(userID, postID);
                            break;

                        case "see_all_posts_from_user":
                            // Mark all posts from userID2 as seen by userID1
                            userID1 = words[1];
                            userID2 = words[2];
                            postManager.seeAllPostsFromUser(userID1, userID2);
                            break;

                        case "toggle_like":
                            // Toggle like status for a post
                            userID = words[1];
                            postID = words[2];
                            postManager.toggleLike(userID, postID);
                            break;

                        case "generate_feed":
                            // Generate a feed of posts for a user
                            userID = words[1];
                            int num = Integer.parseInt(words[2]);
                            postManager.generateFeed(userID, num);
                            break;

                        case "scroll_through_feed":
                            // Simulate scrolling through a feed and interacting with posts
                            userID = words[1];
                            num = Integer.parseInt(words[2]);

                            // Parse the like actions (1 = like, 0 = no action)
                            int[] likes = new int[num];
                            for (int i = 0; i < num; i++) {
                                likes[i] = Integer.parseInt(words[3 + i]);
                            }

                            postManager.scrollThroughFeed(userID, num, likes);
                            break;

                        case "sort_posts":
                            // Sort posts for a user
                            userID = words[1];
                            postManager.sortPosts(userID);
                            break;

                        default:
                            // Handle unknown commands
                            System.out.println("Unknown command: " + words[0]);
                            break;
                    }
                } catch (NumberFormatException e) {
                    // Handle errors while parsing integers
                    System.out.println("Error parsing integer from line: " + line);
                }
            }
        }

        // Close the logger to ensure all logs are saved and resources are released
        logger.close();
    }

}
