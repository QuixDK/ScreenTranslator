import com.formdev.flatlaf.FlatDarkLaf;
import ru.sgugt.logger.Log;

import javax.swing.*;
import java.awt.*;

public class Main {

    private static final Log log = new Log();

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch ( Exception ex ) {
            System.err.println("Failed to initialize LaF");
        }
        for ( Window window : JFrame.getWindows() ) {
            SwingUtilities.updateComponentTreeUI(window);
        }
        SwingUtilities.invokeLater(new MainForm(log));
    }
}