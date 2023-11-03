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
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

/**
 * The window displayed after the game is over, where user can enter nickname for leaderboard.
 */
public class InstructionsWindow {
    // Public property of main GUI element to attach it as ContentPane to
    // frame window created by MainWindow
    public GraphicsPanel backgroundPanel;

    /**
     * Initialize InstructionsWindow.
     * @param window The game Window
     */
    public InstructionsWindow(Window window) {
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
        URL resource = getClass().getClassLoader().getResource("instructions_background.jpg");
        assert resource != null;
        Image back = new ImageIcon(resource).getImage();

        // Background
        backgroundPanel = new GraphicsPanel();
        backgroundPanel.paintHandler = (g) -> {
            // Set sky-blue background
            g.setColor(Color.decode("#87CEEB"));
            g.fillRect(0, 0, backgroundPanel.getWidth(), backgroundPanel.getHeight());

            // Draw background image
            g.drawImage(back, 0, -50, backgroundPanel.getWidth(),
                backgroundPanel.getHeight() + 50, null);
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
        subtitleLabel.setText(Language.getString("instructions"));
        backgroundPanel.add(subtitleLabel, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, 10, 0,
            new Insets(5, 0, 0, 0), 0, 0)); // Spacing

        // Actual instructions
        JTextArea descriptionLabel = new JTextArea(); // TextArea is used to use word wrap
        descriptionLabel.setWrapStyleWord(true);
        descriptionLabel.setOpaque(false);
        descriptionLabel.setEditable(false);
        descriptionLabel.setMaximumSize(new Dimension(300, 200));
        descriptionLabel.setFont(font.deriveFont(14f));
        descriptionLabel.setForeground(new Color(-1));
        descriptionLabel.setText(Language.getString("instructionsDescription"));

        // Instructions container (scrollable)
        JScrollPane descriptionPanel = new JScrollPane(descriptionLabel);
        descriptionPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        descriptionPanel.setOpaque(false);
        descriptionPanel.getViewport().setOpaque(false);
        backgroundPanel.add(descriptionPanel, new GridBagConstraints(0, 3, 1, 1, 1.0, 10.0, 10, 
            GridBagConstraints.BOTH, new Insets(140, 0, 0, 0), 0, 0)); // Spacing
        descriptionPanel.getVerticalScrollBar().setValue(0);

        // Window close button
        JButton closeButton = new JButton();
        closeButton.setFont(font.deriveFont(18f));
        closeButton.setText(Language.getString("close"));
        closeButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.dispose();
            }
        });
        backgroundPanel.add(closeButton, new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0, 10,
            GridBagConstraints.HORIZONTAL, new Insets(20, 0, 0, 0), 0, 16)); // Spacing

    }
}
