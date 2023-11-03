package objects;

/**
 * Raindrop object.
 */
public class Raindrop {
    public double x; // Position X of raindrop
    public double y; // Position Y of raindrop
    public double speed; // Falling speed of raindrop
    public int quality; // -1 -> acid, 0 -> normal, 1 -> bigger water drop
    public int state; // State of sprite animation : -1 -> falling, >=0 - splash animation
}
