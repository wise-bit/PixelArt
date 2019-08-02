import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Pixel extends JLabel {

    private Color color = Color.WHITE;
    private Random rand = new Random();


    public Pixel() {
        this.setBackground(color);
        // this.setBackground(getRandomColor());
        setOpaque(true);
    }

    public Color getRandomColor() {
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        return new Color(r, g, b);
    }

    @Override
    public String toString() {
        return this.getBackground().toString();
    }
}
