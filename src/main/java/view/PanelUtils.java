package view;
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
}


