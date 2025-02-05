package view;
import controller.ActionHandler;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PanelUtils {

    private PanelUtils() {
    }

    public static JPanel createLeftPanel(JPanel listPanel) {
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(30, 33, 40));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(300, 600));
        leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(new Color(30, 33, 40));
        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        leftPanel.add(scrollPane);

        return leftPanel;
    }

    public static JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        return panel;
    }

    public static GridBagConstraints getDefaultGridBagConstraints(int gridX) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = gridX;
        return gbc;
    }

    public static void addButtonToPanel(JPanel panel, JButton button, int gridX) {
        GridBagConstraints gbc = getDefaultGridBagConstraints(gridX);
        panel.add(button, gbc);
    }

    public static BackgroundPanel createBackgroundPanel(String backgroundImagePath) {
        BackgroundPanel panel = new BackgroundPanel(backgroundImagePath);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        return panel;
    }

    public static void setupMainLayout(Container container, JPanel leftPanel, JPanel rightPanel) {
        container.add(leftPanel, BorderLayout.WEST);

        if (rightPanel != null) {
            container.add(rightPanel, BorderLayout.CENTER);
        }

        container.setVisible(true);
    }

    public static void configureMainWindow(JFrame frame, String title) {
        frame.setTitle(title);
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
    }

    public static JPanel createStyledPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(42, 46, 54));
        return panel;
    }
}


