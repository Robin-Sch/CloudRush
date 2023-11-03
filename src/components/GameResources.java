package components;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 * All the resources of the game (think of images, font, etc.).
 */
public class GameResources {
    public final Font font;
    public final Image backgroundGraphics;
    public final Image waterDropGraphics; // normal sprite
    public final Image[] flowerpotGraphics;
    public final Image[] cloudSprite;
    public final Image[] waterDropSprite; // splashed droplet sprites
    public final Image[][] coinsSprite;
    public final Image[] lightningSprite;
    public final Image[][] peopleSprites;
    public final Image[][] lanternGraphics;
    public final Image[] bonusSprite;

    /**
     * Initialize GameResources.
     * @param level The level of the game
     */
    public GameResources(String level) {
        flowerpotGraphics = new Image[5]; // 5 different flower pots
        cloudSprite = new Image[16]; // 16 different cloud sprites
        waterDropSprite = new Image[5]; // 5 different droplet (splashed) sprite
        coinsSprite = new Image[2][6]; // 2 coins with 6 different sprites each
        lightningSprite = new Image[7]; // 7 different sprites
        peopleSprites = new Image[2][16]; // 2 persons with 16 different sprites each
        lanternGraphics = new Image[3][2]; // 3 lanterns with 2 different sprites each (on and off)
        bonusSprite = new Image[18]; // 18 different bonus sprites

        InputStream is = this.getClass().getClassLoader()
            .getResourceAsStream("joystix_monospace.otf");

        try {
            assert is != null;
            font = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }

        // Static graphics
        backgroundGraphics = loadImage("bg-" + level + ".png");
        waterDropGraphics = loadImage("drop/main.png");
        for (int i = 0; i < 5; i++) {
            flowerpotGraphics[i] = loadImage("flowers/" + i + ".png");
        }

        // Sprites
        for (int i = 0; i < 16; i++) {
            cloudSprite[i] = loadImage("cloud/" + i + ".png");
        }
        for (int i = 0; i < 5; i++) {
            waterDropSprite[i] = loadImage("drop/" + i + ".png");
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 6; j++) {
                coinsSprite[i][j] = loadImage("coin/" + i + "-" + j + ".png");
            }
        }
        for (int i = 0; i < 7; i++) {
            lightningSprite[i] = loadImage("lightning/" + i + ".png");
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 16; j++) {
                peopleSprites[i][j] = loadImage("people/" + i + "-" + j + ".png");
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                lanternGraphics[i][j] = loadImage("lanterns/" + i + "-" + j + ".png");
            }
        }
        for (int i = 0; i < 18; i++) {
            bonusSprite[i] = loadImage("bonus/" + i + ".png");
        }
    }

    /**
     * Load an image.
     * @param name Image file path + name
     * @return The image
     */
    private Image loadImage(String name) {
        URL resource = getClass().getClassLoader().getResource(name);
        assert resource != null;
        return new ImageIcon(resource).getImage();
    }
}
