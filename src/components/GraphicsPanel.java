package components;

import java.awt.Graphics;
import java.util.function.Consumer;
import javax.swing.JPanel;

/**
 * Component inheriting from JPanel, extended with additional function, declared
 * during initialization, used to interact with component's Graphics object.
 * It's used as 2D canvas element for GameEngine and also as background image
 * handler for other windows.
 */
public class GraphicsPanel extends JPanel {
    /**
     * Initialize GraphicsPanel.
     */
    public GraphicsPanel() {
        super();
        setOpaque(false);
    }

    /**
     * Function executed every time component is repainted on the
     * screen. Used to interact with component's Graphics.
     */
    public Consumer<Graphics> paintHandler;

    /**
     * Override of JPanel's function. Extended only to execute paintHandler
     * declared during component initialization.
     *
     * @param graphics Graphics object of component.
     */
    @Override
    protected void paintComponent(Graphics graphics) {
        if (paintHandler != null) {
            paintHandler.accept(graphics);
        }
        super.paintComponent(graphics);
    }

}
