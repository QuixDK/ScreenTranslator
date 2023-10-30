package ru.ssugt;

import com.formdev.flatlaf.FlatDarkLaf;;
import ru.ssugt.forms.MainForm;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch ( Exception ex ) {
            System.err.println("Failed to initialize LaF");
        }
        for ( Window window : JFrame.getWindows() ) {
            SwingUtilities.updateComponentTreeUI(window);
        }
        SwingUtilities.invokeLater(new MainForm());
    }

}