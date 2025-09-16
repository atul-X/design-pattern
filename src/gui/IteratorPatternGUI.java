package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Interactive GUI demonstration of the Iterator Pattern
 * Placeholder for future implementation of notification management system
 */
public class IteratorPatternGUI extends JPanel {

    public IteratorPatternGUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        createLayout();
    }

    private void createLayout() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(248, 249, 250));

        // Title
        JLabel titleLabel = new JLabel("🔍 Iterator Pattern - Notification Management", JLabel.CENTER);
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
            "<p>Interactive Iterator Pattern demonstration featuring:</p>" +
            "<ul style='text-align: left; margin: 20px 0;'>" +
            "<li>📧 Email notifications (ArrayList implementation)</li>" +
            "<li>📱 SMS notifications (ArrayDeque/Queue implementation)</li>" +
            "<li>🔔 Push notifications (LinkedHashSet implementation)</li>" +
            "<li>🔄 Uniform iteration across different data structures</li>" +
            "<li>📊 Visual comparison of collection behaviors</li>" +
            "</ul>" +
            "<p><i>This will demonstrate how the Iterator pattern provides uniform access<br>" +
            "to different collection types while hiding their internal structure.</i></p>" +
            "</div></html>", JLabel.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        messageLabel.setForeground(new Color(108, 117, 125));
        messagePanel.add(messageLabel, BorderLayout.CENTER);

        contentPanel.add(messagePanel, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);
    }
}
