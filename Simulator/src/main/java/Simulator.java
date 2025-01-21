import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Simulator extends JFrame implements KeyListener {

    public ControlPanel controlPanel;
    public RenderPanel renderPanel;

    public Thread renderThread;


    public Color theme = new Color(27, 122, 246);
    public Color redTheme = new Color(246, 27, 27);

    public Simulator() {
        this.setSize(1200, 800);
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setTitle("Simulator");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.addKeyListener(this);
        this.setLayout(new BorderLayout());
    }

    public void init() {

        controlPanel = new ControlPanel(this);
        renderPanel = new RenderPanel(this);
        controlPanel.init();
        controlPanel.putClientProperty(FlatClientProperties.STYLE, "arc : 20");
        controlPanel.initActionListener();


        renderPanel.init();

        renderThread = new Thread(renderPanel);
        renderThread.start();
    }

    public void addComponent() {
        controlPanel.addComponent();

        this.add(controlPanel.scrollPane, BorderLayout.WEST);
        this.add(renderPanel, BorderLayout.CENTER);

        this.setVisible(true);
    }

    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new FlatMacDarkLaf());
        FlatJetBrainsMonoFont.install();


        SwingUtilities.invokeLater(() -> {
            Simulator simulator = new Simulator();
            simulator.init();
            simulator.addComponent();
        });
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'w' : renderPanel.moveUp = true;
            break;
            case 's' : renderPanel.moveDown = true;
            break;
            case 'a' : renderPanel.moveLeft = true;
            break;
            case 'd' : renderPanel.moveRight = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'w' : renderPanel.moveUp = false;
                break;
            case 's' : renderPanel.moveDown = false;
                break;
            case 'a' : renderPanel.moveLeft = false;
                break;
            case 'd' : renderPanel.moveRight = false;
        }
    }
}
