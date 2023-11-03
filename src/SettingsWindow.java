import components.GraphicsPanel;
import components.Language;
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
import java.util.function.UnaryOperator;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

/**
 * The window displayed after the game is over, where user can enter nickname for leaderboard.
 */
public class SettingsWindow {
    // Public property of main GUI element to attach it as ContentPane to
    // frame window created by MainWindow
    public GraphicsPanel backgroundPanel;

    /**
     * Initialize SettingsWindow.
     *
     * @param onSubmit callback
     */
    public SettingsWindow(UnaryOperator<Integer> onSubmit) {
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
        Image i = new ImageIcon(resource).getImage();

        // Background
        backgroundPanel = new GraphicsPanel();
        backgroundPanel.paintHandler = (g) -> {
            // Set sky-blue background
            g.setColor(Color.decode("#87CEEB"));
            g.fillRect(0, 0, backgroundPanel.getWidth(), backgroundPanel.getHeight());

            // Draw background image
            g.drawImage(i, 0, 0, backgroundPanel.getWidth(), backgroundPanel.getHeight(), null);
        };
        backgroundPanel.setLayout(new GridBagLayout());
        backgroundPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Title
        JLabel titleLabel = new JLabel();
        titleLabel.setFont(font.deriveFont(32f));
        titleLabel.setForeground(new Color(-1));
        titleLabel.setText("CloudRush");
        backgroundPanel.add(titleLabel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, 10, 0,
            new Insets(0, 0, 0, 0), 0, 0)); // Spacing

        // Subtitle
        JLabel subtitleLabel = new JLabel();
        subtitleLabel.setFont(font.deriveFont(16f));
        subtitleLabel.setForeground(new Color(-1));
        subtitleLabel.setText(Language.getString("settings"));
        backgroundPanel.add(subtitleLabel, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, 10, 0,
            new Insets(5, 0, 0, 0), 0, 0)); // Spacing

        // Language label
        JLabel languageLabel = new JLabel();
        languageLabel.setFont(font.deriveFont(14f));
        languageLabel.setForeground(new Color(-1));
        languageLabel.setText(Language.getString("language"));
        backgroundPanel.add(languageLabel, new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0, 10, 0,
            new Insets(10, 0, 0, 0), 0, 0)); // Spacing

        // Language input
        JComboBox<String> languageSelect = new JComboBox<>(
            new String[]{"English", "Nederlands (Dutch)", "Polski (Polish)"});
        languageSelect.setFont(font.deriveFont(20f));
        languageSelect.setSelectedIndex(Language.code);
        languageSelect.setMinimumSize(new Dimension(100, 50));
        backgroundPanel.add(languageSelect, new GridBagConstraints(0, 5, 1, 1, 1.0, 0.0, 10,
            GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0)); // Spacing

        // Submit button
        JButton submitButton = new JButton();
        submitButton.setFont(font.deriveFont(18f));
        submitButton.setText(Language.getString("save"));
        submitButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSubmit.apply(languageSelect.getSelectedIndex());
            }
        });
        backgroundPanel.add(submitButton, new GridBagConstraints(0, 6, 1, 1, 1.0, 0.0, 10,
            GridBagConstraints.HORIZONTAL, new Insets(20, 0, 0, 0), 0, 16)); // Spacing

    }

}
