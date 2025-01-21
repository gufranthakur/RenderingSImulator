import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SpriteSheetSplitter {
    private BufferedImage[] sprites;
    private static final int ROWS = 1;
    private static final int COLS = 9;
    private static final int TOTAL_SPRITES = 9;

    public SpriteSheetSplitter(String spritesheetPath) throws IOException {
        sprites = new BufferedImage[TOTAL_SPRITES];
        loadSprites(spritesheetPath);
    }

    private void loadSprites(String spritesheetPath) throws IOException {
        // Load the spritesheet
        BufferedImage spritesheet = ImageIO.read(new File(spritesheetPath));

        // Calculate the width of each sprite (height is full image height)
        int spriteWidth = spritesheet.getWidth() / COLS;
        int spriteHeight = spritesheet.getHeight();

        // Split the spritesheet horizontally
        for (int col = 0; col < COLS; col++) {
            sprites[col] = spritesheet.getSubimage(
                    col * spriteWidth,  // x position
                    0,                  // y position (always 0 since single row)
                    spriteWidth,        // width of individual sprite
                    spriteHeight       // full height of image
            );
        }
    }

    // Get a specific sprite by index (0-8)
    public BufferedImage getSprite(int index) {
        if (index < 0 || index >= TOTAL_SPRITES) {
            throw new IllegalArgumentException("Sprite index out of bounds");
        }
        return sprites[index];
    }

    // Get all sprites
    public BufferedImage[] getAllSprites() {
        return sprites;
    }
}