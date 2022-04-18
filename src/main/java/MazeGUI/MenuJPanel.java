package MazeGUI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.awt.GridBagConstraints.*;

public class MenuJPanel extends JPanel {

    private static final int LEFT_SPACING = 2;
    private static final int RIGHT_SPACING = 2;
    private static final int BETWEEN_MENU_SPACING = 5;
    private static final int BETWEEN_ITEM_SPACING = 0;
    private static final Color SUBMENU_COLOR = Color.decode("#f8f9fb");
    private static final Font TITLE_FONT = new Font("Arial", Font.PLAIN, 24);
    private static final Font SUBMENU_FONT = new Font("Arial", Font.PLAIN, 16);
    private static final Border TITLE_BUTTON_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(2, 0, 2, 0, Color.gray),
            BorderFactory.createEmptyBorder(0, 10, 0, 0));
    private static final Border SUBMENU_BUTTON_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.lightGray),
            BorderFactory.createEmptyBorder(2, 20, 2, 0));

    private int titleBtnHeight = 20;
    GridBagConstraints gBC;

    public MenuJPanel() {
        super();

        // Setup Layout
        GridBagLayout gridBag = new GridBagLayout();
        this.setLayout(gridBag);

        // Create default GidBagConstraints
        gBC = new GridBagConstraints(0, RELATIVE, 1, 1, 1, 0,
                CENTER, HORIZONTAL, new Insets(0, LEFT_SPACING, BETWEEN_ITEM_SPACING, RIGHT_SPACING), 0, 0);
    }

    public void CreateMenu(String title, String[] menuItems) {
        // Create title button
        JButton titleButton = new JButton(title);
        titleButton.setPreferredSize(new Dimension(100, 50));
        gBC.gridy++;
        gBC.gridheight = 2;

        // Customise title button
        titleButton.setBackground(Color.white);
        titleButton.setFont(TITLE_FONT);
        titleButton.setHorizontalAlignment(SwingConstants.LEFT);
        titleButton.setBorder(TITLE_BUTTON_BORDER);

        // Add title button
        this.add(titleButton, gBC);
        gBC.gridy++;

        // Create content buttons and add their action listener
        JButton[] contentButtons = CreateContent(menuItems);
        titleButton.addActionListener(new ActionListener() {
            boolean toggle = true;
            @Override
            public void actionPerformed(ActionEvent e) {
                toggle = !toggle;
                for(JButton btn : contentButtons) {
                    btn.setVisible(toggle);
                }
            }
        });
    }

    public void finalisePanel() {
        JPanel spacingJPanel = new JPanel();
        gBC.weighty = 1;
        gBC.gridy++;
        this.add(spacingJPanel, gBC);
    }

    private JButton[] CreateContent(String[] menuItemNames) {
        // Create the returnable list and set up the grid-box constraints required
        JButton[] buttonList = new JButton[menuItemNames.length];
        gBC.gridheight = 1;

        // For each button name, create a button, link its action listener, and customise it
        for(int i = 0; i < menuItemNames.length; i++) {
            // Create the button
            JButton btn = new JButton(menuItemNames[i]);

            // Style it
            btn.setBackground(SUBMENU_COLOR);
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setBorder(SUBMENU_BUTTON_BORDER);
            btn.setFont(SUBMENU_FONT);

            // Add the button to the list and menu
            buttonList[i] = btn;
            gBC.gridy++;
            if(i == menuItemNames.length - 1) {
                gBC.insets = new Insets(0, LEFT_SPACING, BETWEEN_MENU_SPACING, RIGHT_SPACING);
            }
            this.add(buttonList[i], gBC);
        }

        // Reset edited
        gBC.insets = new Insets(0, LEFT_SPACING, BETWEEN_ITEM_SPACING, RIGHT_SPACING);
        return buttonList;
    }
}
