package components;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import objects.Coin;
import objects.Effect;
import objects.EffectType;
import objects.Flowerpot;
import objects.Lantern;
import objects.Lightning;
import objects.Person;
import objects.Raindrop;

/**
 * The Game Engine (instance) with all the logic of the game.
 */
public class GameEngine {
    /**
     * Initialize GameEngine.
     *
     * @param level The level of the game
     */
    public GameEngine(String level) {
        startTime = System.currentTimeMillis();

        switch (level) {
            case "eindhoven": {
                levelDuration = 60 * 1000; // 1 minute
                positiveRainPoints = 10;
                negativeRainPoints = 20;
                positiveLightningPoints = 200;
                negativeLightningPoints = 500;
                levelSpeed = 0.5;
                peopleSpeedMultiplier = 0.5;
                backgroundOffset = 2;
                break;
            }
            case "warsaw": {
                levelDuration = 3 * 60 * 1000; // 3 minutes
                positiveRainPoints = 10;
                negativeRainPoints = 30;
                positiveLightningPoints = 200;
                negativeLightningPoints = 600;
                levelSpeed = 0.5;
                peopleSpeedMultiplier = 1;
                break;
            }
            case "newyork": {
                levelDuration = 3 * 60 * 1000; // 3 minutes
                positiveRainPoints = 10;
                negativeRainPoints = 30;
                positiveLightningPoints = 200;
                negativeLightningPoints = 600;
                levelSpeed = 0.75;
                peopleSpeedMultiplier = 1.3;
                backgroundOffset = 20;
                break;
            }
            default: {
                break;
            }
        }

        this.resources = new GameResources(level);
    }

    /**
     * Function invoked after game is over with user's score as
     * parameter. It's used by MainWindow to show EndWindow with
     * certain score.
     */
    public Function<Integer, Void> onEnd;

    // Resources
    private final GameResources resources; // All the game's resources like background and sprites
    public Graphics canvas;
    private int frameWidth;
    private int frameHeight;

    // Player state variables
    private final long startTime; // When the game started, used to accurately display the time
    private double points = 0; // Player's score
    private double cloudX = 100; // Cloud's X position
    private double cloudY = 100; // Cloud's Y position
    private int frame = 0; // Frame counter, for animation purposes

    // Level properties, variables are public since Effect class uses these
    public double positivePointsMultiplier = 1; // Used to give more points as a bonus
    public double negativePointsMultiplier = 1; // Used to give more negative points as a debuff
    public double leftMultiplier = 1; // How fast moving to the left
    public double rightMultiplier = 1; // How fast moving to the right
    public double upMultiplier = 1; // How fast moving upwards
    public double downMultiplier = 1; // How fast moving downwards
    public double rainIntensity = 3; // How many drops (1 -> 17 drops/s, 17 -> 1 drop/s)
    public int rainQuality = 0; // -1 -> acid, 0 -> normal, 1 -> bigger drops
    public double wind = 0; // wind>0 - to right, wind<0 - to left (affecting rain physics)
    public double flowerGenerationFrequency = 600; // There will be a flower every n pixels
    public double peopleGenerationFrequency = 4; // There will always be n people on map
    public double lanternGenerationFrequency = 600; // There will be a lantern every n pixels
    public double effectsGenerationFrequency = 4; // There will always be n on map
    private final int animationDelay = 3; // Make every "frame" of graphics last n window frames
    private double effectSize = 30; // The size of the floating effect sprite 
    // Level properties which change each level
    private long levelDuration; // 3 minutes
    private int positiveRainPoints; // How many points you get for watering the flowers correctly
    private int negativeRainPoints; // How many (negative) points you get for watering the people
    private int positiveLightningPoints; // How many points you get for turning on street lamps
    private int negativeLightningPoints; // How many points you get for turning off street lamps
    public double levelSpeed; // How fast map is moving
    public double peopleSpeedMultiplier; // How fast people are walking
    private int backgroundOffset; // Draw image background with an offset to avoid overlapping

    // Game objects
    ArrayList<Raindrop> raindrops = new ArrayList<>();
    ArrayList<Lightning> lightnings = new ArrayList<>();
    ArrayList<Flowerpot> flowerpots = new ArrayList<>();
    ArrayList<Coin> coins = new ArrayList<>();
    ArrayList<Lantern> lanterns = new ArrayList<>();
    ArrayList<Person> people = new ArrayList<>();
    ArrayList<Effect> effects = new ArrayList<>();
    ArrayList<EffectType> availableEffects = new ArrayList<>(List.of(EffectType.values()));

    // To prevent sounds from playing all at the same time, but rather wait a bit before the
    // previous one has stopped playing
    private final HashMap<String, Long> soundDelays = new HashMap<>();

    // Size of graphic images, variables are public since Effect class uses these
    public double flowerWidth = 120;
    public double flowerHeight = flowerWidth * (1417d / 2700d);
    public int personWidth = 70;
    public double personHeight = personWidth * (920d / 600d);
    public double lanternWidth = 300;
    public double lanternHeight = lanternWidth * (2280d / 2801d);
    public double dropWidth = 30;
    public double dropHeight = dropWidth * (122d / 200d);
    public double cloudSize = 200;
    public double cloudHeight = cloudSize * (420d / 650d);

    // Controlling the cloud actions, public since GameWindow uses these
    public boolean goingLeft = false; // Is left (A) key pressed
    public boolean goingRight = false; // Is right (D) key pressed
    public boolean goingUp = false; // Is up (W) key pressed
    public boolean goingDown = false; // Is down (S) key pressed
    public boolean raining = false; // Is space key pressed

    /**
     * Tick (update) the game.
     */
    public void tick() {
        // End game when no time left
        long timeLeft = levelDuration - (System.currentTimeMillis() - startTime);
        if (timeLeft < 0) {
            onEnd.apply((int) points);
            return;
        }

        // Update canvas size
        Rectangle bounds = canvas.getClipBounds();
        frameWidth = bounds.width;
        frameHeight = bounds.height;

        // Execute tick
        frame++;
        generateRain();
        generateFlowerpots();
        generateLanterns();
        generatePeople();
        generateEffects();
        detectRainHit();
        detectEffectHit();
        drawBackground();
        drawEffects();
        drawLanterns();
        drawFlowerpots();
        drawPeople();
        drawRaindrops();
        drawLightning();
        drawCoins();
        drawCloud();
        drawScore();
        drawBuffs();
    }

    /**
     * Create lightning.
     */
    public void makeLightning() {
        Lightning lightning = new Lightning() {
            {
                this.x = cloudX;
                // Generate lightning in part of the cloud that is always covered,
                // no matter which animation frame is active, which is 1/3 of cloud height
                this.y = cloudY + cloudHeight / 3;
                this.state = 0;
                // Default height is to ground, can be changed in hit detection
                this.height = frameHeight - this.y;
            }
        };
        lightnings.add(lightning);
        playSound("lightning");
        detectLightningHit(lightning);
    }

    /**
     * Play a sound.
     * @param name The name of the sound
     */
    private void playSound(String name) {
        // Check if some sound is already playing
        long lastPlayedSound = soundDelays.containsKey(name) ? soundDelays.get(name) : 0;

        // Play sounds on hit, making sure only to allow one sound per 100 milleseconds to prevent
        // sounds from being "overlapped" with a very small offset (which makes it sound weird)
        if (System.currentTimeMillis() - lastPlayedSound > 100) {
            soundDelays.put(name, System.currentTimeMillis());
            new Thread(() -> {
                try {
                    // Get the Sound from the file system
                    URL sound = getClass().getClassLoader().getResource("sounds/" + name + ".wav");
                    assert sound != null; // Make sure sound exists
                    AudioInputStream coinSound = AudioSystem.getAudioInputStream(sound); // stream

                    // Get the clip and play the sound
                    Clip clip = AudioSystem.getClip();
                    clip.open(coinSound);
                    clip.setFramePosition(0);

                    // Whenever the sound is done playing, close the clip
                    clip.addLineListener(e -> {
                        if (e.getType() == LineEvent.Type.STOP) {
                            clip.close();
                        }
                    });
                    clip.start(); // Actually play the sound
                } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }

    /**
     * Generate rain drops.
     */
    private void generateRain() {
        // Generate rain if user is pressing space key
        if (raining) {
            // Limit raindrops count
            if (frame % rainIntensity == 0) {
                // Add raindrop object to game
                raindrops.add(new Raindrop() {
                    {
                        // Set raindrop's position randomly across cloud width
                        // Math.random() - 0.5 for randimizer, and cloudSize * .6 is minimal size
                        // of all cloud sprites (due to animation some sprites are smaller than 1)
                        this.x = cloudX + (Math.random() - 0.5) * cloudSize * 0.6;
                        // Create drop behind the cloud, at widest part
                        this.y = cloudY + cloudHeight / 3;
                        speed = 2; // Default speed
                        quality = rainQuality; // Copy acid property to class instance
                        state = -1; // State of splash animation sprite
                    }
                });
            }
        }
    }

    /**
     * Generate flower pots.
     */
    private void generateFlowerpots() {
        // Get last flowerpot position
        double lastPos = flowerpots.isEmpty() ? 0 : flowerpots.get(flowerpots.size() - 1).x;

        // Check if new flowerpot needs to be added within declared in game properties range
        if (flowerpots.isEmpty() || frameWidth * 2 - lastPos > flowerGenerationFrequency) {
            // Add flowerpot object
            flowerpots.add(new Flowerpot() {
                {
                    // Randomize flowerpot location, but within declared range
                    this.x = lastPos + flowerGenerationFrequency * 0.5
                        + flowerGenerationFrequency * Math.random();
                    // Randomize flowerpot graphics (different flower colors)
                    this.type = (int) (Math.random() * 5);
                }
            });
        }
    }

    /**
     * Generate lanterns.
     */
    private void generateLanterns() {
        // Get last lantern position
        double lastPos = lanterns.isEmpty() ? 0 : lanterns.get(lanterns.size() - 1).x;

        // Check if new lantern needs to be added within declared in game properties range
        if (lanterns.isEmpty() || frameWidth * 2 - lastPos > lanternGenerationFrequency) {
            // Add flowerpot object
            lanterns.add(new Lantern() {
                {
                    // Randomize lantern location within declared range, to avoid same distances
                    this.x = lastPos + lanternGenerationFrequency * 0.5 
                        + lanternGenerationFrequency * Math.random();
                    // Randomize lantern graphics
                    this.type = (int) (Math.random() * 3);
                }
            });
        }
    }

    /**
     * Generate people.
     */
    private void generatePeople() {
        // Make sure there's a constant amount of people in frame
        while (people.size() < peopleGenerationFrequency) {

            // Randomize from which direction person is approaching
            boolean left = Math.random() > 0.5; // Is the person going from left?
            people.add(new Person() {
                {
                    this.x = left ? -personWidth : frameWidth + personWidth;
                    // Give a random speed
                    this.speed = (left ? 1 : -1) * (1 + 5 * Math.random());
                    // Randomize person graphics (male/female)
                    this.type = (int) (Math.random() * 2);
                    // Set animation position to the first frame
                    this.state = 0;
                }
            });
        }
    }

    /**
     * Generate (de)buf effects.
     */
    private void generateEffects() {
        // Generate as many effects as many types there are or as many is allowed by 
        // `effectsGenerationFrequency`, whichever is smaller. It's to prevent same effect twice
        while (effects.size() < Math.min(EffectType.values().length, effectsGenerationFrequency)) {
            // Randomize which effect is generated
            int effectIndex = (int) (Math.random() * availableEffects.size());
            EffectType effectType = availableEffects.remove(effectIndex);

            // Get position of last visible effect, to generate new one behind that one, to avoid
            // the effect appearing in the middle of the sky, but to gently slide from right side
            double lastPos = Math.max(frameWidth,
                effects.isEmpty() ? frameWidth : effects.get(effects.size() - 1).x);
            effects.add(new Effect(effectType) {
                {
                    this.positive = Math.random() > 0.5; // Randomize if positive or neg effect
                    this.state = (int) (Math.random() * 360); // Randomize sprite animation
                    // Randomize position, but set some minimal distance from the previous one
                    this.x = lastPos + 400 * Math.random() + 100;
                    // Set vertical position such that cloud will be able to collect this effect
                    this.y = Math.random() * (frameHeight - 400);
                }
            });
        }
    }

    /**
     * Detect if water droplet(s) hit a person or flower pot.
     */
    private void detectRainHit() {
        for (Raindrop drop : raindrops) {
            // Bottom pixel of the water droplet
            double bottomOfDroplet = drop.y + dropHeight;

            // Check if raindrop reached the ground
            if (bottomOfDroplet >= frameHeight) {
                // Start raindrop splash animation
                drop.state = 0;
                drop.y = frameHeight - dropHeight * 2; // Fix position to make animation smooth
            } else if (bottomOfDroplet > frameHeight - personHeight
                    && bottomOfDroplet <= frameHeight - flowerHeight / 2) {
                // Check if raindrop is below top level of people (frameHeight - personHeight),
                // but still above the top of the flower pots (frameHeight - flowerHeight / 2)

                // Check if raindrop fell on a person
                for (Person person : people) {
                    // Horizontal hit detection of drop and person. We target middle of the drop,
                    // which has always 30px width, so +15px to get center of it. Persons have
                    // different sprites, so we should limit it's hitbox to not trigger hit when
                    // drop fell just before/after the person (hitbox it from 30% to 70% of person
                    // width). We subtract personWidth if the person is going in opposite direction
                    // (when it's speed is negative), because then x is the other side of person

                    double middle = drop.x + 15; // Middle of the droplet
                    int offset = person.speed < 0 ? personWidth : 0;
                    double left = person.x + personWidth * 0.3 - offset;
                    double right = person.x + personWidth * 0.7 - offset;
                    if (middle >= left && middle <= right) { // Middle of drop is between L and R
                        // Play sound and decrease points, since drop hit a person
                        playSound("mistake");
                        points -= negativeRainPoints * negativePointsMultiplier;

                        // Start splash animation & fix raindrop position to make animation smooth
                        drop.state = 0;
                        drop.y = frameHeight - personHeight - dropHeight;

                        // Add coin object
                        coins.add(new Coin() {
                            {
                                this.x = drop.x + 15; // Middle of droplet
                                this.y = frameHeight - personHeight; // Top of person
                                this.positive = false; // Made a mistake, so negative coin
                                this.state = 0; // Start with first sprite of coin (for animation)
                            }
                        });
                    }
                }
            } else if (bottomOfDroplet > frameHeight - flowerHeight / 2) {
                // Check if water droplet has reached the top of the flower pots

                // Run through all flowerpots
                for (Flowerpot flowerpot : flowerpots) {
                    // Hit detection of drop and flowerpot. We target middle of the drop, which has 
                    // always 30px width, so +15 to get center of it. Due to map movement we should 
                    // limit flowerpot hitbox to 90% of its width, without that, the splashing 
                    // animation will be displayed on flowerpot which is no longer there,
                    // because of the map movement.
                    double middle = drop.x + 15; // Middle of the droplet
                    double left = flowerpot.x;
                    double right = flowerpot.x + flowerWidth * 0.9;

                    if (middle >= left && middle <= right) { // Middle of drop is between L and R
                        double earnedPoints = positiveRainPoints * positivePointsMultiplier;
                        if (drop.quality == -1) { // Acid
                            earnedPoints *= -1; // remove points
                        } else if (drop.quality == 1) { // Droplet contains double amount of water
                            earnedPoints *= 2; // extra points
                        }

                        points += earnedPoints; // Add (or remove) points from score

                        // Start splash animation & fix raindrop position to make animation smooth
                        drop.state = 0;
                        drop.y = frameHeight - flowerHeight / 2 - dropHeight;

                        if (drop.quality == -1) { // Acid
                            playSound("mistake");
                            // Add negative coin object
                            coins.add(new Coin() {
                                {
                                    this.x = drop.x + 15; // Middle of droplet
                                    this.y = frameHeight - flowerHeight / 2; // Top of flower pot
                                    this.positive = false; // Made a mistake, so negative coin
                                    this.state = 0; // Start with 1st coin sprite (for animation)
                                }
                            });
                        } else if (drop.quality == 0 || drop.quality == 1) {
                            // Add coin object
                            playSound("coin");
                            coins.add(new Coin() {
                                {
                                    this.x = drop.x + 15; // Middle of droplet
                                    this.y = frameHeight - flowerHeight / 2; // Top of flower pot
                                    this.positive = true; // Did a correct action
                                    this.state = 0; // Start with 1st coin sprite (for animation)
                                }
                            });
                        }

                        // Stop hit detection after 1st hit (since flower pots can't overlap)
                        // Whenever a flower pot is hit, it can't hit a second one
                        break;
                    }
                }
            }
        }
    }

    /**
     * Detect if lightning hit a person or lantern.
     * @param lightning The lightning object
     */
    private void detectLightningHit(Lightning lightning) {
        for (Lantern lantern : lanterns) {
            // Hit detection of lightning and lantern. Lantern has big sprite, and we want it to be
            // activated only when lightning hit directly the yellow area of lantern (the lamp),
            // that's why the hitbox of the lantern is limited to 40%-60% of its width.
            double middle = lightning.x; // Middle of the lightning
            double left = lantern.x + lanternWidth * 0.4;
            double right = lantern.x + lanternWidth * 0.6;

            // Check (lightning.y < frameHeight - lanternHeight) to see if lightning has reached
            // the top of the lanterns (lantern height), rest is just basic hit detection.
            if (lightning.y < frameHeight - lanternHeight && middle >= left && middle <= right) {
                lightning.height -= lanternHeight; // Lightning hits the lamp not the ground
                lantern.enabled = !lantern.enabled; // Toggle lantern on/off
                if (lantern.enabled) {
                    // Play sound and add points
                    playSound("coin");
                    points += positiveLightningPoints * positivePointsMultiplier;

                    // Add coin object
                    coins.add(new Coin() {
                        {
                            this.x = lightning.x; // X coordinate
                            this.y = frameHeight - lanternHeight; // Lamp of lantern
                            this.positive = true; // Did a correct action
                            this.state = 0; // Start with first sprite of coin (for animation)
                        }
                    });
                } else { // Player turned off a lantern (hit it twice), which is a mistake
                    // Play sound and remove points
                    playSound("mistake");
                    points -= negativeLightningPoints * negativePointsMultiplier;

                    // Add negative coin object
                    coins.add(new Coin() {
                        {
                            this.x = lightning.x; // X coordinate
                            this.y = frameHeight - lanternHeight; // Lamp of lantern
                            this.positive = false; // Made a mistake, so negative coin
                            this.state = 0; // Start with first sprite of coin (for animation)
                        }
                    });
                }
            }
        }

        for (Person person : people) {
            // Hit detection of person and lightning. We target whole hitbox of person, because 
            // lightning has a large effective area. The subtraction of personWidth is necessary to 
            // address situation when person is going in opposite direction (its speed is negative),
            // because then person.x is the other side of the person
            double middle = lightning.x; // Middle of the lightning
            int offset = person.speed < 0 ? personWidth : 0;
            double left = person.x - offset;
            double right = person.x + personWidth - offset;

            if (middle >= left && middle <= right) {
                lightning.height -= personHeight; // Make sure to hit the person not the ground

                coins.add(new Coin() {
                    {
                        this.x = lightning.x; // X coordinate
                        this.y = frameHeight - personHeight; // Head of person
                        this.positive = false; // Made a mistake, so negative coin
                        this.state = 0; // Start with first sprite of coin (for animation)
                    }
                });
                points -= negativeLightningPoints * negativePointsMultiplier;
                playSound("mistake");

                // Only one person can be hit by a single lightning
                break;
            }
        }
    }

    /**
     * Check if cloud touched a (de)buf effect item.
     */
    private void detectEffectHit() {
        for (int i = effects.size() - 1; i >= 0; i--) {
            Effect effect = effects.get(i);

            // Hit detection of cloud and effect in the sky. We want to be sure that effect is not 
            // in use (invisible), hence condition for effect.start==0 (meaning it hasn't started)
            // Then we're checking hitbox of always-visible part of cloud (cloud animation sprites 
            // don't always have same size) which is 25%-75% in horizontal, and 33%-66% in vertical
            if (effect.start == 0
                    && effect.x - effectSize / 2 > cloudX - cloudSize / 4
                    && effect.x - effectSize / 2 < cloudX + cloudSize / 4
                    && effect.y - effectSize / 2 > cloudY - cloudHeight / 3
                    && effect.y - effectSize / 2 < cloudY + cloudHeight / 3) {
                effect.start = System.currentTimeMillis(); // To keep track of duration of effect
                effect.duration = (((int) (Math.random() * 20)) + 5) * 1000; // Randomize duration
                if (effect.positive) {
                    playSound("effect-correct");
                } else {
                    playSound("effect-wrong");
                }
                effect.affect(this);
            }
        }
    }

    /**
     * Draw the city background.
     */
    private void drawBackground() {
        // Draw background, maintaining aspect ratio
        double imgHeight = frameHeight;
        double imgWidth = resources.backgroundGraphics.getWidth(null)
            * (imgHeight / resources.backgroundGraphics.getHeight(null));

        // How much of the background image is already drawn
        int animationOffset = (int) ((frame * levelSpeed) % imgWidth);

        // Repeat background to create infinite background city graphics
        for (int i = 0; i < frameWidth / imgWidth + 1; i++) {
            // Actually draw background
            canvas.drawImage(
                resources.backgroundGraphics, // background
                (int) (i * imgWidth) - backgroundOffset - animationOffset, // Destination X
                0, // Destination Y
                (int) ((i + 1) * imgWidth) - animationOffset, // Destination width
                (int) imgHeight, // Destination height
                0, // Source X
                0, // Source Y
                resources.backgroundGraphics.getWidth(null), // Source width
                resources.backgroundGraphics.getHeight(null), // Source height
                null
            );
        }
    }

    /**
     * Draw (de)buffs on the sky.
     */
    private void drawEffects() {
        ((Graphics2D) canvas).setStroke(new BasicStroke(6));
        for (int i = effects.size() - 1; i >= 0; i--) {
            Effect effect = effects.get(i);

            // Check if effect is active (so it's start property is set)
            if (effect.start != 0) {
                if (System.currentTimeMillis() - effect.start >= effect.duration) {
                    // Effect has ended, so we should remove it
                    effect.undo(this);
                    availableEffects.add(effect.type);
                    effects.remove(i);
                }
                continue;
            }

            effect.x -= levelSpeed; // Move each effect to the left, together with map

            // Remove effect (to free up memory) when it moves outside viewport
            if (effect.x + effectSize < 0) {
                availableEffects.add(effect.type);
                effects.remove(i);
            } else {
                Image sprite = resources.bonusSprite[effect.state++ / 2 % 16]; // Animated sprite

                // Draw effect
                canvas.drawImage(
                    sprite, // Effect sprite
                    (int) effect.x, // X
                    (int) effect.y, // Y
                    (int) effectSize, // Width
                    (int) effectSize, // Height
                    null
                );
            }
        }
    }

    /**
     * Draw the lanterns.
     */
    private void drawLanterns() {
        for (int i = lanterns.size() - 1; i >= 0; i--) {
            Lantern lantern = lanterns.get(i);

            lantern.x -= levelSpeed; // Move each lantern to the left, together with map

            // Remove lantern (to free up memory) when it moves outside viewport
            if (lantern.x + lanternWidth < 0) {
                lanterns.remove(i);
            } else {
                Image sprite = resources.lanternGraphics[lantern.type][lantern.enabled ? 1 : 0];

                // Actually draw lantern
                canvas.drawImage(
                    sprite, // Lantern sprite
                    (int) lantern.x, // X
                    (int) (frameHeight - lanternHeight), // Y
                    (int) lanternWidth, // Width
                    (int) lanternHeight, // Height
                    null
                );
            }
        }
    }

    /**
     * Draw the flower pots.
     */
    private void drawFlowerpots() {
        for (int i = flowerpots.size() - 1; i >= 0; i--) {
            Flowerpot flowerpot = flowerpots.get(i);

            flowerpot.x -= levelSpeed; // Move each flowerpot to the left, together with map

            // Remove flowerpot (to free up memory) when it moves outside viewport
            if (flowerpot.x + flowerWidth < 0) {
                flowerpots.remove(i);
            } else {
                Image sprite = resources.flowerpotGraphics[flowerpot.type];

                // Actually draw flowerpot
                canvas.drawImage(
                    sprite, // Flowerpot sprite
                    (int) flowerpot.x, // X
                    (int) (frameHeight - flowerHeight), // Y
                    (int) flowerWidth, // Width
                    (int) flowerHeight, // Height
                    null
                );
            }
        }
    }

    /**
     * Draw the people.
     */
    private void drawPeople() {
        for (int i = people.size() - 1; i >= 0; i--) {
            Person person = people.get(i);

            // Move each person to the left, together with map
            person.x += person.speed * peopleSpeedMultiplier;

            // Remove flowerpot (to free up memory) when it moves outside viewport
            if (person.x + personWidth < 0 || person.x - personWidth > frameWidth) {
                people.remove(i);
            } else {
                // Animate the sprite
                Image sprite = resources.peopleSprites[person.type][(person.state++ / 4) % 16];

                // Actually draw person
                canvas.drawImage(
                    sprite, // Animated person sprite
                    (int) person.x, // X
                    (int) (frameHeight - personHeight), // Y
                    (int) person.speed > 0 ? personWidth : -personWidth, // Inversed when backwards
                    (int) personHeight, // Height
                    null
                );
            }
        }
    }

    /**
     * Draw the rain droplets.
     */
    private void drawRaindrops() {
        // Draw raindrops
        for (int i = raindrops.size() - 1; i >= 0; i--) {
            Raindrop drop = raindrops.get(i);

            // If raindrop is (still) falling
            if (drop.state == -1) {
                // Move raindrop according to gravity (speed) and wind
                drop.y += 3 * drop.speed;
                drop.x -= levelSpeed + wind;

                // Actually draw droplet
                canvas.drawImage(
                    resources.waterDropGraphics, // Droplet sprite
                    (int) drop.x, // X
                    (int) drop.y, // Y
                    30, // Width
                    (int) dropHeight, // Height
                    null
                );
            } else { // If raindrop is splashing
                Image sprite = resources.waterDropSprite[drop.state++ / animationDelay];
                double ratio = resources.waterDropGraphics.getHeight(null)
                    / (double) resources.waterDropGraphics.getWidth(null);

                // Draw one sprite of the splash animation, next tick the next sprite is drawn to
                // create the animation effect (taking animationDely in consideration ofc)
                canvas.drawImage(
                    sprite, // Animated droplet sprite
                    (int) drop.x, // X
                    (int) drop.y, // Y
                    60, // Width
                    (int) (60 * ratio), // Height
                    null
                );

                // Remove raindrop after splash ends
                if (drop.state >= resources.waterDropSprite.length * animationDelay) {
                    raindrops.remove(i);
                }
            }
        }
    }

    /**
     * Draw lightning.
     */
    private void drawLightning() {
        for (int i = lightnings.size() - 1; i >= 0; i--) {
            Lightning lightning = lightnings.get(i);

            Image sprite = resources.lightningSprite[lightning.state++ / animationDelay];

            // Draw one sprite of the animation, next tick the next sprite is drawn to
            // create the animation effect (taking animationDely in consideration ofc)
            canvas.drawImage(
                sprite, // Animated lightning sprite
                (int) (lightning.x - (cloudSize / 4)), // X
                (int) lightning.y, // Y
                (int) (cloudSize / 2), // Width
                (int) lightning.height, // Height
                null
            );

            // Remove lightning after animation is done
            if (lightning.state >= resources.lightningSprite.length * animationDelay) {
                lightnings.remove(i);
            }
        }
    }

    /**
     * Draw the coins.
     */
    private void drawCoins() {
        double coinSize = 30;
        for (int i = coins.size() - 1; i >= 0; i--) {
            Coin coin = coins.get(i);
            coin.y -= coin.positive ? 2 : -2; // Animate coin going up

            // Add dynamic transparency to each coin (as its slowly disappearing when going up)
            Graphics2D g2 = (Graphics2D) canvas;
            AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                (float) (1 - coin.state++ / 30d));
            g2.setComposite(ac);

            int coinType = coin.positive ? 1 : 0; // Positive or negative coin
            int frameNumber = (coin.state / animationDelay) % resources.coinsSprite.length;
            Image sprite = resources.coinsSprite[coinType][frameNumber];

            // Actually draw coin
            canvas.drawImage(
                sprite, // Animated coin sprite
                (int) coin.x - 15, // X
                (int) coin.y - (coin.positive ? 15 : 0), // Y
                (int) coinSize, // Width
                (int) coinSize, // Height
                null
            );

            // Remove coin after animation
            if (coin.state > 30) {
                coins.remove(i);
            }
        }
        // Reset transparency to 100%
        Graphics2D g2 = (Graphics2D) canvas;
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
        g2.setComposite(ac);
    }

    /**
     * Draw the cloud (player).
     */
    private void drawCloud() {
        // Apply cloud transforms
        if (goingLeft) {
            cloudX = Math.max(0, cloudX - 3 * leftMultiplier);
        }
        if (goingRight) {
            cloudX = Math.min(frameWidth, cloudX + 3 * rightMultiplier);
        }
        if (goingUp) {
            cloudY = Math.max(0, cloudY - 3 * upMultiplier);
        }
        if (goingDown) {
            cloudY = Math.min(frameHeight - 400, cloudY + 3 * downMultiplier);
        }

        Image sprite = resources.cloudSprite[(frame / 20) % 16]; // Animate cloud sprites

        // Actually draw cloud
        canvas.drawImage(
            sprite, // Animated cloud sprite
            (int) (cloudX - cloudSize / 2d), // X
            (int) (cloudY - cloudHeight / 2d), // Y
            (int) cloudSize, // Width
            (int) cloudHeight, // Height
            null
        );
    }

    /**
     * Draw the score (top left).
     */
    private void drawScore() {
        // Draw semi-transparent (70%) rounded rectangles as a background to
        // time left and current score
        Graphics2D g2 = (Graphics2D) canvas;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        canvas.setColor(Color.decode("#333333"));
        canvas.fillRoundRect(10, 10, 160, 45, 16, 16);
        canvas.fillRoundRect(180, 10, 160, 45, 16, 16);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        // Amount of time left
        long timeLeft = levelDuration - (System.currentTimeMillis() - startTime);
        // Amount of time left per minute, seconds and milliseconds
        long minutes = timeLeft / 60000;
        long seconds = (timeLeft % 60000) / 1000;
        long milliseconds = (timeLeft * 10) % 1000 / 10;

        // Draw the time left and collected points
        canvas.setColor(Color.white);
        canvas.setFont(resources.font.deriveFont(20f));
        canvas.drawString(String.valueOf((int) points), 20, 45);
        canvas.drawString(String.format("%02d:%02d.%02d", minutes, seconds, milliseconds), 190, 45);

        // Draw the labels (points and time)
        canvas.setFont(resources.font.deriveFont(12f));
        canvas.drawString(Language.getString("points"), 20, 25);
        canvas.drawString(Language.getString("time"), 190, 25);
    }

    /**
     * Draw active (de)buffs effects.
     */
    private void drawBuffs() {
        // Get 2D graphics to manipulate transparency
        Graphics2D g2 = (Graphics2D) canvas;

        // How many effects are already displayed on screen
        int position = 0;

        // Iterate through all effects, but draw only active (started)
        for (Effect effect : effects) {
            if (effect.start != 0) {
                // Draw background of effect
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                canvas.setColor(Color.decode("#333333"));
                canvas.fillRoundRect(10, 60 + 50 * position, 280, 45, 8, 8);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

                // Draw title, description and time left of effect
                canvas.setColor(Color.decode(effect.positive ? "#00FF00" : "#FF0000"));
                canvas.setFont(resources.font.deriveFont(14f));
                String[] texts = effect.type.getDescription(effect.positive).split("\n");
                canvas.drawString(texts[0], 15, 60 + 50 * position + 15);
                canvas.setColor(Color.decode("#FFFFFF"));
                canvas.setFont(resources.font.deriveFont(10f));
                canvas.drawString(texts[1], 15, 60 + 50 * position + 27);
                canvas.setColor(Color.decode("#CCCCCC"));

                // Amount of duration left
                long timeLeft = effect.duration - (System.currentTimeMillis() - effect.start);
                // Amount of duration left per minute, seconds and milliseconds
                long minutes = timeLeft / 60000;
                long seconds = (timeLeft % 60000) / 1000;
                long milliseconds = (timeLeft * 10) % 1000;
                String timeString = String.format("%02d:%02d.%02d", minutes, seconds, milliseconds);

                // Draw how much duration this effect has left
                canvas.drawString(timeString, 15, 60 + 50 * position + 40);

                position++;
            }
        }
    }
}
