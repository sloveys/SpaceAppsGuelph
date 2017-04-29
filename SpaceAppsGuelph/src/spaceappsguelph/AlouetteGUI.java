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
import java.awt.event.*;

/**
 *
 * @author Daniel Kolodziejczak
 */
public class AlouetteGUI extends JFrame {
    public final int WIDTH = 1280, HEIGHT = 720;
    public static final Font FONT = new Font("Serif", Font.PLAIN, 20);
    
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
        
        JPanel satellitePanel = createTextPanel("Satellite: ");
        searchPanel.add(satellitePanel);

        JDatePickerImpl datePicker = createDateTimePicker();
        
        searchPanel.add(datePicker);
        
        JPanel stationPanel = createTextPanel("Station: ");
        searchPanel.add(stationPanel);
        
        return searchPanel;
    }
    
    private JPanel createTextPanel(String label) {
        JPanel satellitePanel = new JPanel();
        satellitePanel.setLayout(new BoxLayout(satellitePanel, BoxLayout.X_AXIS));
        
        JTextField satelliteTF = new JTextField();
        satelliteTF.setAlignmentX(Component.LEFT_ALIGNMENT);
        satelliteTF.setMaximumSize(new Dimension(satelliteTF.getMaximumSize().width, 40));
        satelliteTF.setFont(FONT);
        
        JLabel satelliteLabel = new JLabel(label);
        satelliteLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        satelliteLabel.setMaximumSize(new Dimension(satelliteLabel.getMaximumSize().width , 40));
        satelliteLabel.setFont(FONT);
        satelliteLabel.setLabelFor(satelliteTF);
        
        satellitePanel.add(satelliteLabel); 
        satellitePanel.add(satelliteTF);
        
        return satellitePanel;
    }
    
    private JDatePickerImpl createDateTimePicker() throws IOException {
        UtilCalendarModel model = new UtilCalendarModel();
        model.setYear(1962);
        model.setMonth(8);
        model.setDay(29);
        
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
        
        datePicker.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Calendar selectedValue = (Calendar) datePicker.getModel().getValue();
                //System.out.println(selectedValue.toString());
                //Date selectedDate = selectedValue.getTime();
                //System.out.println(selectedDate.toString());
            }
        });
        
        return datePicker;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        return mainPanel;
    }
    
    
}
