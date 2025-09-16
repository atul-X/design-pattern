package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Interactive GUI demonstration of the Observer Pattern
 * Placeholder for future implementation of stock market simulation
 */
public class ObserverPatternGUI extends JPanel {

    public ObserverPatternGUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        createLayout();
    }

    private void createLayout() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(248, 249, 250));

        // Title
        JLabel titleLabel = new JLabel("👁️ Observer Pattern - Stock Market Simulation", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(0, 0, 30, 0));
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        // Coming soon message
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBackground(Color.WHITE);
        messagePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(222, 226, 230), 1),
            new EmptyBorder(40, 40, 40, 40)
        ));

        JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>" +
            "<h2>🚧 Coming Soon!</h2>" +
            "<p>Interactive Observer Pattern demonstration featuring:</p>" +
            "<ul style='text-align: left; margin: 20px 0;'>" +
            "<li>📈 Real-time stock price updates</li>" +
            "<li>🤖 Trading bot observers with different strategies</li>" +
            "<li>📊 Portfolio tracker with live calculations</li>" +
            "<li>🔔 Price alert system</li>" +
            "<li>📱 Visual notification system</li>" +
            "</ul>" +
            "<p><i>This will showcase how multiple observers react to stock price changes<br>" +
            "demonstrating the power of the Observer pattern in event-driven systems.</i></p>" +
            "</div></html>", JLabel.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        messageLabel.setForeground(new Color(108, 117, 125));
        messagePanel.add(messageLabel, BorderLayout.CENTER);

        contentPanel.add(messagePanel, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);
    }
}
