package motorph.system.cp2;

/**
 *
 * @ De Pano, Gatsola, Gonzales, Villamor | Group 15 H1101
 */

import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.*;

// Implements low-level data entry interceptors and formatting constraints for user input fields.

public class FieldValidatorGUI {
    
    /**
     * Attaches a document transaction filter to prevent non-digit entry patterns.
     * Evaluates text shifts dynamically prior to standard display update operations.
     */

    public static void applyStrictDigitFilter(JTextField field, int limit, boolean allowDecimal) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (isValidInput(fb.getDocument().getText(0, fb.getDocument().getLength()) + string, limit, allowDecimal)) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String nextText = currentText.substring(0, offset) + text + currentText.substring(offset + length);
                if (isValidInput(nextText, limit, allowDecimal)) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            // Matches structural inputs against systemic evaluation rules via uniform expressions.
            
            private boolean isValidInput(String fullResultText, int characterMax, boolean decimalMode) {
                if (fullResultText.isEmpty()) return true;
                if (characterMax > 0 && fullResultText.length() > characterMax) return false;
                if (decimalMode) {
                    return fullResultText.matches("\\d*\\.?\\d*");
                } else {
                    return fullResultText.matches("\\d+");
                }
            }
        });
    }

    /**
     * Registers interactive listeners to apply regulatory identification hyphens automatically.
     * Rebuilds text values using target numeric blocks during terminal text updates.
     */
    
    public static void applyAutoHyphenFilter(JTextField field, String type) {
        field.addKeyListener(new KeyAdapter() {
            private String lastText = "";
            @Override
            public void keyReleased(KeyEvent e) {
                int code = e.getKeyCode();
                // Filters out standard non-text system navigation keys immediately.
                if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_RIGHT || 
                    code == KeyEvent.VK_UP || code == KeyEvent.VK_DOWN || 
                    code == KeyEvent.VK_SHIFT || code == KeyEvent.VK_CONTROL) {
                    return;
                }
                
                String currentText = field.getText();
                if (currentText.equals(lastText)) return;

                String digits = currentText.replaceAll("[^0-9]", "");
                StringBuilder formatted = new StringBuilder();

                // Selects and enforce localized regulatory string segmentation templates.
                if (type.equals("PHONE")) {
                    if (digits.length() > 9) digits = digits.substring(0, 9);
                    for (int i = 0; i < digits.length(); i++) {
                        if (i == 3 || i == 6) formatted.append("-");
                        formatted.append(digits.charAt(i));
                    }
                } else if (type.equals("SSS")) {
                    if (digits.length() > 10) digits = digits.substring(0, 10);
                    for (int i = 0; i < digits.length(); i++) {
                        if (i == 2 || i == 9) formatted.append("-");
                        formatted.append(digits.charAt(i));
                    }
                } else if (type.equals("TIN")) {
                    if (digits.length() > 12) digits = digits.substring(0, 12);
                    for (int i = 0; i < digits.length(); i++) {
                        if (i == 3 || i == 6 || i == 9) formatted.append("-");
                        formatted.append(digits.charAt(i));
                    }
                }

                lastText = formatted.toString();
                int caretPos = field.getCaretPosition();
                boolean isAtEnd = (caretPos == currentText.length());

                // Queues standard caret normalization shifts on the interface loop to preserve entry tracking.
                SwingUtilities.invokeLater(() -> {
                    field.setText(formatted.toString());
                    if (isAtEnd) {
                        field.setCaretPosition(field.getText().length());
                    } else {
                        field.setCaretPosition(Math.min(caretPos, field.getText().length()));
                    }
                });
            }
        });
    }
}
