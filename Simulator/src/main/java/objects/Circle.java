package objects;

import java.awt.*;

public class Circle {
    private int x, y, radius, speed;
    private Color color;

    public boolean invertedX, invertedY;

    // Constructor to define position, size, and color of the oval
    public Circle(int x, int y, int radius, Color color, int speed) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;
        this.speed = speed;
    }

    // Method to draw the oval
    public void render(Graphics2D g2d) {
        // Set the color for the oval
        g2d.setColor(color);
        // Draw the oval (ellipse) with the specified coordinates and size
        g2d.fillOval(x, y, radius, radius);
    }

    public void update() {
            if (x >= 1200 - radius) {
                invertedX = true;
            } else if (x < 200) {
                invertedX = false;
            }

            if (y >= 900 - radius) {
                invertedY = true;  // If the Y position is at or beyond the bottom, invert the movement
            } else if (y <= 200) {
                invertedY = false;  // If the Y position is at or beyond the top, revert to normal movement
            }

            if (!invertedX) {
                x += speed;
            } else {
                x -= speed;
            }

            if (!invertedY) {
                y += speed;  // Move downwards
            } else {
                y -= speed;  // Move upwards
            }
    }


}
