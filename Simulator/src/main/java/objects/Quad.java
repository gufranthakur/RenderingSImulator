package objects;

import java.awt.*;

public class Quad extends Rectangle {
    public int x, y, width, height;
    private Color color;
    private static final int MAX_COLOR_VALUE = 255;
    private static final int MIN_BLUE_VALUE = 100;  // Minimum blue to avoid going too dark
    private static final int INCREMENT_AMOUNT = 2;  // Increased for smoother transition

    // Flag to track if color data needs updating
    private boolean needsColorUpdate = false;
    private int blueComponent;
    // Flag to track direction of color change
    private boolean isDarkening = true;

    public Quad(int x, int y, int width, int height, Color color) {
        super(x, y, width, height);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        // Start with bright blue
        this.blueComponent = MAX_COLOR_VALUE;
        this.color = new Color(0, 0, blueComponent);
    }

    public void render(Graphics2D g2d) {
        // Only create new Color object if color has changed
        if (needsColorUpdate) {
            color = new Color(0, 0, blueComponent);
            needsColorUpdate = false;
        }
        g2d.setColor(color);
        g2d.fillRect(x, y, width, height);
    }

    public void update() {
        if (isDarkening) {
            if (blueComponent > MIN_BLUE_VALUE) {
                blueComponent -= INCREMENT_AMOUNT;
            } else {
                isDarkening = false;
                blueComponent += INCREMENT_AMOUNT;
            }
        } else {
            if (blueComponent < MAX_COLOR_VALUE) {
                blueComponent += INCREMENT_AMOUNT;
            } else {
                isDarkening = true;
                blueComponent -= INCREMENT_AMOUNT;
            }
        }

        needsColorUpdate = true;
    }

    // Method to check if quad is visible in viewport
    public boolean isVisible(Rectangle viewport) {
        return viewport.intersects(x, y, width, height);
    }
}