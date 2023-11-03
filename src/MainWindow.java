import components.GraphicsPanel;
import components.Language;
import components.Leaderboard;
import components.LeaderboardEntry;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.function.UnaryOperator;
import java.util.prefs.Preferences;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The main (starting) window. Here you select your level and/or settings etc..
 */
public class MainWindow {
    private final GraphicsPanel mainPanel; // The main panel
    private static JFrame window; // The main window
    private static JFrame gameWindow; // The game window
    private final JPanel buttonsPanel; // Needed to reload the leaderboard
    private final Font font; // Custom font

    // We need these buttons here to change the label on them (when users picks different language)
    private final JButton levelEasyButton;
    private final JButton levelMediumButton;
    private final JButton levelHardButton;
    private final JButton instructionsButton;
    private final JButton settingsButton;
    private final JButton exitButton;

    public static void main(String[] args) throws IOException, FontFormatException {
        // Initialize instance of MainWindow to display it
        MainWindow instance = new MainWindow();

        // Construct window
        window = new JFrame("CloudRush");
        window.setContentPane(instance.mainPanel); // Connect frame (window) to main component
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Action on window close
        window.pack(); // Set preferable window size
        window.setLocationRelativeTo(null); // Show window in the center of the screen
        window.setVisible(true); // Actual render window on screen
        window.setMinimumSize(new Dimension(860, 500)); // Perfect size not to wrap any text
        window.setResizable(false); // Not allowed to re-size window, this could mess up sprites

        // Show instructions window on first run
        Preferences preferences = Preferences.userRoot().node("/"); // Java preferences
        if (!preferences.get("initialized", "false").equals("true")) {
            preferences.put("initialized", "true");
            showInstructionsWindow();
        }
    }

    /**
     * Initialize MainWindow.
     */
    public MainWindow() {
        // Load language from Preferences API
        Language.loadLanguage();

        // Import and load custom font from resources
        InputStream is = this.getClass().getClassLoader()
            .getResourceAsStream("joystix_monospace.otf");
        try {
            assert is != null;
            font = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }

        // Load background graphics
        URL resource = getClass().getClassLoader().getResource("main_background.png");
        assert resource != null;
        Image back = new ImageIcon(resource).getImage();

        // Override creation on mainPanel to display background graphics
        mainPanel = new GraphicsPanel();
        mainPanel.paintHandler = (g) -> {
            // Set sky-blue background
            g.setColor(Color.decode("#87CEEB"));
            g.fillRect(0, 0, mainPanel.getWidth(), mainPanel.getHeight());

            // Draw background image
            g.drawImage(back, 0, 110, mainPanel.getWidth(), mainPanel.getHeight() - 110, null);
        };
        mainPanel.setLayout(new GridBagLayout());

        // App Title
        JLabel appTitle = new JLabel();
        appTitle.setFont(font.deriveFont(32f));
        appTitle.setForeground(new Color(-1));
        appTitle.setText("CloudRush");
        mainPanel.add(appTitle, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, 10, 0,
            new Insets(0, 0, 0, 0), 0, 0)); // Spacing

        // Content panel (contain buttons panel)
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setOpaque(false);
        mainPanel.add(contentPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, 10,
            GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0)); // Spacing

        // Buttons panel (containing level selection, settings and exit button)
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridBagLayout());
        buttonsPanel.setOpaque(false);
        contentPanel.add(buttonsPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 10, 0,
            new Insets(0, 0, 0, 0), 0, 0)); // Spacing

        // Level Eindhoven (easy)
        levelEasyButton = new JButton();
        levelEasyButton.setFont(font.deriveFont(18f));
        levelEasyButton.setText(Language.getString("eindhoven")
            + " (" + Language.getString("easy") + ")");
        buttonsPanel.add(levelEasyButton, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, 10,
            GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 16)); // Spacing

        // Level Warsaw (medium)
        levelMediumButton = new JButton();
        levelMediumButton.setFont(font.deriveFont(18f));
        levelMediumButton.setText(Language.getString("warsaw")
            + " (" + Language.getString("medium") + ")");
        buttonsPanel.add(levelMediumButton, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, 10,
            GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 16)); // Spacing

        // Level New York (hard)
        levelHardButton = new JButton();
        levelHardButton.setFont(font.deriveFont(18f));
        levelHardButton.setText(Language.getString("newyork")
            + " (" + Language.getString("hard") + ")");
        buttonsPanel.add(levelHardButton, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0, 10,
            GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 16)); // Spacing

        // Instructions
        instructionsButton = new JButton();
        instructionsButton.setFont(font.deriveFont(18f));
        instructionsButton.setText(Language.getString("instructions"));
        buttonsPanel.add(instructionsButton, new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0, 10,
            GridBagConstraints.HORIZONTAL, new Insets(20, 5, 5, 5), 0, 16)); // Spacing

        // Settings
        settingsButton = new JButton();
        settingsButton.setFont(font.deriveFont(18f));
        settingsButton.setText(Language.getString("settings"));
        buttonsPanel.add(settingsButton, new GridBagConstraints(1, 5, 1, 1, 1.0, 0.0, 10,
            GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 16)); // Spacing

        // Exit
        exitButton = new JButton();
        exitButton.setFont(font.deriveFont(18f));
        exitButton.setText(Language.getString("exit"));
        buttonsPanel.add(exitButton, new GridBagConstraints(1, 6, 1, 1, 1.0, 0.0, 10,
            GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 16)); // Spacing

        // Footer
        final JLabel label1 = new JLabel();
        label1.setForeground(new Color(-1));
        label1.setText("Copyright © Kacper Golik & Robin-Sch");
        mainPanel.add(label1, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, 10,
            GridBagConstraints.WEST, new Insets(0, 0, 0, 0), 0, 0)); // Spacing

        // Events to buttons' click (aka action) event
        levelEasyButton.addActionListener(e -> startGame("eindhoven"));
        levelMediumButton.addActionListener(e -> startGame("warsaw"));
        levelHardButton.addActionListener(e -> startGame("newyork"));
        instructionsButton.addActionListener(e -> showInstructionsWindow());
        settingsButton.addActionListener(e -> showSettingsWindow());
        exitButton.addActionListener(e -> {
            window.dispose();
            System.exit(0);
        });
        loadLeaderBoard();
    }

    /**
     * Display instructions window.
     */
    private static void showInstructionsWindow() {
        JFrame instructionsWindow = new JFrame("CloudRush - "
            + Language.getString("instructions"));

        InstructionsWindow instructionsWindowInstance = new InstructionsWindow(instructionsWindow);
        instructionsWindow.setContentPane(instructionsWindowInstance.backgroundPanel);
        instructionsWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        instructionsWindow.pack();
        instructionsWindow.setMinimumSize(new Dimension(540, 340));
        instructionsWindow.setMaximumSize(new Dimension(540, 340));
        instructionsWindow.setLocationRelativeTo(null); // Show window in the center of the screen
        instructionsWindow.setVisible(true);
        instructionsWindow.setResizable(false);
    }

    /**
     * Display settings window.
     */
    private void showSettingsWindow() {
        JFrame settingsWindow = new JFrame("CloudRush - "
            + Language.getString("settings"));

        SettingsWindow settingsWindowInstance = new SettingsWindow(languageName -> {
            // Close settings window
            settingsWindow.dispose();

            // Update language
            Language.setLanguage(languageName);

            // Update GUI
            levelEasyButton.setText(Language.getString("eindhoven")
                + " (" + Language.getString("easy") + ")");
            levelMediumButton.setText(Language.getString("warsaw")
                + " (" + Language.getString("medium") + ")");
            levelHardButton.setText(Language.getString("newyork")
                + " (" + Language.getString("hard") + ")");

            instructionsButton.setText(Language.getString("instructions"));
            settingsButton.setText(Language.getString("settings"));
            exitButton.setText(Language.getString("exit"));

            return null;
        });
        settingsWindow.setContentPane(settingsWindowInstance.backgroundPanel);
        settingsWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dont exit if game ends
        settingsWindow.pack();
        settingsWindow.setMinimumSize(new Dimension(340, 200));
        settingsWindow.setLocationRelativeTo(null); // Show window in the center of the screen
        settingsWindow.setVisible(true);
        settingsWindow.setResizable(false);
    }

    /**
     * Load leaderboard from file and display all players
     * with their scores in descending order to show ranking.
     */
    private void loadLeaderBoard() {
        // Remove previous leaderboard (first 6 components are part
        // of main menu, no need to remove them)
        Component[] components = buttonsPanel.getComponents();
        for (int i = 6; i < components.length; i++) {
            buttonsPanel.remove(components[i]);
        }

        // Create new leaderboard
        String[] levels = new String[]{"eindhoven", "warsaw", "newyork"};
        Leaderboard leaderboard = new Leaderboard();
        for (int i = 0; i < levels.length; i++) {
            ArrayList<LeaderboardEntry> entries = leaderboard.getLeaderboard(levels[i]);

            JPanel leaderboardPanel = new JPanel();
            leaderboardPanel.setOpaque(false);
            leaderboardPanel.setLayout(new GridBagLayout());
            leaderboardPanel.setMaximumSize(new Dimension(30, 100));

            for (int j = 0; j < 3; j++) {
                boolean isNotNull = entries.size() > j; // Whether entry does exists

                // Position
                JLabel positionLabel = new JLabel();
                positionLabel.setFont(font.deriveFont(32f));
                positionLabel.setText(String.valueOf(j + 1));
                leaderboardPanel.add(positionLabel, new GridBagConstraints(0, j, 1, 1, 0.2, 0.0, 10,
                    0, new Insets(0, 0, 0, 0), 0, 0));

                // Details panel
                JPanel details = new JPanel();
                details.setOpaque(false);
                details.setLayout(new GridBagLayout());
                leaderboardPanel.add(details, new GridBagConstraints(1, j, 1, 1, 1.0, 0.0, 10,
                    GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

                // Score
                JLabel scoreLabel = new JLabel();
                scoreLabel.setFont(font.deriveFont(16f));
                scoreLabel.setText(isNotNull ? String.valueOf(entries.get(j).getScore()) : "-");
                details.add(scoreLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, 10,
                    GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

                // Nickname
                JLabel nicknameLabel = new JLabel();
                nicknameLabel.setFont(font.deriveFont(12f));
                nicknameLabel.setText(isNotNull ? entries.get(j).getNickname() : "-");
                details.add(nicknameLabel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, 10,
                    GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
            }

            buttonsPanel.add(leaderboardPanel, new GridBagConstraints(i, 1, 1, 1, 1.0, 0.0, 10,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        }
    }

    /**
     * Configure and run a GameWindow.
     */
    private void startGame(String level) {
        ReopenMainWindow windowCloseListener = new ReopenMainWindow();

        GameWindow gameInstance = new GameWindow(level, points -> {
            gameWindow.removeWindowListener(windowCloseListener);
            endGame(points, level);
            return null;
        });
        gameWindow = new JFrame("CloudRush - " + level.toUpperCase()); // Change program title
        gameWindow.setContentPane(gameInstance.mainPanel);
        gameWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Not exit prog if game ends
        gameWindow.pack();
        gameWindow.setMinimumSize(new Dimension(950, 650));
        gameWindow.setLocationRelativeTo(null); // Show window in the center of the screen
        gameWindow.addWindowListener(windowCloseListener); // Reopen main window when game ends
        gameWindow.setVisible(true);
        gameWindow.setResizable(false);
        window.setVisible(false);
    }

    /**
     * Function invoked by GameWindow when game is over. Used to show EndWindow.
     * @param points number of points collected by player
     * @param level the level
     */
    private void endGame(int points, String level) {
        // Close GameWindow
        gameWindow.dispose();

        // Create EndWindow to get player's nickname and save score
        JFrame endWindow = new JFrame("CloudRush - " + Language.getString("gameOver"));
        EndWindow endWindowInstance = new EndWindow(points, level, (UnaryOperator<Void>) unused -> {
            endWindow.dispose();
            loadLeaderBoard();
            window.setVisible(true);
            return null;
        });
        endWindow.setContentPane(endWindowInstance.backgroundPanel);
        endWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dont exit prog if game ends
        endWindow.pack();
        endWindow.setMinimumSize(new Dimension(360, 280));
        endWindow.addWindowListener(new ReopenMainWindow()); // Reopen main window when game ends
        endWindow.setLocationRelativeTo(null); // Show window in the center of the screen
        endWindow.setVisible(true);
        endWindow.setResizable(false);
    }

    /**
     * Reopen the main (start) window when game window is closed.
     */
    public static class ReopenMainWindow extends WindowAdapter {
        @Override
        public void windowClosed(WindowEvent e) {
            window.revalidate();
            window.repaint();
            window.setVisible(true);
        }
    }
}

