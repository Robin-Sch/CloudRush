package objects;

import components.GameEngine;

/**
 * Effect object.
 */
public class Effect {
    public EffectType type; // Which effect it is
    public double x; // X coordinate
    public double y; // Y coordinate
    public int state; // Animation state
    public long duration; // Duration of the effect
    public long start = 0; // When the effect started
    public boolean positive; // Whether the effect is good or bad for the player

    public Effect(EffectType type) {
        this.type = type;
    }

    /**
     * Apply the effect to the game.
     * @param context Game context
     */
    public void affect(GameEngine context) {
        switch (type) {
            case CLOUD_SIZE -> {
                context.cloudSize *= positive ? 1.5 : 0.5;
                context.cloudHeight = context.cloudSize * (420d / 650d);
            }
            case RAIN_INTENSITY -> context.rainIntensity /= positive ? 3 : 0.5;
            case MOVEMENT_SPEED -> {
                context.upMultiplier *= positive ? 2 : 0.5;
                context.downMultiplier *= positive ? 2 : 0.5;
                context.leftMultiplier *= positive ? 2 : 0.5;
                context.rightMultiplier *= positive ? 2 : 0.5;
            }
            case WIND_STRENGTH -> context.wind *= positive ? 0.5 : 2;
            case RAIN_QUALITY -> context.rainQuality = positive ? 1 : -1;
            case FLOWERS_COUNT -> context.flowerGenerationFrequency /= (positive ? 2 : 0.5);
            case PEOPLE_COUNT -> context.peopleGenerationFrequency *= (positive ? 0.5 : 2);
            case LANTERNS_COUNT -> context.lanternGenerationFrequency /= (positive ? 2 : 0.5);
            case POSITIVE_POINTS -> context.positivePointsMultiplier = positive ? 2 : 0.5;
            case NEGATIVE_POINTS -> context.negativePointsMultiplier = positive ? 0.5 : 2;
            case LEVEL_SPEED -> context.levelSpeed *= positive ? 0.5 : 2;
            case FLOWER_SIZE -> {
                context.flowerWidth *= (positive ? 2 : 0.5);
                context.flowerHeight = context.flowerWidth * (1417d / 2700d);
            }
            case PEOPLE_SPEED -> context.peopleSpeedMultiplier *= positive ? 0.5 : 2;
            case EFFECTS_COUNT -> context.effectsGenerationFrequency *= (positive ? 2 : 0.5);
            default -> System.out.println("Invalid effect affectp!");
        }
    }

    /**
     * Remove the change the effect made.
     * @param context Game context
     */
    public void undo(GameEngine context) {
        switch (type) {
            case CLOUD_SIZE -> {
                context.cloudSize /= positive ? 1.5 : 0.5;
                context.cloudHeight = context.cloudSize * (420d / 650d);
            }
            case RAIN_INTENSITY -> context.rainIntensity *= positive ? 3 : 0.5;
            case MOVEMENT_SPEED -> {
                context.upMultiplier /= positive ? 2 : 0.5;
                context.downMultiplier /= positive ? 2 : 0.5;
                context.leftMultiplier /= positive ? 2 : 0.5;
                context.rightMultiplier /= positive ? 2 : 0.5;
            }
            case WIND_STRENGTH -> context.wind /= positive ? 0.5 : 2;
            case RAIN_QUALITY -> context.rainQuality = 0;
            case FLOWERS_COUNT -> context.flowerGenerationFrequency *= (positive ? 2 : 0.5);
            case PEOPLE_COUNT -> context.peopleGenerationFrequency /= (positive ? 0.5 : 2);
            case LANTERNS_COUNT -> context.lanternGenerationFrequency *= (positive ? 2 : 0.5);
            case POSITIVE_POINTS -> context.positivePointsMultiplier = 1;
            case NEGATIVE_POINTS -> context.negativePointsMultiplier = 1;
            case LEVEL_SPEED -> context.levelSpeed /= positive ? 0.5 : 2;
            case FLOWER_SIZE -> {
                context.flowerWidth /= (positive ? 2 : 0.5);
                context.flowerHeight = context.flowerWidth * (1417d / 2700d);
            }
            case PEOPLE_SPEED -> context.peopleSpeedMultiplier /= positive ? 0.5 : 2;
            case EFFECTS_COUNT -> context.effectsGenerationFrequency /= (positive ? 2 : 0.5);
            default -> System.out.println("Invalid effect undo!");
        }
    }
}
