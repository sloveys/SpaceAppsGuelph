/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spaceappsguelph;

import java.io.Serializable;

/**
 *
 * @author Loveys
 */
public class Metadata implements Serializable {
    private final TimeStamp tStamp;
    private final int satellite;
    private final int station;
    //private final String url;
    
    public Metadata(int satellite, int station, int year, int dayOfYear, int hour, int minute, int second) throws Exception {
        if (!(satelliteIsValid(satellite) && stationIsValid(station))) {
            throw new Exception("MetadataToURL: Illegal satellite, station, or URL.");
        }
        
        tStamp = new TimeStamp(year, dayOfYear, hour, minute, second);
        this.satellite = satellite;
        this.station = station;
        //this.url = url;
    }
    
    public int getSatellite() {
        return satellite;
    }
    
    public int getStation() {
        return station;
    }
    
//    public String getURL() {
//        return url;
//    }
    
    public TimeStamp getTimeStamp() {
        return tStamp;
    }
    
    public int getYear() {
        return tStamp.getYear();
    }
    
    public int getDayOfYear() {
        return tStamp.getDayOfYear();
    }
    
    public int getHour() {
        return tStamp.getHour();
    }
    
    public int getMinute() {
        return tStamp.getMinute();
    }
    
    public int getSecond() {
        return tStamp.getSecond();
    }
    
    private boolean satelliteIsValid(int x) {
        return (x == 1 || x == 2);
    }
    
    private boolean stationIsValid(int x) {
        return (x > 0 && x < 21);
    }
    
    private boolean urlIsValid(String x) {
        return (!x.isEmpty());
    }
    
    public boolean equals(Object other) {
        Metadata otherMtoU = (Metadata)other;
        return tStamp.equals(otherMtoU.tStamp)
                && satellite == otherMtoU.satellite
                && station == otherMtoU.station;
    }
}
