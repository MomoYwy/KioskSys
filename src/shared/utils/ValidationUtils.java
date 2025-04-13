package shared.utils;

import java.awt.Window;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class ValidationUtils {

    /**
     * Validates that none of the components have empty text
     *  parent The parent component for error dialogs
     *  components Array of components to validate (JTextField, JComboBox, etc.)
     *  fieldNames Names of fields for error messages
     *  true if all valid, false if any empty
     */
    public static boolean validateNotEmpty(Window parent, JComponent[] components, String[] fieldNames) {
        for (int i = 0; i < components.length; i++) {
            String text = "";
            
            if (components[i] instanceof JTextField) {
                text = ((JTextField) components[i]).getText().trim();
            } 
            else if (components[i] instanceof JComboBox) {
                Object selected = ((JComboBox<?>) components[i]).getSelectedItem();
                text = selected != null ? selected.toString().trim() : "";
            }
            
            if (text.isEmpty()) {
                JOptionPane.showMessageDialog(parent,
                    "Please enter " + fieldNames[i],
                    "Missing Field",
                    JOptionPane.WARNING_MESSAGE);
                components[i].requestFocus();
                return false;
            }
        }
        return true;
    }

    /**
     * Validates that a string is a valid double
     *  parent The parent component for error dialogs
     * text The text to validate
     *  fieldName Name of field for error message
     *  true if valid, false if invalid
     */
    public static boolean validateDouble(JComponent parent, String text, String fieldName) {
        try {
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(parent,
                "Please enter a valid number for " + fieldName,
                "Invalid Input",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }
}