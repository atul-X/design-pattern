package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main GUI Application for Design Pattern Showcase
 * Provides an interactive visual demonstration of implemented design patterns
 */
public class DesignPatternShowcase extends JFrame {
    private static final String APP_TITLE = "Design Patterns Interactive Showcase";
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 800;

    private JTabbedPane mainTabbedPane;
    private JPanel welcomePanel;

    public DesignPatternShowcase() {
        initializeGUI();
        createWelcomePanel();
        createPatternTabs();
        setupWindowProperties();
    }

    private void initializeGUI() {
        setTitle(APP_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Fall back to default look and feel
        }

        // Create main tabbed pane with modern styling
        mainTabbedPane = new JTabbedPane(JTabbedPane.TOP);
        mainTabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        add(mainTabbedPane, BorderLayout.CENTER);
    }

    private void createWelcomePanel() {
        welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        welcomePanel.setBackground(new Color(248, 249, 250));

        // Title section
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(248, 249, 250));

        JLabel titleLabel = new JLabel("🎯 Design Patterns Interactive Showcase", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(33, 37, 41));
        titlePanel.add(titleLabel, BorderLayout.NORTH);

        JLabel subtitleLabel = new JLabel("Explore and interact with fundamental design patterns", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(108, 117, 125));
        subtitleLabel.setBorder(new EmptyBorder(10, 0, 20, 0));
        titlePanel.add(subtitleLabel, BorderLayout.CENTER);

        welcomePanel.add(titlePanel, BorderLayout.NORTH);

        // Content section
        JPanel contentPanel = new JPanel(new GridLayout(2, 1, 20, 20));
        contentPanel.setBackground(new Color(248, 249, 250));

        // Behavioral Patterns Card
        JPanel behavioralCard = createPatternCategoryCard(
            "🎭 Behavioral Patterns",
            "Communication between objects and assignment of responsibilities",
            new String[]{"Command Pattern", "Observer Pattern", "Strategy Pattern", "Iterator Pattern", "Memento Pattern", "Template Method Pattern"},
            new Color(13, 110, 253)
        );
        contentPanel.add(behavioralCard);

        // Creational Patterns Card
        JPanel creationalCard = createPatternCategoryCard(
            "🏭 Creational Patterns",
            "Object creation mechanisms and instantiation control",
            new String[]{"Singleton Pattern", "Factory Pattern (Planned)", "Builder Pattern (Planned)", "Prototype Pattern (Planned)"},
            new Color(25, 135, 84)
        );
        contentPanel.add(creationalCard);

        welcomePanel.add(contentPanel, BorderLayout.CENTER);

        // Instructions panel
        JPanel instructionsPanel = new JPanel(new BorderLayout());
        instructionsPanel.setBackground(new Color(248, 249, 250));
        instructionsPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JLabel instructionsLabel = new JLabel("<html><div style='text-align: center;'>" +
            "💡 <b>How to use:</b> Click on the tabs above to explore different design patterns.<br>" +
            "Each tab provides interactive demonstrations, real-world examples, and visual explanations." +
            "</div></html>", JLabel.CENTER);
        instructionsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        instructionsLabel.setForeground(new Color(108, 117, 125));
        instructionsPanel.add(instructionsLabel, BorderLayout.CENTER);

        welcomePanel.add(instructionsPanel, BorderLayout.SOUTH);

        mainTabbedPane.addTab("🏠 Welcome", welcomePanel);
    }

    private JPanel createPatternCategoryCard(String title, String description, String[] patterns, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(222, 226, 230), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(accentColor);
        headerPanel.add(titleLabel, BorderLayout.NORTH);

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descLabel.setForeground(new Color(108, 117, 125));
        descLabel.setBorder(new EmptyBorder(5, 0, 15, 0));
        headerPanel.add(descLabel, BorderLayout.CENTER);

        card.add(headerPanel, BorderLayout.NORTH);

        // Patterns list
        JPanel patternsPanel = new JPanel(new GridLayout(0, 2, 10, 5));
        patternsPanel.setBackground(Color.WHITE);

        for (String pattern : patterns) {
            JLabel patternLabel = new JLabel("• " + pattern);
            patternLabel.setFont(new Font("Arial", Font.PLAIN, 13));
            patternLabel.setForeground(new Color(33, 37, 41));
            patternsPanel.add(patternLabel);
        }

        card.add(patternsPanel, BorderLayout.CENTER);

        return card;
    }

    private void createPatternTabs() {
        // Add pattern demonstration tabs
        mainTabbedPane.addTab("👑 Singleton", new SingletonPatternGUI());
        mainTabbedPane.addTab("🎮 Command", new CommandPatternGUI());
        mainTabbedPane.addTab("👁️ Observer", new ObserverPatternGUI());
        mainTabbedPane.addTab("🔍 Iterator", new IteratorPatternGUI());

        // Add placeholder tabs for future patterns
        JPanel comingSoonPanel = createComingSoonPanel("More patterns coming soon!");
        mainTabbedPane.addTab("🎯 Strategy", comingSoonPanel);
        mainTabbedPane.addTab("💾 Memento", comingSoonPanel);
        mainTabbedPane.addTab("📋 Template", comingSoonPanel);
    }

    private JPanel createComingSoonPanel(String message) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 249, 250));

        JLabel label = new JLabel(message, JLabel.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 18));
        label.setForeground(new Color(108, 117, 125));
        panel.add(label, BorderLayout.CENTER);

        return panel;
    }

    private void setupWindowProperties() {
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null); // Center on screen
        setMinimumSize(new Dimension(800, 600));

        // Add application icon (if available)
        try {
            // You can add an icon here if you have one
            // setIconImage(ImageIO.read(new File("icon.png")));
        } catch (Exception e) {
            // No icon available, continue without it
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new DesignPatternShowcase().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                    "Error starting application: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
