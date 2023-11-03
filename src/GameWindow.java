import components.GameEngine;
import components.GraphicsPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.function.Function;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

/**
 * The window where a game is played in.
 */
public class GameWindow {
    public JPanel mainPanel; // Public to attach it to gameWindow instance in MainWindow.java
    private GraphicsPanel canvas; // Game's main drawing canvas
    private GameEngine game; // Game instance

    /**
     * Initialize GameWindow.
     * @param level The level to load
     */
    public GameWindow(String level, Function<Integer, Void> onEnd) {
        canvas = new GraphicsPanel();
        canvas.paintHandler = this::onDraw;
        canvas.repaint();

        // The panel where the game is played in/on.
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.add(canvas, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 10,
            GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0)); // Spacing

        // Get game instance with properties
        game = new GameEngine(level);
        game.onEnd = onEnd;

        declareControls(); // Create key bindings
        startGraphicsEngine(); // Start animation timer
    }

    /**
     * Method used to create and assign keyboard strokes handlers.
     */
    private void declareControls() {
        // Variable to limit lightnings generated during single press of Enter key.
        // It's one-element array, because it needs to be 'final' to be accessed by
        // nested class (namely AbstractAction), but also it needs to be modified.
        final boolean[] generatedLightning = {false};

        // Declare key bindings and adding it to the inputmap
        KeyStroke leftPressed = KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false);
        KeyStroke rightPressed = KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false);
        KeyStroke upPressed = KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false);
        KeyStroke downPressed = KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false);
        KeyStroke spacePressed = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false);
        KeyStroke enterPressed = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
        mainPanel.getInputMap().put(leftPressed, "left_pressed");
        mainPanel.getInputMap().put(rightPressed, "right_pressed");
        mainPanel.getInputMap().put(upPressed, "up_pressed");
        mainPanel.getInputMap().put(downPressed, "down_pressed");
        mainPanel.getInputMap().put(spacePressed, "space_pressed");
        mainPanel.getInputMap().put(enterPressed, "enter_pressed");

        KeyStroke leftReleased = KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true);
        KeyStroke rightReleased = KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true);
        KeyStroke upReleased = KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true);
        KeyStroke downReleased = KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true);
        KeyStroke spaceReleased = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true);
        KeyStroke enterReleased = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true);
        mainPanel.getInputMap().put(leftReleased, "left_released");
        mainPanel.getInputMap().put(rightReleased, "right_released");
        mainPanel.getInputMap().put(upReleased, "up_released");
        mainPanel.getInputMap().put(downReleased, "down_released");
        mainPanel.getInputMap().put(spaceReleased, "space_released");
        mainPanel.getInputMap().put(enterReleased, "enter_released");

        // Declare event handlers on key press
        mainPanel.getActionMap().put("left_pressed", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                game.goingLeft = true;
            }
        });
        mainPanel.getActionMap().put("right_pressed", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                game.goingRight = true;
            }
        });
        mainPanel.getActionMap().put("up_pressed", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                game.goingUp = true;
            }
        });
        mainPanel.getActionMap().put("down_pressed", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                game.goingDown = true;
            }
        });
        mainPanel.getActionMap().put("space_pressed", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                game.raining = true;
            }
        });
        mainPanel.getActionMap().put("enter_pressed", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (!generatedLightning[0]) {
                    generatedLightning[0] = true;
                    game.makeLightning();
                }
            }
        });

        // Same but on key release
        mainPanel.getActionMap().put("enter_released", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                generatedLightning[0] = false;
            }
        });
        mainPanel.getActionMap().put("left_released", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                game.goingLeft = false;
            }
        });
        mainPanel.getActionMap().put("right_released", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                game.goingRight = false;
            }
        });
        mainPanel.getActionMap().put("up_released", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                game.goingUp = false;
            }
        });
        mainPanel.getActionMap().put("down_released", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                game.goingDown = false;
            }
        });
        mainPanel.getActionMap().put("space_released", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                game.raining = false;
            }
        });
    }

    /**
     * Start game timer.
     */
    private void startGraphicsEngine() {
        // Using javax.swing.Timer to increase precision
        Timer timer = new Timer(0, e -> {
            // Triggers game.tick() in onDraw()
            canvas.repaint();
        });
        timer.setRepeats(true); // Repeat timer callback infinite times
        timer.setDelay(17); // ~60 FPS
        timer.start();
    }

    /**
     * Redraw callback of main canvas, used to draw all game object.
     * @param g Graphics of GraphicsPanel GUI component
     */
    private void onDraw(Graphics g) {
        // Set most optimal interpolation (for faster rendering)
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        // Pass graphics to game engine and trigger game repaint
        game.canvas = g;
        game.tick();
    }
}
