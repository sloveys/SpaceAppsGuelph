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

        JPanel startDateTimePanel = createDateTimePanel("Start Date and Time: ");
        searchPanel.add(startDateTimePanel);
        
        JPanel endDateTimePanel = createDateTimePanel("End Date and Time: ");
        searchPanel.add(endDateTimePanel);
        
        JPanel stationPanel = createTextPanel("Station: ");
        searchPanel.add(stationPanel);
        
        return searchPanel;
    }
    
    private JPanel createTextPanel(String label) {
        JPanel satellitePanel = new JPanel();
        satellitePanel.setLayout(new BoxLayout(satellitePanel, BoxLayout.X_AXIS));
        
        JTextField satelliteTF = new JTextField();
        satelliteTF.setMaximumSize(new Dimension(satelliteTF.getMaximumSize().width, 40));
        satelliteTF.setFont(FONT);
        
        JLabel satelliteLabel = new JLabel(label, SwingConstants.LEFT);
        satelliteLabel.setMaximumSize(new Dimension(satelliteLabel.getMaximumSize().width , 40));
        satelliteLabel.setFont(FONT);
        satelliteLabel.setLabelFor(satelliteTF);
        
        satellitePanel.add(satelliteLabel); 
        satellitePanel.add(satelliteTF);
        
        return satellitePanel;
    }
    
    private JPanel createDateTimePanel(String label) throws IOException {
        JPanel datePanel = new JPanel();
        datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.Y_AXIS));
        
        UtilCalendarModel dateModel = new UtilCalendarModel();
        dateModel.setYear(1962);
        dateModel.setMonth(8);
        dateModel.setDay(29);
        
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
        
        JDatePanelImpl startDatePanel = new JDatePanelImpl(dateModel, properties);
        JDatePickerImpl datePicker = new JDatePickerImpl(startDatePanel, new DateTextFormatter());
        
        datePicker.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Calendar selectedValue = (Calendar) datePicker.getModel().getValue();
                //System.out.println(selectedValue.toString());
                //Date selectedDate = selectedValue.getTime();
                //System.out.println(selectedDate.toString());
            }
        });
        
        JLabel dateLabel = new JLabel(label, SwingConstants.LEFT);
        dateLabel.setMinimumSize(new Dimension(dateLabel.getMaximumSize().width , 40));
        dateLabel.setFont(FONT);
        dateLabel.setLabelFor(datePicker);
        
        JPanel timePanel = new JPanel();
        timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.X_AXIS));
        
        //Hours
        SpinnerDateModel timeModel = new SpinnerDateModel();
        timeModel.setCalendarField(Calendar.HOUR);
        
        JSpinner hoursSpinner = new JSpinner();
        hoursSpinner.setModel(timeModel);
        hoursSpinner.setEditor(new JSpinner.DateEditor(hoursSpinner, "kk"));
        hoursSpinner.setPreferredSize(new Dimension(50 , hoursSpinner.getMinimumSize().height));
        hoursSpinner.setMaximumSize(new Dimension(50 , 30));
        hoursSpinner.setFont(FONT);
        
        //Minutes
        timeModel = new SpinnerDateModel();
        timeModel.setCalendarField(Calendar.MINUTE);
        
        JSpinner minutesSpinner = new JSpinner();
        minutesSpinner.setModel(timeModel);
        minutesSpinner.setEditor(new JSpinner.DateEditor(minutesSpinner, "mm"));
        minutesSpinner.setPreferredSize(new Dimension(50 , minutesSpinner.getMinimumSize().height));
        minutesSpinner.setMaximumSize(new Dimension(50 , 30));
        minutesSpinner.setFont(FONT);
        
        //Seconds
        timeModel = new SpinnerDateModel();
        timeModel.setCalendarField(Calendar.SECOND);
        
        JSpinner secondsSpinner = new JSpinner();
        secondsSpinner.setModel(timeModel);
        secondsSpinner.setEditor(new JSpinner.DateEditor(secondsSpinner, "ss"));
        secondsSpinner.setPreferredSize(new Dimension(50 , secondsSpinner.getMinimumSize().height));
        secondsSpinner.setMaximumSize(new Dimension(50 , 30));
        secondsSpinner.setFont(FONT);
        
        timePanel.add(hoursSpinner);
        timePanel.add(minutesSpinner);
        timePanel.add(secondsSpinner);
        
        datePanel.add(dateLabel);
        datePanel.add(datePicker);
        datePanel.add(timePanel);
        
        return datePanel;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        return mainPanel;
    }
    
    
}
