import components.GraphicsPanel;
import components.Language;
import components.Leaderboard;
import components.LeaderboardEntry;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.function.Function;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * The window displayed after the game is over, where user can enter nickname for leaderboard.
 */
public class EndWindow {
    // Public property of main GUI element to attach it as ContentPane to
    // frame window created by MainWindow
    public GraphicsPanel backgroundPanel;

    private final String level; // The game level name
    private final Function<Void, Void> onSubmit; // Callback function
    private final JLabel scoreLabel; // Used to get score
    private final JTextField nicknameInput; // Used to get nickname

    /**
     * Initialize EndWindow.
     * @param points Amount of points
     * @param level The level played
     * @param onSubmit callback
     */
    public EndWindow(int points, String level, Function<Void, Void> onSubmit) {
        this.onSubmit = onSubmit;
        this.level = level;

        // Import and load custom font from resources
        InputStream is = this.getClass().getClassLoader()
            .getResourceAsStream("joystix_monospace.otf");
        Font font;
        try {
            assert is != null;
            font = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }

        // Load background graphics
        URL resource = getClass().getClassLoader().getResource("main_background.png");
        assert resource != null;
        Image img = new ImageIcon(resource).getImage();

        // Background
        backgroundPanel = new GraphicsPanel();
        backgroundPanel.paintHandler = (g) -> {
            // Set sky-blue background
            g.setColor(Color.decode("#87CEEB"));
            g.fillRect(0, 0, backgroundPanel.getWidth(), backgroundPanel.getHeight());

            // Draw background image
            g.drawImage(img, 0, 0, backgroundPanel.getWidth(), backgroundPanel.getHeight(), null);
        };
        backgroundPanel.setLayout(new GridBagLayout());
        backgroundPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Title
        JLabel gameOverLabel = new JLabel();
        gameOverLabel.setFont(font.deriveFont(32f));
        gameOverLabel.setForeground(new Color(-1));
        gameOverLabel.setText(Language.getString("gameOver"));
        backgroundPanel.add(gameOverLabel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, 10, 0,
            new Insets(0, 0, 0, 0), 0, 0)); // Spacing

        // Score label
        JLabel scoreDescriptionLabel = new JLabel();
        scoreDescriptionLabel.setFont(font.deriveFont(14f));
        scoreDescriptionLabel.setForeground(new Color(-1));
        scoreDescriptionLabel.setText(Language.getString("score"));
        backgroundPanel.add(scoreDescriptionLabel, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, 10, 
            0, new Insets(10, 0, 0, 0), 0, 0)); // Spacing

        // Actual score
        scoreLabel = new JLabel();
        scoreLabel.setFont(font.deriveFont(28f));
        scoreLabel.setForeground(new Color(-1));
        scoreLabel.setText(String.valueOf(points));
        backgroundPanel.add(scoreLabel, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, 10, 0,
            new Insets(0, 0, 0, 0), 0, 0)); // Spacing

        // Nickname label
        JLabel nicknameLabel = new JLabel();
        nicknameLabel.setFont(font.deriveFont(14f));
        nicknameLabel.setForeground(new Color(-1));
        nicknameLabel.setText(Language.getString("nickname"));
        backgroundPanel.add(nicknameLabel, new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0, 10, 0,
            new Insets(10, 0, 0, 0), 0, 0)); // Spacing

        // Nickname input
        nicknameInput = new JTextField();
        nicknameInput.setFont(font.deriveFont(28f));
        nicknameInput.setText("");
        nicknameInput.setMinimumSize(new Dimension(200, 36));
        backgroundPanel.add(nicknameInput, new GridBagConstraints(0, 5, 1, 1, 1.0, 0.0, 10,
            GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0)); // Spacing

        // Submit button
        JButton submitButton = new JButton();
        submitButton.setFont(font.deriveFont(18f));
        submitButton.setText(Language.getString("submit"));
        submitButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitScore();
            }
        });
        backgroundPanel.add(submitButton, new GridBagConstraints(0, 6, 1, 1, 1.0, 0.0, 10,
            GridBagConstraints.HORIZONTAL, new Insets(20, 0, 0, 0), 0, 16)); // Spacing

    }

    private void submitScore() {
        String nickname = nicknameInput.getText() // Get nickname with max 24 characters
            .substring(0, Math.min(nicknameInput.getText().length(), 24));

        // Save to the leaderboard
        Leaderboard leaderboard = new Leaderboard();
        LeaderboardEntry entry = new LeaderboardEntry(level, scoreLabel.getText(), nickname);
        leaderboard.addToLeaderboard(entry);
        onSubmit.apply(null);
    }
}
