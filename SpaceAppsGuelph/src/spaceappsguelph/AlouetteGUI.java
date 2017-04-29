/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceappsguelph;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import org.jdatepicker.impl.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel Kolodziejczak
 */
public class AlouetteGUI extends JFrame {
    public final int WIDTH = 1280, HEIGHT = 720;
    public static final Font FONT = new Font("Serif", Font.PLAIN, 16);
    
    private final MTUList mtuList;

    public AlouetteGUI() throws IOException {
        super("Alouette Data Manager");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        mtuList = new MTUList();
        
        final JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.EAST);
        
        final JPanel searchPanel = createSearchPanel();
        add(searchPanel, BorderLayout.WEST);
    }
    
    private JPanel createSearchPanel() throws IOException {
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        
        UtilCalendarModel model = new UtilCalendarModel();
        
        //Localization
        Properties properties = new Properties();
        InputStream propsFile;
        try {
            propsFile = AlouetteGUI.class.getResourceAsStream("/org/jdatepicker/i18n/Text.properties"); //default english
            properties.load(propsFile);
            propsFile.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AlouetteGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        JDatePanelImpl startDatePanel = new JDatePanelImpl(model, properties);
        JDatePickerImpl datePicker = new JDatePickerImpl(startDatePanel, new DateTextFormatter());
        
        searchPanel.add(datePicker);        
        
        return searchPanel;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        return mainPanel;
    }
    
    
}
