package gui;

import creational.singleton.AppSetting;
import creational.singleton.logger.Logger;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Interactive GUI demonstration of the Singleton Pattern
 * Shows both AppSetting configuration and Logger system examples
 */
public class SingletonPatternGUI extends JPanel {

    // AppSetting demo components
    private JTextField dbUrlField;
    private JTextField apiKeyField;
    private JLabel instanceHashLabel1;
    private JLabel instanceHashLabel2;
    private JLabel identityCheckLabel;

    // Logger demo components
    private JTextField logMessageField;
    private JComboBox<String> logLevelCombo;
    private JTextArea logOutputArea;
    private JLabel loggerInstanceLabel1;
    private JLabel loggerInstanceLabel2;
    private JLabel loggerIdentityLabel;

    // Pattern explanation components
    private JTextArea explanationArea;

    public SingletonPatternGUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        initializeComponents();
        createLayout();
        setupEventHandlers();
        updateDisplays();
    }

    private void initializeComponents() {
        // AppSetting components
        dbUrlField = new JTextField("jdbc:mysql://localhost:3306/mydb", 25);
        apiKeyField = new JTextField("wxxzzxzd", 20);
        instanceHashLabel1 = new JLabel("Not created yet");
        instanceHashLabel2 = new JLabel("Not created yet");
        identityCheckLabel = new JLabel("Not compared yet");

        // Logger components
        logMessageField = new JTextField(30);
        logLevelCombo = new JComboBox<>(new String[]{"INFO", "WARN", "ERROR"});
        logOutputArea = new JTextArea(8, 40);
        logOutputArea.setEditable(false);
        logOutputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        logOutputArea.setBackground(new Color(33, 37, 41));
        logOutputArea.setForeground(Color.WHITE);
        loggerInstanceLabel1 = new JLabel("Not created yet");
        loggerInstanceLabel2 = new JLabel("Not created yet");
        loggerIdentityLabel = new JLabel("Not compared yet");

        // Explanation area
        explanationArea = new JTextArea(6, 50);
        explanationArea.setEditable(false);
        explanationArea.setWrapStyleWord(true);
        explanationArea.setLineWrap(true);
        explanationArea.setBackground(new Color(255, 248, 220));
        explanationArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        explanationArea.setText(
            "🎯 SINGLETON PATTERN DEMONSTRATION\n\n" +
            "The Singleton pattern ensures that a class has only one instance and provides global access to it. " +
            "This demo shows two real-world examples:\n\n" +
            "1. AppSetting: Configuration management with shared state\n" +
            "2. Logger: Centralized logging system with thread safety\n\n" +
            "Notice how multiple getInstance() calls return the same object instance (same hash code)."
        );
    }

    private void createLayout() {
        // Main container with tabs
        JTabbedPane tabbedPane = new JTabbedPane();

        // AppSetting demo tab
        JPanel appSettingPanel = createAppSettingPanel();
        tabbedPane.addTab("🔧 Configuration Singleton", appSettingPanel);

        // Logger demo tab
        JPanel loggerPanel = createLoggerPanel();
        tabbedPane.addTab("📝 Logger Singleton", loggerPanel);

        // Pattern explanation tab
        JPanel explanationPanel = createExplanationPanel();
        tabbedPane.addTab("📚 Pattern Explanation", explanationPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createAppSettingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("🔧 Application Configuration Singleton", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Main content
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(248, 249, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Configuration section
        JPanel configPanel = new JPanel(new GridBagLayout());
        configPanel.setBorder(new TitledBorder("Configuration Settings"));
        configPanel.setBackground(Color.WHITE);
        GridBagConstraints configGbc = new GridBagConstraints();
        configGbc.insets = new Insets(5, 5, 5, 5);

        configGbc.gridx = 0; configGbc.gridy = 0;
        configPanel.add(new JLabel("Database URL:"), configGbc);
        configGbc.gridx = 1;
        configPanel.add(dbUrlField, configGbc);

        configGbc.gridx = 0; configGbc.gridy = 1;
        configPanel.add(new JLabel("API Key:"), configGbc);
        configGbc.gridx = 1;
        configPanel.add(apiKeyField, configGbc);

        configGbc.gridx = 0; configGbc.gridy = 2; configGbc.gridwidth = 2;
        JButton updateConfigButton = new JButton("Update Configuration");
        updateConfigButton.setBackground(new Color(13, 110, 253));
        updateConfigButton.setForeground(Color.WHITE);
        updateConfigButton.addActionListener(e -> updateAppSetting());
        configPanel.add(updateConfigButton, configGbc);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(configPanel, gbc);

        // Instance verification section
        JPanel instancePanel = new JPanel(new GridBagLayout());
        instancePanel.setBorder(new TitledBorder("Singleton Instance Verification"));
        instancePanel.setBackground(Color.WHITE);
        GridBagConstraints instGbc = new GridBagConstraints();
        instGbc.insets = new Insets(5, 5, 5, 5);
        instGbc.anchor = GridBagConstraints.WEST;

        instGbc.gridx = 0; instGbc.gridy = 0;
        instancePanel.add(new JLabel("Instance 1 Hash:"), instGbc);
        instGbc.gridx = 1;
        instancePanel.add(instanceHashLabel1, instGbc);

        instGbc.gridx = 0; instGbc.gridy = 1;
        instancePanel.add(new JLabel("Instance 2 Hash:"), instGbc);
        instGbc.gridx = 1;
        instancePanel.add(instanceHashLabel2, instGbc);

        instGbc.gridx = 0; instGbc.gridy = 2;
        instancePanel.add(new JLabel("Same Instance?"), instGbc);
        instGbc.gridx = 1;
        instancePanel.add(identityCheckLabel, instGbc);

        instGbc.gridx = 0; instGbc.gridy = 3; instGbc.gridwidth = 2;
        JButton verifyButton = new JButton("Verify Singleton Behavior");
        verifyButton.setBackground(new Color(25, 135, 84));
        verifyButton.setForeground(Color.WHITE);
        verifyButton.addActionListener(e -> verifyAppSettingSingleton());
        instancePanel.add(verifyButton, instGbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        contentPanel.add(instancePanel, gbc);

        panel.add(contentPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createLoggerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("📝 Thread-Safe Logger Singleton", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Main content
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(248, 249, 250));

        // Logging controls
        JPanel controlsPanel = new JPanel(new GridBagLayout());
        controlsPanel.setBorder(new TitledBorder("Logging Controls"));
        controlsPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        controlsPanel.add(new JLabel("Log Level:"), gbc);
        gbc.gridx = 1;
        controlsPanel.add(logLevelCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        controlsPanel.add(new JLabel("Message:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        controlsPanel.add(logMessageField, gbc);

        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JButton logButton = new JButton("Log Message");
        logButton.setBackground(new Color(220, 53, 69));
        logButton.setForeground(Color.WHITE);
        logButton.addActionListener(e -> logMessage());
        controlsPanel.add(logButton, gbc);

        contentPanel.add(controlsPanel, BorderLayout.NORTH);

        // Log output
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBorder(new TitledBorder("Log Output"));
        outputPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(logOutputArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        outputPanel.add(scrollPane, BorderLayout.CENTER);

        JButton clearButton = new JButton("Clear Log");
        clearButton.addActionListener(e -> logOutputArea.setText(""));
        outputPanel.add(clearButton, BorderLayout.SOUTH);

        contentPanel.add(outputPanel, BorderLayout.CENTER);

        // Instance verification
        JPanel instancePanel = new JPanel(new GridBagLayout());
        instancePanel.setBorder(new TitledBorder("Logger Instance Verification"));
        instancePanel.setBackground(Color.WHITE);
        GridBagConstraints instGbc = new GridBagConstraints();
        instGbc.insets = new Insets(5, 5, 5, 5);
        instGbc.anchor = GridBagConstraints.WEST;

        instGbc.gridx = 0; instGbc.gridy = 0;
        instancePanel.add(new JLabel("Logger 1 Hash:"), instGbc);
        instGbc.gridx = 1;
        instancePanel.add(loggerInstanceLabel1, instGbc);

        instGbc.gridx = 0; instGbc.gridy = 1;
        instancePanel.add(new JLabel("Logger 2 Hash:"), instGbc);
        instGbc.gridx = 1;
        instancePanel.add(loggerInstanceLabel2, instGbc);

        instGbc.gridx = 0; instGbc.gridy = 2;
        instancePanel.add(new JLabel("Same Instance?"), instGbc);
        instGbc.gridx = 1;
        instancePanel.add(loggerIdentityLabel, instGbc);

        instGbc.gridx = 0; instGbc.gridy = 3; instGbc.gridwidth = 2;
        JButton verifyLoggerButton = new JButton("Verify Logger Singleton");
        verifyLoggerButton.setBackground(new Color(25, 135, 84));
        verifyLoggerButton.setForeground(Color.WHITE);
        verifyLoggerButton.addActionListener(e -> verifyLoggerSingleton());
        instancePanel.add(verifyLoggerButton, instGbc);

        contentPanel.add(instancePanel, BorderLayout.SOUTH);

        panel.add(contentPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createExplanationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("📚 Singleton Pattern Explanation", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(explanationArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void setupEventHandlers() {
        // Enter key support for log message field
        logMessageField.addActionListener(e -> logMessage());
    }

    private void updateAppSetting() {
        try {
            AppSetting config = AppSetting.getInstance();
            config.setDatabaseUrl(dbUrlField.getText());
            config.setApiKey(apiKeyField.getText());

            JOptionPane.showMessageDialog(this,
                "Configuration updated successfully!\n" +
                "Database URL: " + config.getDatabaseUrl() + "\n" +
                "API Key: " + config.getApiKey(),
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error updating configuration: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verifyAppSettingSingleton() {
        AppSetting instance1 = AppSetting.getInstance();
        AppSetting instance2 = AppSetting.getInstance();

        instanceHashLabel1.setText(String.valueOf(instance1.hashCode()));
        instanceHashLabel2.setText(String.valueOf(instance2.hashCode()));

        boolean sameInstance = instance1 == instance2;
        identityCheckLabel.setText(sameInstance ? "✅ YES - Same Instance!" : "❌ NO - Different Instances");
        identityCheckLabel.setForeground(sameInstance ? new Color(25, 135, 84) : new Color(220, 53, 69));
    }

    private void logMessage() {
        try {
            String message = logMessageField.getText().trim();
            if (message.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a message to log.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Logger logger = Logger.getInstance();
            String level = (String) logLevelCombo.getSelectedItem();

            // Capture the log output
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(baos));

            // Log the message
            switch (level) {
                case "INFO":
                    logger.info(message);
                    break;
                case "WARN":
                    logger.warn(message);
                    break;
                case "ERROR":
                    logger.error(message);
                    break;
            }

            // Restore original output and display in GUI
            System.setOut(originalOut);
            String logOutput = baos.toString();
            logOutputArea.append(logOutput + "\n");
            logOutputArea.setCaretPosition(logOutputArea.getDocument().getLength());

            // Clear the input field
            logMessageField.setText("");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error logging message: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verifyLoggerSingleton() {
        Logger logger1 = Logger.getInstance();
        Logger logger2 = Logger.getInstance();

        loggerInstanceLabel1.setText(String.valueOf(logger1.hashCode()));
        loggerInstanceLabel2.setText(String.valueOf(logger2.hashCode()));

        boolean sameInstance = logger1 == logger2;
        loggerIdentityLabel.setText(sameInstance ? "✅ YES - Same Instance!" : "❌ NO - Different Instances");
        loggerIdentityLabel.setForeground(sameInstance ? new Color(25, 135, 84) : new Color(220, 53, 69));
    }

    private void updateDisplays() {
        // Initialize with current singleton states
        verifyAppSettingSingleton();
        verifyLoggerSingleton();
    }
}
