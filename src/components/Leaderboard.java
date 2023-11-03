package components;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The Leaderboard.
 */
public class Leaderboard {
    private final String leaderboardFilePath;

    /**
     * Initialize Leaderboard.
     */
    public Leaderboard() {
        String configPath = null;

        String possibleAppDataPath = System.getenv("APPDATA"); // Check if APPDATA variable is set
        if (possibleAppDataPath != null) {
            configPath = possibleAppDataPath + "/CloudRush";
        } else {
            possibleAppDataPath = System.getProperty("user.home"); // For Linux and macOS

            if (possibleAppDataPath != null) {
                configPath = possibleAppDataPath + "/.config/CloudRush";
            }
        }

        if (configPath == null) { // Other operating system(s)??
            System.out.println("ERROR, NO PATH FOUND WHERE TO SAVE THE CONFIG FILES!");
            System.exit(1);
        }

        if (!checkIfDirectoryExists(configPath)) { // Make sure configPath exists, else create it
            System.out.println("ERROR, INVALID PATH FOUND WHERE TO SAVE THE CONFIG FILES!");
            System.exit(1);
        }

        this.leaderboardFilePath = configPath + "/leaderboard.csv";
        if (!checkIfFileExists(leaderboardFilePath)) {
            System.out.println("ERROR, LEADERBOARD FILE IS A DIRECTORY!");
            System.exit(1);
        }
    }

    /**
     * Add an entry to the leaderboard.
     *
     * @param entry The entry to add
     */
    public void addToLeaderboard(LeaderboardEntry entry) {
        try {
            // Open csv file
            BufferedWriter writer = new BufferedWriter(new FileWriter(leaderboardFilePath, true));
            // Add entry in csv style (comma seperated) and add a new line at the end
            writer.append(entry.toString()).append("\n");
            // Save the file
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Fetch the values from the leaderboard.
     *
     * @param levelToGet the level
     * @return The leaderboard
     */
    public ArrayList<LeaderboardEntry> getLeaderboard(String levelToGet) {
        ArrayList<LeaderboardEntry> entries = new ArrayList<>();

        try {
            Scanner sc = new Scanner(new File(leaderboardFilePath)); // Load the file
            sc.useDelimiter("\n"); // Split lines by newline

            while (sc.hasNext()) {
                String entryCSV = sc.next(); // Entry
                String[] values = entryCSV.split(","); // Values of the entry

                String level = values[0];
                if (!levelToGet.equals(level)) { // If level of the entry is not level to display
                    continue;
                }

                String score = values[1];
                String nickname = values[2];

                LeaderboardEntry entry = new LeaderboardEntry(level, score, nickname); // Format
                entries.add(entry);
            }
            sc.close(); // Close file

            // Sort the leaderboard entries from high to low
            entries.sort((one, two) ->
                Integer.parseInt(two.getScore()) - Integer.parseInt(one.getScore()));

            return entries;
        } catch (Exception e) {
            return entries;
        }
    }

    /**
     * Makes sure that the file exist and is not a directory.
     *
     * @param filePath The path of the file to check
     * @return Whether file exists (true) or is a directory (false)
     */
    private boolean checkIfFileExists(String filePath) {
        try {
            File leaderboardFile = new File(filePath);
            if (!leaderboardFile.exists()) { // Check if file exists
                FileOutputStream s = new FileOutputStream(filePath); // Create file if not exists
                s.close();
                return checkIfFileExists(filePath); // Make sure the file is created
            } else {
                // It should be a file, and not directory
                return !leaderboardFile.isDirectory();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Make sure that the directory exists and is not a file.
     *
     * @param directoryPath The path of the directory to check
     * @return Whether directory exists (true) or is a file (false)
     */
    private boolean checkIfDirectoryExists(String directoryPath) {
        File directoryFile = new File(directoryPath);
        if (!directoryFile.exists()) { // Check if directory exists
            directoryFile.mkdirs(); // Create directory if not exists
            return checkIfDirectoryExists(directoryPath); // Make sure the directory is created
        } else {
            // It should be a directory, and not a file
            return !directoryFile.isFile();
        }
    }
}
