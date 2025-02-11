import java.io.*;

/**
 * A Logger utility for writing messages to a log file.
 * Provides methods to log messages and manage the file lifecycle.
 */
public class Logger {

    /** The PrintWriter used to write to the log file */
    private PrintWriter writer;


    /**
     * Constructs a Logger instance that writes to the specified file.
     *
     * @param filename the name of the file to which logs will be written
     * @throws IOException if an I/O error occurs while opening the file
     */
    public Logger(String filename) throws IOException {
        // Create a PrintWriter with BufferedWriter for efficient file writing
        writer = new PrintWriter(new BufferedWriter(new FileWriter(filename, false)));

    }

    /**
     * Logs a message to the file.
     * Each message is written to a new line in the file.
     *
     * @param message the message to be logged
     */
    public void log(String message) {
        // Write the message to the log file and flush the stream
        writer.println(message);
        writer.flush();
    }

    /**
     * Closes the logger, releasing any system resources associated with the file.
     * Should be called when logging is complete to ensure all data is saved properly.
     */
    public void close() {
        // Close the PrintWriter to release resources
        writer.close();
    }


}
