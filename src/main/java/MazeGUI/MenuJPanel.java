package MazeGUI;

import Utils.Debug;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static java.awt.GridBagConstraints.*;

public class MenuJPanel extends JPanel {

    public static final Color HIGHLIGHT_COLOR = Color.decode("#bdfaa5");
    // Spacing
    private static final int LEFT_SPACING = 2;
    private static final int RIGHT_SPACING = 2;
    private static final int BETWEEN_MENU_SPACING = 5;
    private static final int BETWEEN_ITEM_SPACING = 0;

    // Menu Colours
    private static final Color TITLE_COLOR = Color.decode("#ffffff");
    private static final Color SUBMENU_COLOR = Color.decode("#f8f9fb");

    // Fonts
    private static final Font TITLE_FONT = new Font("Arial", Font.PLAIN, 24);
    private static final Font SUBMENU_FONT = new Font("Arial", Font.PLAIN, 16);

    // Borders
    private static final Border TITLE_BUTTON_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(2, 0, 2, 0, Color.gray),
            BorderFactory.createEmptyBorder(0, 10, 0, 0));
    private static final Border SUBMENU_BUTTON_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.lightGray),
            BorderFactory.createEmptyBorder(5, 20, 5, 0));

    private JButton[][] buttons;
    private ArrayList<JButton[]> tempButtonList;

    GridBagConstraints gBC;

    /**
     * Creates the panel and prepares the internal layout
     * After calling this, use CreateMenu() to populate it
     */
    public MenuJPanel() {
        super();

        // Setup the arraylist
        tempButtonList = new ArrayList<>();

        // Setup Layout
        GridBagLayout gridBag = new GridBagLayout();
        this.setLayout(gridBag);

        // Create default GidBagConstraints
        gBC = new GridBagConstraints(0, RELATIVE, 1, 1, 1, 0,
                CENTER, HORIZONTAL, new Insets(0, LEFT_SPACING, BETWEEN_ITEM_SPACING, RIGHT_SPACING), 0, 0);
    }

    /**
     * Creates a new menu with specified submenu elements
     * REMEMBER to call FinalisePanel() afterwards to fix formatting errors
     *
     * @param title The title of the expander
     * @param menuItems A string array of the menu elements
     * @param submenuListeners An array of action listeners for the submenu buttons, this must be the same size as
     *                         the menu items array
     */
    public void CreateMenu(String title, String[] menuItems, ActionListener[] submenuListeners, boolean autoHighlight) {
        // Check that menuItems length matches submenuListeners length
        if(menuItems.length != submenuListeners.length) {
            Debug.LogLn("menuItems length does not match submenuListeners length, Menu '" + title + "' was not created");
            return;
        }

        // Create title button
        JButton titleButton = new JButton(title);
        titleButton.setPreferredSize(new Dimension(100, 50));
        gBC.gridy++;
        gBC.gridheight = 2;

        // Customise title button
        titleButton.setBackground(TITLE_COLOR);
        titleButton.setFont(TITLE_FONT);
        titleButton.setHorizontalAlignment(SwingConstants.LEFT);
        titleButton.setBorder(TITLE_BUTTON_BORDER);

        // Add title button
        this.add(titleButton, gBC);
        gBC.gridy++;

        // Create content buttons and add their action listener
        JButton[] contentButtons = CreateContent(menuItems, submenuListeners, autoHighlight);
        tempButtonList.add(contentButtons);
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

    /**
     * Call after all menus have been created with CreateMenu() to
     * finalise the formatting
     */
    public void FinalisePanel() {
        JPanel spacingJPanel = new JPanel();
        gBC.weighty = 1;
        gBC.gridy++;
        buttons = tempButtonList.toArray(JButton[][]::new);
        this.add(spacingJPanel, gBC);
    }

    /**
     * Sets all buttons in the specified submenu to be enabled or disabled
     *
     * @param menuID submenu id
     * @param enabled whether they should be enabled or disabled (true for enabled)
     */
    public void SetSubmenuIsEnabled(int menuID, boolean enabled) {
        if(buttons.length <= menuID) { return; }
        for(JButton b : buttons[menuID]) {
            b.setEnabled(enabled);
        }
    }

    public void ClearHighlighting() {
        for(JButton[] menu : buttons) {
            for(JButton b : menu) {
                b.setBackground(SUBMENU_COLOR);
            }
        }
    }

    private JButton[] CreateContent(String[] menuItemNames, ActionListener[] submenuListeners, boolean autoHighlight) {
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

            // Add action listener
            if(submenuListeners[i] != null) {
                btn.addActionListener(submenuListeners[i]);
            }
            if(autoHighlight) {
                btn.addActionListener(highlightListener);
            }

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

    ActionListener highlightListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ClearHighlighting();
            ((JButton)e.getSource()).setBackground(HIGHLIGHT_COLOR);
        }
    };
}
