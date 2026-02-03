package simon;
import java.awt.Color;

public enum Colour {
    GREEN(Color.GREEN.darker(), Color.GREEN),
    RED(Color.RED.darker(), Color.RED),
    YELLOW(Color.YELLOW.darker(), Color.YELLOW),
    BLUE(Color.BLUE.darker(), Color.BLUE);

    public final Color dim;
    public final Color bright;

    Colour(Color dim, Color bright) {
        this.dim = dim;
        this.bright = bright;
    }
}