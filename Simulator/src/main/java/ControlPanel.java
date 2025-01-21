import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ControlPanel extends JPanel {

    private Simulator simulator;

    public JScrollPane scrollPane;
    private JPanel startStopPanel, spriteBatchPanel, frustumCullingPanel, antiAliasPanel, outputPanel;

    public JComboBox simulationSelectionBox;
    private JButton startButton, stopButton;
    private JButton spriteBatchToggle;
    private JButton frustumCullingToggle;
    private JButton antiAliasToggle;
    public JLabel fpsLabel, upsLabel, avgTimeLabel, memoryUsageLabel, cpuLoadLabel;


    private Dimension buttonDimension = new Dimension(100, 40);
    private Dimension panelDimension = new Dimension(300, 100);

    public ControlPanel(Simulator simulator) {
        this.simulator = simulator;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(simulator.getBackground());
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        this.setAlignmentX(Component.CENTER_ALIGNMENT);

        scrollPane = new JScrollPane(this);
    }

    public void init() {
        startStopPanel = getPanel("Control");
        JPanel container = new JPanel(new FlowLayout());
        container.setBackground(simulator.getBackground().darker());
        startStopPanel.setPreferredSize(new Dimension(300, 130));
        startStopPanel.setMaximumSize(new Dimension(300, 130));

        simulationSelectionBox = new JComboBox();
        simulationSelectionBox.setFont(new Font(simulationSelectionBox.getFont().getName(), Font.PLAIN, 18));
        simulationSelectionBox.addItem("Frustum Culling");
        simulationSelectionBox.addItem("Anti Aliasing");
        simulationSelectionBox.addItem("Sprite Batching");
        simulationSelectionBox.setFocusable(false);

        startButton = getButton("Start");
        startButton.setBackground(new Color(9, 195, 74));
        startButton.setPreferredSize(new Dimension(80, 30));

        stopButton = getButton("Stop");
        stopButton.setBackground(new Color(218, 24, 24));
        stopButton.setPreferredSize(new Dimension(80, 30));

        spriteBatchPanel = getPanel("Sprite Batch");
        frustumCullingPanel = getPanel("Frustum Culling");
        antiAliasPanel = getPanel("Anti-Alias");
        antiAliasPanel.setPreferredSize(new Dimension(300, 130));
        antiAliasPanel.setMaximumSize(new Dimension(300, 130));

        outputPanel = getPanel("Output");
        outputPanel.setPreferredSize(new Dimension(300, 300));
        outputPanel.setMaximumSize(new Dimension(300, 200));

        JPanel spriteContainer = new JPanel(new FlowLayout());
        spriteContainer.setBackground(simulator.getBackground().darker());
        spriteBatchToggle = getButton("Turn on");

        JPanel frustumContainer = new JPanel(new FlowLayout());
        frustumContainer.setBackground(simulator.getBackground().darker());
        frustumCullingToggle = getButton("Turn on");

        JPanel aliasContainer = new JPanel(new FlowLayout());
        aliasContainer.setBackground(simulator.getBackground().darker());
        antiAliasToggle = getButton("Turn on");

        fpsLabel = new JLabel();
        upsLabel = new JLabel();
        avgTimeLabel = new JLabel();
        memoryUsageLabel = new JLabel();
        cpuLoadLabel = new JLabel();

        int fontSize = 22;

        fpsLabel.setFont(new Font(fpsLabel.getFont().getName(), Font.PLAIN, fontSize));
        fpsLabel.setAlignmentX(CENTER_ALIGNMENT);
        upsLabel.setFont(new Font(upsLabel.getFont().getName(), Font.PLAIN, fontSize));
        upsLabel.setAlignmentX(CENTER_ALIGNMENT);
        avgTimeLabel.setFont(new Font(avgTimeLabel.getFont().getName(), Font.PLAIN, fontSize));
        avgTimeLabel.setAlignmentX(CENTER_ALIGNMENT);
        memoryUsageLabel.setFont(new Font(memoryUsageLabel.getFont().getName(), Font.PLAIN, fontSize));
        memoryUsageLabel.setAlignmentX(CENTER_ALIGNMENT);
        cpuLoadLabel.setFont(new Font(cpuLoadLabel.getFont().getName(), Font.PLAIN, fontSize));
        cpuLoadLabel.setAlignmentX(CENTER_ALIGNMENT);


        container.add(startButton);
        container.add(stopButton);
        startStopPanel.add(simulationSelectionBox);
        startStopPanel.add(container);

        frustumContainer.add(frustumCullingToggle);
        frustumCullingPanel.add(frustumContainer);

        spriteContainer.add(spriteBatchToggle);
        spriteBatchPanel.add(spriteContainer);

        aliasContainer.add(antiAliasToggle);
        antiAliasPanel.add(aliasContainer);

        outputPanel.add(fpsLabel);
        outputPanel.add(upsLabel);
        outputPanel.add(avgTimeLabel);
        outputPanel.add(memoryUsageLabel);
        outputPanel.add(cpuLoadLabel);

        outputPanel.setPreferredSize(new Dimension(300, 250));
        outputPanel.setMaximumSize(new Dimension(300, 250));
    }

    public void initActionListener() {
        startButton.addActionListener(e -> {
            simulator.renderPanel.isRunning = true;
        });

        stopButton.addActionListener(e -> {
            simulator.renderPanel.isRunning = false;

        });

        antiAliasToggle.addActionListener(e -> {
            if (simulator.renderPanel.antiAliasFlag) {
                simulator.renderPanel.antiAliasFlag = false;
                antiAliasToggle.setBackground(simulator.theme);
                antiAliasToggle.setText("Turn on");
            } else {
                simulator.renderPanel.antiAliasFlag = true;
                antiAliasToggle.setBackground(simulator.redTheme);
                antiAliasToggle.setText("Turn off");
            }
        });

        frustumCullingToggle.addActionListener(e -> {
            if (simulator.renderPanel.frustumCullFlag) {
                simulator.renderPanel.frustumCullFlag = false; // Corrected flag
                frustumCullingToggle.setBackground(simulator.theme);
                frustumCullingToggle.setText("Turn on"); // Adjusted text to match logic
            } else {
                simulator.renderPanel.frustumCullFlag = true; // Corrected flag
                frustumCullingToggle.setBackground(simulator.redTheme);
                frustumCullingToggle.setText("Turn off"); // Adjusted text to match logic
            }
        });

        spriteBatchToggle.addActionListener(e -> {
            if (simulator.renderPanel.spriteBatchFlag) { // Corrected flag
                simulator.renderPanel.spriteBatchFlag = false;
                spriteBatchToggle.setBackground(simulator.theme);
                spriteBatchToggle.setText("Turn on"); // Adjusted text to match logic
            } else {
                simulator.renderPanel.spriteBatchFlag = true; // Corrected flag
                spriteBatchToggle.setBackground(simulator.redTheme);
                spriteBatchToggle.setText("Turn off"); // Adjusted text to match logic
            }
        });



    }

    public JPanel getPanel(String title) {
        JLabel titleLabel = new JLabel(title);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setHorizontalTextPosition(SwingConstants.CENTER);

        titleLabel.setFont(new Font(FlatJetBrainsMonoFont.FAMILY, Font.BOLD, 22));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(panelDimension);
        panel.setMaximumSize(panelDimension);
        panel.putClientProperty(FlatClientProperties.STYLE, "arc : 20");
        panel.setBackground(this.getBackground().darker());
        panel.setBorder(new EmptyBorder(10, 20, 20, 20));

        panel.add(titlePanel);

        return panel;
    }

    public JButton getButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(buttonDimension);
        button.setFont(new Font(button.getFont().getFontName(), Font.PLAIN, 18));
        button.setBackground(simulator.theme);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusable(false);

        return button;
    }

    public void addComponent() {
        this.add(startStopPanel);
        this.add(Box.createVerticalStrut(10));
        this.add(frustumCullingPanel);
        this.add(Box.createVerticalStrut(10));
        this.add(antiAliasPanel);
        this.add(Box.createVerticalStrut(10));
        this.add(spriteBatchPanel);
        this.add(Box.createVerticalStrut(10));
        this.add(outputPanel);

        this.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
}
