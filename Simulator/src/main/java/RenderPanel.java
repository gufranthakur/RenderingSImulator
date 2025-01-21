import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont;
import com.sun.management.OperatingSystemMXBean;
import objects.Circle;
import objects.Quad;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

import java.util.Objects;
import java.util.Random;

public class RenderPanel extends JPanel implements Runnable{

    private Simulator simulator;

    public boolean frustumCullFlag, antiAliasFlag, spriteBatchFlag;

    public Random random = new Random();

    public boolean isRunning = true;

    public boolean moveUp, moveDown, moveLeft, moveRight;
    public int cameraX, cameraY = 0;
    public Rectangle viewport;

    private long frameTimeSum = 0;
    private int frameCount = 0;
    private Runtime runtime = Runtime.getRuntime();
    private MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
    private OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    Circle[] circles = new Circle[300];
    Quad[] quads = new Quad[99999];
    String[] spritePaths = {"sprite1.png", "sprite2.png", "sprite3.png", "sprite4.png",
            "sprite5.png", "sprite6.png", "sprite7.png", "sprite8.png", "sprite9.png", };

    SpriteSheetSplitter spriteSheetSplitter;

    public RenderPanel(Simulator simulator) {
        this.simulator = simulator;
        this.setBackground(Color.BLACK);
        this.setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    public void init() {
        try {
            spriteSheetSplitter = new SpriteSheetSplitter("spritesheet.png");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        viewport = new Rectangle(cameraX + 50,
                cameraY + 50,
                getWidth(),
                getHeight());

        for (int i = 0; i < circles.length; i++) {
            int randomX = random.nextInt(800);
            int randomY = random.nextInt(600);
            int randomWidth = random.nextInt(99, 100);
            int randomSpeed = random.nextInt(5, 8);

            int randomR = random.nextInt(0, 30); // Inclusive range
            int randomB = random.nextInt(120, 250);


            circles[i] = new Circle(randomX, randomY, randomWidth,
                    new Color(randomR, randomR, randomB), randomSpeed);
        }


        int quadX = 0;
        int quadY = 0;
        for (int i = 0; i < quads.length; i++) {
            int randomWidth = random.nextInt(100, 200);
            int randomHeight = random.nextInt(100, 200);

            int randomR = random.nextInt(3, 57); // Inclusive range
            int randomG = random.nextInt(3, 143);
            int randomB = random.nextInt(242, 252);

            if (quadX > 10000) { // Reset quadX and move to the next row
                quadX = 0;
                quadY += 205; // Increase by the height of a quad
            }

            quads[i] = new Quad(quadX, quadY, randomWidth, randomHeight, new Color(randomR, randomG, randomB));

            quadX += randomWidth + 5; // Move to the next position in the row
        }

    }
    int x = 50; int y = 200;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;

        g2D.setTransform(new AffineTransform());


        if (simulator.controlPanel.simulationSelectionBox.getSelectedItem().equals("Frustum Culling")) {
            g2D.translate(-cameraX, -cameraY);
             viewport.setBounds(
                    cameraX + 50,                // viewport x in world coordinates
                    cameraY + 50,                // viewport y in world coordinates
                    getWidth(),             // viewport width
                    getHeight()            // viewport height
            );

            if (frustumCullFlag) {
                // Only render visible quads
                for (Quad quad : quads) {
                    if (quad.isVisible(viewport)) {
                        quad.render(g2D);
                    }
                }
            } else {
                // Render all quads
                for (Quad quad : quads) {
                    quad.render(g2D);
                }
            }
        }

        else if (simulator.controlPanel.simulationSelectionBox.getSelectedItem().equals("Anti Aliasing")) {

            if (antiAliasFlag) {
                g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                for (Circle circle : circles) circle.render(g2D);
                g2D.setFont(new Font(simulator.getFont().getName(), Font.PLAIN, 100));

                g2D.setColor(Color.WHITE);
                g2D.drawString("Anti Aliasing ON", 350, 80);
            } else {
                g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
                for (Circle circle : circles) circle.render(g2D);
                g2D.setFont(new Font(simulator.getFont().getName(), Font.PLAIN, 100));

                g2D.setColor(Color.WHITE);
                g2D.drawString("Anti Aliasing OFF", 350, 80);

            }
        }

        else if (simulator.controlPanel.simulationSelectionBox.getSelectedItem().equals("Sprite Batching")) {
            g2D.translate(150, 70);
            if (!spriteBatchFlag) {

                try {
                    // Keep drawing until we cover more than screen height
                    for (int currentY = 0; currentY < getHeight(); currentY += 32) {
                        // Keep drawing until we cover more than screen width
                        for (int currentX = 0; currentX < getWidth(); currentX += 32) {
                            int randomIndex = random.nextInt(0, 9);
                            g2D.drawImage(ImageIO.read(new File(spritePaths[randomIndex])), currentX, currentY, null);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                for (int currentY = 0; currentY < getHeight(); currentY += 32) {
                    // Keep drawing until we cover more than screen width
                    for (int currentX = 0; currentX < getWidth(); currentX += 32) {
                        int randomIndex = random.nextInt(0, 9);
                        g2D.drawImage(spriteSheetSplitter.getSprite(randomIndex), currentX, currentY, null);
                    }
                }
            }
        }
    }


    public void update() {
        if (isRunning) {
            if (Objects.equals(simulator.controlPanel.simulationSelectionBox.getSelectedItem(), "Frustum Culling")) {
                // Update camera position
                if (moveUp) cameraY -= 10;
                if (moveDown) cameraY += 10;
                if (moveLeft) cameraX -= 10;
                if (moveRight) cameraX += 10;

                // Update viewport bounds
                viewport.setBounds(
                        cameraX + 50,
                        cameraY + 50,
                        getWidth(),
                        getHeight()
                );

                // Only update visible quads when frustum culling is enabled
                if (frustumCullFlag) {
                    for (Quad quad : quads) {
                        if (quad.isVisible(viewport)) {
                            quad.update();
                        }
                    }
                } else {
                    // Update all quads when frustum culling is disabled
                    for (Quad quad : quads) {
                        quad.update();
                    }
                }
            } else if (simulator.controlPanel.simulationSelectionBox.getSelectedItem().equals("Anti Aliasing")) {
                for (Circle circle : circles) {
                    circle.update();
                }
            }
        }
    }
    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / 60;
        double timePerUpdate = 1000000000.0 / 60;

        long previousTime = System.nanoTime();
        long lastMemoryUsage = runtime.totalMemory() - runtime.freeMemory();

        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while (true) {
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }

            if (deltaF >= 1) {
                long frameStartTime = System.nanoTime();

                repaint();

                long frameEndTime = System.nanoTime();
                frameTimeSum += (frameEndTime - frameStartTime);
                frameCount++;

                frames++;
                deltaF--;
            }

            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();

                // Calculate average frame time
                double avgFrameTime = frameTimeSum / (double) frameCount;

                // Calculate memory delta
                long currentMemoryUsage = runtime.totalMemory() - runtime.freeMemory();
                long memoryDelta = currentMemoryUsage - lastMemoryUsage;
                double memoryDeltaMB = memoryDelta / (1024.0 * 1024.0);

                // CPU usage
                double cpuLoad = osBean.getProcessCpuLoad() * 100;

                simulator.controlPanel.fpsLabel.setText("FPS : " + frames);
                simulator.controlPanel.upsLabel.setText("UPS : " + updates);
                simulator.controlPanel.avgTimeLabel.setText("Render time : " + String.format("%.2f", avgFrameTime / 1_000_000.0) + "ms");
                simulator.controlPanel.memoryUsageLabel.setText("Memory: " + String.format("%.2f", memoryDeltaMB) + " MB/s");
                simulator.controlPanel.cpuLoadLabel.setText("CPU Load: " + String.format("%.2f", cpuLoad) + "%");

                // Reset counters
                frames = 0;
                updates = 0;
                frameTimeSum = 0;
                frameCount = 0;
                lastMemoryUsage = currentMemoryUsage;
            }
        }
    }
}
