package objects;

import components.Language;

/**
 * Enum describing all types of effects.
 */
public enum EffectType {
    CLOUD_SIZE,
    RAIN_INTENSITY,
    MOVEMENT_SPEED,
    WIND_STRENGTH,
    RAIN_QUALITY,
    FLOWERS_COUNT,
    PEOPLE_COUNT,
    LANTERNS_COUNT,
    POSITIVE_POINTS,
    NEGATIVE_POINTS,
    LEVEL_SPEED,
    FLOWER_SIZE,
    PEOPLE_SPEED,
    EFFECTS_COUNT;

    /**
     * Function used to get title and description (separated by \n) of certain effect
     * in its positive and negative state.
     * @param positive Whether the effect is good or bad for the player
     */
    public String getDescription(boolean positive) {
        String result = "";
        if (positive) {
            switch (this) {
                case CLOUD_SIZE -> result = "positiveCloudSize";
                case RAIN_INTENSITY -> result = "positiveRainIntensity";
                case MOVEMENT_SPEED -> result = "positiveMovementSpeed";
                case WIND_STRENGTH -> result = "positiveWindStrength";
                case RAIN_QUALITY -> result = "positiveRainQuality";
                case FLOWERS_COUNT -> result = "positiveFlowersCount";
                case PEOPLE_COUNT -> result = "positivePeopleCount";
                case LANTERNS_COUNT -> result = "positiveLanternsCount";
                case POSITIVE_POINTS -> result = "positivePositivePoints";
                case NEGATIVE_POINTS -> result = "positiveNegativePoints";
                case LEVEL_SPEED -> result = "positiveLevelSpeed";
                case FLOWER_SIZE -> result = "positiveFlowerSize";
                case PEOPLE_SPEED -> result = "positivePeopleSpeed";
                case EFFECTS_COUNT -> result = "positiveEffectsCount";
                default -> result = "";
            }
        } else {
            switch (this) {
                case CLOUD_SIZE -> result = "negativeCloudSize";
                case RAIN_INTENSITY -> result = "negativeRainIntensity";
                case MOVEMENT_SPEED -> result = "negativeMovementSpeed";
                case WIND_STRENGTH -> result = "negativeWindStrength";
                case RAIN_QUALITY -> result = "negativeRainQuality";
                case FLOWERS_COUNT -> result = "negativeFlowersCount";
                case PEOPLE_COUNT -> result = "negativePeopleCount";
                case LANTERNS_COUNT -> result = "negativeLanternsCount";
                case POSITIVE_POINTS -> result = "negativePositivePoints";
                case NEGATIVE_POINTS -> result = "negativeNegativePoints";
                case LEVEL_SPEED -> result = "negativeLevelSpeed";
                case FLOWER_SIZE -> result = "negativeFlowerSize";
                case PEOPLE_SPEED -> result = "negativePeopleSpeed";
                case EFFECTS_COUNT -> result = "negativeEffectsCount";
                default -> result = "";
            }
        }
        return Language.getString(result);
    }
}
