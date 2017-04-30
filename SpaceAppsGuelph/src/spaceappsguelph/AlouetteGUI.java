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
    private static final JTextArea textArea = new JTextArea(10, 30);

    private final MTUList mtuList;

    public AlouetteGUI() throws IOException {
        super("Alouette Data Manager");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        mtuList = new MTUList();
        
        final JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);
        
        final JPanel urlDisplay = createUrlPanel();
        add(urlDisplay, BorderLayout.EAST);
        
        final JPanel searchPanel = createSearchPanel(urlDisplay);
        add(searchPanel, BorderLayout.WEST);
        
        
    }
    
    private JPanel createSearchPanel(JPanel outputField) throws IOException {
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        JPanel satPanel = new JPanel();
        satPanel.setLayout(new BorderLayout());
        satPanel.setSize(10, 10);
        JLabel satCombo = new JLabel("Satellite:");
        satPanel.add(satCombo, BorderLayout.WEST);
        searchPanel.add(satPanel);
        String [] satellites = {"Alouette"};
        JComboBox satelliteBox = new JComboBox(satellites);
        searchPanel.add(satelliteBox);
//        JPanel satellitePanel = createTextPanel("Satellite: ");
//        searchPanel.add(satellitePanel);

        
        
        JDatePickerImpl startDatePicker = null;
        JSpinner startHoursSpinner = null, startMinutesSpinner = null, startSecondsSpinner = null;
        createDateTimePanel("Start Date and Time (Hour, Minute, Second): ", searchPanel, startDatePicker, 
                startHoursSpinner, startMinutesSpinner, startSecondsSpinner);
        
        JDatePickerImpl endDatePicker = null;
        JSpinner endHoursSpinner = null, endMinutesSpinner = null, endSecondsSpinner = null;
        createDateTimePanel("End Date and Time (Hour, Minute, Second): ", searchPanel, endDatePicker,
                endHoursSpinner, endMinutesSpinner, endSecondsSpinner);
        
        
        JPanel stationPanel = new JPanel();
        stationPanel.setLayout(new BorderLayout());
        stationPanel.setSize(10, 10);
        JLabel stationCombo = new JLabel("Station:");
        stationPanel.add(stationCombo, BorderLayout.WEST);
        searchPanel.add(stationPanel);
        
        String [] stations = {"Resolute Bay, NWT","Prince Albert, AB","Ottawa, ON","St John's, NL","Fairbanks, USA","Fort Myers, USA","Quito, Ecuador","Antofagasta, Chile","Falkland Islands, UK","Winkfield, UK","Singapore, Malaysia","Woomera, AUS","Grand Forks, USA","Blossom Point, USA","South Point, USA","Johannesburg, SA","Mojave, USA","Winkfield, UK (2)","Fairbanks, USA (2)","Rosman, USA"};
        JComboBox stationsBox = new JComboBox(stations);
        searchPanel.add(stationsBox);
        
            
        JButton searchButton = new JButton("Search");
        searchButton.setFont(FONT);
        
        
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Calendar startCalendarValue = (Calendar) startDatePicker.getModel().getValue();
                
                int startYear = startCalendarValue.get(Calendar.YEAR);
                int startDayOfYear = startCalendarValue.get(Calendar.DAY_OF_YEAR);
                int startHours = (int)startHoursSpinner.getValue();
                int startMinutes = (int)startMinutesSpinner.getValue();
                int startSeconds = (int)startSecondsSpinner.getValue();
                TimeStamp startTimeStamp = null;
                try {
                    startTimeStamp = new TimeStamp(startYear, startDayOfYear, startHours, startMinutes, startSeconds);
                } catch (Exception ex) {
                    Logger.getLogger(AlouetteGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            
                Calendar endCalendarValue = (Calendar) endDatePicker.getModel().getValue();
                
                int endYear = endCalendarValue.get(Calendar.YEAR);
                int endDayOfYear = endCalendarValue.get(Calendar.DAY_OF_YEAR);
                int endHours = (int)endHoursSpinner.getValue();
                int endMinutes = (int)endMinutesSpinner.getValue();
                int endSeconds = (int)endSecondsSpinner.getValue();
                TimeStamp endTimeStamp = null;
                try {
                    endTimeStamp = new TimeStamp(endYear, endDayOfYear, endHours, endMinutes, endSeconds);
                } catch (Exception ex) {
                    Logger.getLogger(AlouetteGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                int sattelite = 0;
                if (satelliteBox.getSelectedItem().equals("Alouette"))
                {
                    sattelite = 1;
                }
                int stationNum = 0;
                if (stationsBox.getSelectedItem().equals("Resolute Bay, NWT"))
                    stationNum = 1;
                else if (stationsBox.getSelectedItem().equals("Prince Albert, AB"))
                    stationNum = 2;
                else if (stationsBox.getSelectedItem().equals("Ottawa, ON"))
                    stationNum = 3;
                else if (stationsBox.getSelectedItem().equals("St John's, NL"))
                    stationNum = 4;
                else if (stationsBox.getSelectedItem().equals("Fairbanks, USA"))
                    stationNum = 5;
                else if (stationsBox.getSelectedItem().equals("Fort Myers, USA"))
                    stationNum = 6;
                else if (stationsBox.getSelectedItem().equals("Quito, Ecuador"))
                    stationNum = 7;
                else if (stationsBox.getSelectedItem().equals("Antofagasta, Chile"))
                    stationNum = 8;
                else if (stationsBox.getSelectedItem().equals("Falkland Islands, UK"))
                    stationNum = 9;
                else if (stationsBox.getSelectedItem().equals("Winkfield, UK"))
                    stationNum = 10;
                else if (stationsBox.getSelectedItem().equals("Singapore, Malaysia"))
                    stationNum = 11;
                else if (stationsBox.getSelectedItem().equals("Woomera, AUS"))
                    stationNum = 12;
                else if (stationsBox.getSelectedItem().equals("Grand Forks, USA"))
                    stationNum = 13;
                else if (stationsBox.getSelectedItem().equals("Blossom Point, USA"))
                    stationNum = 14;
                else if (stationsBox.getSelectedItem().equals("South Point, USA"))
                    stationNum = 15;
                else if (stationsBox.getSelectedItem().equals("Johannesburg, SA"))
                    stationNum = 16;
                else if (stationsBox.getSelectedItem().equals("Mojave, USA"))
                    stationNum = 17;
                else if (stationsBox.getSelectedItem().equals("Winkfield, UK (2)"))
                    stationNum = 18;
                else if (stationsBox.getSelectedItem().equals("Fairbanks, USA (2)"))
                    stationNum = 19;
                else if (stationsBox.getSelectedItem().equals("Rosman, USA"))
                    stationNum = 20;
                
                ArrayList<String> urls = new ArrayList<>();
                textArea.append("TESTING\n");
                urls = mtuList.searchUrls(sattelite, startTimeStamp, endTimeStamp, stationNum);
                for (String i : urls)
                {
                    textArea.append(i);
                }
                
            }
        });
        searchPanel.add(Box.createRigidArea(new Dimension(10, 15)));
   
        searchPanel.add(searchButton);
        searchPanel.add(Box.createRigidArea(new Dimension(10, 60)));
    
        JPanel urlLabelPanel = new JPanel();
        urlLabelPanel.setLayout(new BorderLayout());
        urlLabelPanel.setSize(10, 10);
        JLabel urlLabel = new JLabel("Add an Ionograph by URL");
        urlLabelPanel.add(urlLabel, BorderLayout.WEST);
        searchPanel.add(urlLabelPanel);
        
        
        JTextField newUrl = new JTextField();
        searchPanel.add(newUrl);
        
        searchPanel.add(Box.createRigidArea(new Dimension(10, 15)));

        JButton addButton = new JButton("Add");
        addButton.setFont(FONT);
        
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent f) {
                if (newUrl.getText().isEmpty())
                {
                    textArea.append("Please input a URL\n");
                    return;
                }
                else
                    textArea.append("Processing...\n");
                Metadata temp = BmpAlgorithms.masterAlgo(newUrl.getText());
                if (temp == null)
                {
                    textArea.append("There was an error decoding that Ionograph, it is either corrupted or the URL is incorrect.\n");
//                    textArea.setText("ballin");
                }
                else
                {
                    textArea.append("Ionograph successfully added to database.\n");
                }
            }
        });
        
        searchPanel.add(addButton);
        searchPanel.add(Box.createRigidArea(new Dimension(10, 45)));
        //testing
//        JButton addFolderButton = new JButton("Add from Folder");
//        addFolderButton.setFont(FONT);
//        
//        addFolderButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent f) {
//                JFileChooser fileChooser = new JFileChooser();
//                fileChooser.showOpenDialog(rootPane);
//                File file = fileChooser.getSelectedFile();
//                File [] ionos = file.listFiles();
//                for (File i : ionos)
//                {
//                    Metadata temp = BmpAlgorithms.masterAlgo(i);
//                    if (temp == null) {
//                        textArea.append("There was an error decoding that Ionograph, it is either corrupted or the URL is incorrect.\n");
////                    textArea.setText("ballin");
//                    } else {
//                        textArea.append("Ionograph successfully added to database.\n");
//                    }
//                }
//            }
//        });
//        
//        searchPanel.add(addFolderButton);
        
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
    
    private JPanel createDateTimePanel(String label, JPanel outerPanel, JDatePickerImpl datePicker,
        JSpinner hoursSpinner, JSpinner minutesSpinner, JSpinner secondsSpinner) throws IOException {
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
        datePicker = new JDatePickerImpl(startDatePanel, new DateTextFormatter());
        
        JLabel dateLabel = new JLabel(label, SwingConstants.LEFT);
        dateLabel.setMinimumSize(new Dimension(dateLabel.getMaximumSize().width , 40));
        dateLabel.setFont(FONT);
        dateLabel.setLabelFor(datePicker);
        
        JPanel timePanel = new JPanel();
        timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.X_AXIS));
        
        //Hours
        SpinnerDateModel timeModel = new SpinnerDateModel();
        timeModel.setCalendarField(Calendar.HOUR);
        
        hoursSpinner = new JSpinner();
        hoursSpinner.setModel(timeModel);
        hoursSpinner.setEditor(new JSpinner.DateEditor(hoursSpinner, "kk"));
        hoursSpinner.setPreferredSize(new Dimension(50 , hoursSpinner.getMinimumSize().height));
        hoursSpinner.setMaximumSize(new Dimension(50 , 30));
        hoursSpinner.setFont(FONT);
        
        //Minutes
        timeModel = new SpinnerDateModel();
        timeModel.setCalendarField(Calendar.MINUTE);
        
        minutesSpinner = new JSpinner();
        minutesSpinner.setModel(timeModel);
        minutesSpinner.setEditor(new JSpinner.DateEditor(minutesSpinner, "mm"));
        minutesSpinner.setPreferredSize(new Dimension(50 , minutesSpinner.getMinimumSize().height));
        minutesSpinner.setMaximumSize(new Dimension(50 , 30));
        minutesSpinner.setFont(FONT);
        
        //Seconds
        timeModel = new SpinnerDateModel();
        timeModel.setCalendarField(Calendar.SECOND);
        
        secondsSpinner = new JSpinner();
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
        
        outerPanel.add(datePanel);
        
        return datePicker;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        return mainPanel;
    }
    
    private JPanel createUrlPanel() {
        JPanel urlPanel = new JPanel();
        urlPanel.setLayout(new FlowLayout());
        JScrollPane output = new JScrollPane(textArea);
        textArea.setEditable(false);
        add(output);
        return(urlPanel);
    }
    
    private class manageMTtoUrlList extends WindowAdapter {
        private String fileAddress;

        public manageMTtoUrlList(String fileAddress) {
            this.fileAddress = fileAddress;
        }

        @Override
        public void windowOpened(WindowEvent e) {
            mtuList.loadObj(fileAddress);
            System.out.println("Successfully loaded prodcuts from file: " + fileAddress);
        }

        @Override
        public void windowClosing(WindowEvent e) {
            mtuList.saveObj(fileAddress);
            System.out.println("Successfully saved prodcuts to file: " + fileAddress);
        }
    }
}

