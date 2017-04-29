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
public class TimeStamp {
    private int year; // year zero is 1963
    private int dayOfYear;
    private int hour; // 24 hour format
    private int minute;
    private int second;
    
    public TimeStamp(int year, int dayOfYear, int hour, int minute, int second) throws Exception {
        if (!(yearIsValid(year) && ...)) {
            throw new Exception("...");
        }
        this.year = year;
        this.dayOfYear = dayOfYear;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }
    
    private boolean yearIsValid(int x) {
        if (x<0) {
            return false;
        }
        return true;
    }
}
