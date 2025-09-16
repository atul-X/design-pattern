package gui;

import behavioural.command.smarthome.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Interactive GUI demonstration of the Command Pattern
 * Shows smart home automation system with visual device controls
 */
public class CommandPatternGUI extends JPanel {

    // Smart home devices
    private SmartLight livingRoomLight;
    private SmartLight bedroomLight;
    private SmartThermostat thermostat;
    private SecuritySystem securitySystem;
    private SmartHomeController controller;

    // GUI Components
    private JPanel devicesPanel;
    private JTextArea commandHistoryArea;
    private JList<String> commandHistoryList;
    private DefaultListModel<String> historyModel;

    // Device status labels
    private JLabel livingRoomLightStatus;
    private JLabel bedroomLightStatus;
    private JLabel thermostatStatus;
    private JLabel securityStatus;

    // Control components
    private JSlider thermostatSlider;
    private JTextField customTempField;

    public CommandPatternGUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        initializeSmartHome();
        initializeComponents();
        createLayout();
        setupEventHandlers();
        updateDeviceStatuses();
    }

    private void initializeSmartHome() {
        // Create smart home devices
        livingRoomLight = new SmartLight("Living Room");
        bedroomLight = new SmartLight("Bedroom");
        thermostat = new SmartThermostat();
        securitySystem = new SecuritySystem();
        controller = new SmartHomeController();
    }

    private void initializeComponents() {
        // Device status labels
        livingRoomLightStatus = new JLabel("OFF");
        bedroomLightStatus = new JLabel("OFF");
        thermostatStatus = new JLabel("22°C (AUTO)");
        securityStatus = new JLabel("DISARMED");

        // Control components
        thermostatSlider = new JSlider(16, 30, 22);
        thermostatSlider.setMajorTickSpacing(2);
        thermostatSlider.setMinorTickSpacing(1);
        thermostatSlider.setPaintTicks(true);
        thermostatSlider.setPaintLabels(true);

        customTempField = new JTextField("22", 5);

        // Command history
        historyModel = new DefaultListModel<>();
        commandHistoryList = new JList<>(historyModel);
        commandHistoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        commandHistoryList.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
    }

    private void createLayout() {
        // Main container with tabs
        JTabbedPane tabbedPane = new JTabbedPane();

        // Smart Home Control tab
        JPanel controlPanel = createSmartHomeControlPanel();
        tabbedPane.addTab("🏠 Smart Home Control", controlPanel);

        // Command History tab
        JPanel historyPanel = createCommandHistoryPanel();
        tabbedPane.addTab("📜 Command History", historyPanel);

        // Pattern Explanation tab
        JPanel explanationPanel = createExplanationPanel();
        tabbedPane.addTab("📚 Pattern Explanation", explanationPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createSmartHomeControlPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("🏠 Smart Home Automation System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Main content
        JPanel contentPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        contentPanel.setBackground(new Color(248, 249, 250));

        // Living Room Light Control
        contentPanel.add(createLightControlPanel("💡 Living Room Light", livingRoomLight, livingRoomLightStatus));

        // Bedroom Light Control
        contentPanel.add(createLightControlPanel("💡 Bedroom Light", bedroomLight, bedroomLightStatus));

        // Thermostat Control
        contentPanel.add(createThermostatControlPanel());

        // Security System Control
        contentPanel.add(createSecurityControlPanel());

        panel.add(contentPanel, BorderLayout.CENTER);

        // Control buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(248, 249, 250));

        JButton undoButton = new JButton("↶ Undo Last Command");
        undoButton.setBackground(new Color(255, 193, 7));
        undoButton.setForeground(Color.BLACK);
        undoButton.addActionListener(e -> undoLastCommand());
        buttonPanel.add(undoButton);

        JButton goodNightButton = new JButton("🌙 Good Night Macro");
        goodNightButton.setBackground(new Color(102, 16, 242));
        goodNightButton.setForeground(Color.WHITE);
        goodNightButton.addActionListener(e -> executeGoodNightMacro());
        buttonPanel.add(goodNightButton);

        JButton refreshButton = new JButton("🔄 Refresh Status");
        refreshButton.setBackground(new Color(108, 117, 125));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.addActionListener(e -> updateDeviceStatuses());
        buttonPanel.add(refreshButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createLightControlPanel(String title, SmartLight light, JLabel statusLabel) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder(title));
        panel.setBackground(Color.WHITE);

        // Status display
        JPanel statusPanel = new JPanel(new FlowLayout());
        statusPanel.setBackground(Color.WHITE);
        statusPanel.add(new JLabel("Status: "));
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusPanel.add(statusLabel);
        panel.add(statusPanel, BorderLayout.NORTH);

        // Control buttons
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton onButton = new JButton("Turn ON");
        onButton.setBackground(new Color(25, 135, 84));
        onButton.setForeground(Color.WHITE);
        onButton.addActionListener(e -> {
            SmartHomeCommand command = new LightOnCommand(light);
            executeCommand(command);
        });
        buttonPanel.add(onButton);

        JButton offButton = new JButton("Turn OFF");
        offButton.setBackground(new Color(220, 53, 69));
        offButton.setForeground(Color.WHITE);
        offButton.addActionListener(e -> {
            SmartHomeCommand command = new LightOffCommand(light);
            executeCommand(command);
        });
        buttonPanel.add(offButton);

        JButton dimButton = new JButton("Dim");
        dimButton.setBackground(new Color(255, 193, 7));
        dimButton.setForeground(Color.BLACK);
        dimButton.addActionListener(e -> {
            // For demo purposes, we'll just turn the light on with a message
            SmartHomeCommand command = new LightOnCommand(light);
            executeCommand(command);
            JOptionPane.showMessageDialog(this, "Light dimmed to 50%", "Dim Light", JOptionPane.INFORMATION_MESSAGE);
        });
        buttonPanel.add(dimButton);

        panel.add(buttonPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createThermostatControlPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("🌡️ Smart Thermostat"));
        panel.setBackground(Color.WHITE);

        // Status display
        JPanel statusPanel = new JPanel(new FlowLayout());
        statusPanel.setBackground(Color.WHITE);
        statusPanel.add(new JLabel("Status: "));
        thermostatStatus.setFont(new Font("Arial", Font.BOLD, 14));
        statusPanel.add(thermostatStatus);
        panel.add(statusPanel, BorderLayout.NORTH);

        // Temperature controls
        JPanel controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setBackground(Color.WHITE);
        controlPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Slider control
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        controlPanel.add(new JLabel("Temperature Slider:", JLabel.CENTER), gbc);

        gbc.gridy = 1;
        controlPanel.add(thermostatSlider, gbc);

        gbc.gridy = 2; gbc.gridwidth = 1;
        JButton setSliderTempButton = new JButton("Set Temperature");
        setSliderTempButton.setBackground(new Color(13, 110, 253));
        setSliderTempButton.setForeground(Color.WHITE);
        setSliderTempButton.addActionListener(e -> {
            int temp = thermostatSlider.getValue();
            SmartHomeCommand command = new SetTemperatureCommand(thermostat, temp);
            executeCommand(command);
        });
        controlPanel.add(setSliderTempButton, gbc);

        // Custom temperature input
        gbc.gridx = 0; gbc.gridy = 3;
        controlPanel.add(new JLabel("Custom Temp:"), gbc);
        gbc.gridx = 1;
        controlPanel.add(customTempField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        JButton setCustomTempButton = new JButton("Set Custom Temperature");
        setCustomTempButton.setBackground(new Color(13, 110, 253));
        setCustomTempButton.setForeground(Color.WHITE);
        setCustomTempButton.addActionListener(e -> {
            try {
                int temp = Integer.parseInt(customTempField.getText());
                SmartHomeCommand command = new SetTemperatureCommand(thermostat, temp);
                executeCommand(command);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid temperature number", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        });
        controlPanel.add(setCustomTempButton, gbc);

        panel.add(controlPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSecurityControlPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("🔒 Security System"));
        panel.setBackground(Color.WHITE);

        // Status display
        JPanel statusPanel = new JPanel(new FlowLayout());
        statusPanel.setBackground(Color.WHITE);
        statusPanel.add(new JLabel("Status: "));
        securityStatus.setFont(new Font("Arial", Font.BOLD, 14));
        statusPanel.add(securityStatus);
        panel.add(statusPanel, BorderLayout.NORTH);

        // Control buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton armButton = new JButton("ARM System");
        armButton.setBackground(new Color(220, 53, 69));
        armButton.setForeground(Color.WHITE);
        armButton.addActionListener(e -> {
            SmartHomeCommand command = new SecurityArmCommand(securitySystem);
            executeCommand(command);
        });
        buttonPanel.add(armButton);

        JButton disarmButton = new JButton("DISARM System");
        disarmButton.setBackground(new Color(25, 135, 84));
        disarmButton.setForeground(Color.WHITE);
        disarmButton.addActionListener(e -> {
            // Create a simple disarm command (you might need to implement this)
            JOptionPane.showMessageDialog(this, "Security system disarmed", "Security", JOptionPane.INFORMATION_MESSAGE);
            updateDeviceStatuses();
        });
        buttonPanel.add(disarmButton);

        panel.add(buttonPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createCommandHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("📜 Command Execution History", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        // History list
        JScrollPane scrollPane = new JScrollPane(commandHistoryList);
        scrollPane.setBorder(new TitledBorder("Executed Commands"));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Control buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(248, 249, 250));

        JButton clearHistoryButton = new JButton("Clear History");
        clearHistoryButton.setBackground(new Color(108, 117, 125));
        clearHistoryButton.setForeground(Color.WHITE);
        clearHistoryButton.addActionListener(e -> {
            historyModel.clear();
            controller = new SmartHomeController(); // Reset controller
        });
        buttonPanel.add(clearHistoryButton);

        JButton undoSelectedButton = new JButton("Undo Selected");
        undoSelectedButton.setBackground(new Color(255, 193, 7));
        undoSelectedButton.setForeground(Color.BLACK);
        undoSelectedButton.addActionListener(e -> undoLastCommand());
        buttonPanel.add(undoSelectedButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createExplanationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("📚 Command Pattern Explanation", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        JTextArea explanationArea = new JTextArea();
        explanationArea.setEditable(false);
        explanationArea.setWrapStyleWord(true);
        explanationArea.setLineWrap(true);
        explanationArea.setBackground(new Color(255, 248, 220));
        explanationArea.setBorder(new EmptyBorder(15, 15, 15, 15));
        explanationArea.setText(
            "🎮 COMMAND PATTERN DEMONSTRATION\n\n" +
            "The Command pattern encapsulates a request as an object, allowing you to parameterize clients with different requests, " +
            "queue operations, and support undo operations.\n\n" +
            "KEY COMPONENTS:\n" +
            "• Command Interface: SmartHomeCommand with execute() and undo() methods\n" +
            "• Concrete Commands: LightOnCommand, LightOffCommand, SetTemperatureCommand, SecurityArmCommand\n" +
            "• Receivers: SmartLight, SmartThermostat, SecuritySystem (actual devices)\n" +
            "• Invoker: SmartHomeController (manages and executes commands)\n" +
            "• Client: This GUI application (creates and configures commands)\n\n" +
            "BENEFITS DEMONSTRATED:\n" +
            "✅ Undo Functionality: Each command can be reversed\n" +
            "✅ Macro Commands: Good Night macro executes multiple commands\n" +
            "✅ Command History: Track all executed commands\n" +
            "✅ Decoupling: GUI doesn't know about device implementation details\n" +
            "✅ Extensibility: Easy to add new commands without changing existing code\n\n" +
            "REAL-WORLD APPLICATIONS:\n" +
            "• Smart Home Automation (as shown)\n" +
            "• Text Editor Undo/Redo\n" +
            "• Database Transaction Management\n" +
            "• GUI Button Actions\n" +
            "• Remote Control Systems\n" +
            "• Job Queue Processing"
        );

        JScrollPane scrollPane = new JScrollPane(explanationArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void setupEventHandlers() {
        // Temperature slider change listener
        thermostatSlider.addChangeListener(e -> {
            if (!thermostatSlider.getValueIsAdjusting()) {
                customTempField.setText(String.valueOf(thermostatSlider.getValue()));
            }
        });

        // Custom temperature field listener
        customTempField.addActionListener(e -> {
            try {
                int temp = Integer.parseInt(customTempField.getText());
                if (temp >= 16 && temp <= 30) {
                    thermostatSlider.setValue(temp);
                }
            } catch (NumberFormatException ex) {
                // Ignore invalid input
            }
        });
    }

    private void executeCommand(SmartHomeCommand command) {
        try {
            controller.executeCommand(command);

            // Add to history
            String historyEntry = String.format("[%s] %s",
                java.time.LocalTime.now().toString().substring(0, 8),
                command.getDescription());
            historyModel.addElement(historyEntry);

            // Auto-scroll to bottom
            commandHistoryList.ensureIndexIsVisible(historyModel.getSize() - 1);

            // Update device statuses
            updateDeviceStatuses();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error executing command: " + e.getMessage(),
                "Command Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void undoLastCommand() {
        try {
            controller.undoLastCommand();

            // Add undo entry to history
            String historyEntry = String.format("[%s] UNDO - Last command reversed",
                java.time.LocalTime.now().toString().substring(0, 8));
            historyModel.addElement(historyEntry);

            // Update device statuses
            updateDeviceStatuses();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Cannot undo: " + e.getMessage(),
                "Undo Error",
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void executeGoodNightMacro() {
        try {
            // Create individual commands for the Good Night macro
            List<SmartHomeCommand> goodNightCommands = java.util.Arrays.asList(
                new LightOffCommand(livingRoomLight),
                new LightOffCommand(bedroomLight),
                new SetTemperatureCommand(thermostat, 20),
                new SecurityArmCommand(securitySystem)
            );
            
            // Create and execute Good Night macro command
            GoodNightMacroCommand goodNightMacro = new GoodNightMacroCommand(goodNightCommands);
            
            controller.executeCommand(goodNightMacro);

            // Add to history
            String historyEntry = String.format("[%s] MACRO - Good Night routine executed",
                java.time.LocalTime.now().toString().substring(0, 8));
            historyModel.addElement(historyEntry);

            // Update device statuses
            updateDeviceStatuses();

            JOptionPane.showMessageDialog(this,
                "Good Night macro executed!\n" +
                "• All lights turned off\n" +
                "• Thermostat set to 20°C\n" +
                "• Security system armed",
                "Good Night",
                JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error executing Good Night macro: " + e.getMessage(),
                "Macro Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateDeviceStatuses() {
        // Update living room light status
        livingRoomLightStatus.setText(livingRoomLight.isOn() ? "ON" : "OFF");
        livingRoomLightStatus.setForeground(livingRoomLight.isOn() ?
            new Color(25, 135, 84) : new Color(220, 53, 69));

        // Update bedroom light status
        bedroomLightStatus.setText(bedroomLight.isOn() ? "ON" : "OFF");
        bedroomLightStatus.setForeground(bedroomLight.isOn() ?
            new Color(25, 135, 84) : new Color(220, 53, 69));

        // Update thermostat status
        thermostatStatus.setText(thermostat.getTemperature() + "°C (" + thermostat.getMode() + ")");
        thermostatStatus.setForeground(thermostat.isOn() ?
            new Color(13, 110, 253) : new Color(108, 117, 125));

        // Update security status
        securityStatus.setText(securitySystem.isArmed() ? "ARMED" : "DISARMED");
        securityStatus.setForeground(securitySystem.isArmed() ?
            new Color(220, 53, 69) : new Color(25, 135, 84));

        // Update slider to match thermostat
        thermostatSlider.setValue(thermostat.getTemperature());
        customTempField.setText(String.valueOf(thermostat.getTemperature()));
    }
}
