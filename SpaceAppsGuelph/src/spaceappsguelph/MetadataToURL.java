/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spaceappsguelph;

/**
 *
 * @author Loveys
 */
public class MetadataToURL {
    private final TimeStamp tStamp;
    private final int satellite;
    private final int station;
    private final String url;
    
    public MetadataToURL(int satellite, int station, String url, int year, int dayOfYear, int hour, int minute, int second) throws Exception {
        
        
        tStamp = new TimeStamp(year, dayOfYear, hour, minute, second);
        this.satellite = satellite;
        this.station = station;
        this.url = url;
    }
    
    private boolean satelliteIsValid(int x) {
        return (x == 1 || x == 2);
    }
}
