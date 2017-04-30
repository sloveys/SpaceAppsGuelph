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
public class TimeStamp {
    private final int year; // year zero is 1960 *****
    private final int dayOfYear;
    private final int hour; // 24 hour format
    private final int minute;
    private final int second;
    
    public TimeStamp(int year, int dayOfYear, int hour, int minute, int second) throws Exception {
        if (!(yearIsValid(year) && dayOfYearIsValid(dayOfYear) && hourIsValid(hour) && minuteIsValid(minute) && secondIsValid(second))) {
            throw new Exception("TimeStamp: Illegal time parameters.");
        }
        this.year = year;
        this.dayOfYear = dayOfYear;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }
    
    public int getYear() {
        return year;
    }
    
    public int getDayOfYear() {
        return dayOfYear;
    }
    
    public int getHour() {
        return hour;
    }
    
    public int getMinute() {
        return minute;
    }
    
    public int getSecond() {
        return second;
    }
    
    public static int yToyyyy(int year) {
        if (year >= 2)
            return (year + 1960);
        return (year + 1970);
    }
    
    public static int yyyyToy(int year) {
        if (year >= 1970)
            return (year - 1970);
        return (year-1960);
    }
    
    private boolean yearIsValid(int x) { // could use makeing this more specific
        if (x < 0) {
            return false;
        }
        return true;
    }
    
    private boolean dayOfYearIsValid(int x) {
        if (x < 1 || x > 365) {
            return false;
        }
        return true;
    }
    
    private boolean hourIsValid(int x) {
        if (x < 1 || x > 24) {
            return false;
        }
        return true;
    }
    
    private boolean minuteIsValid(int x) {
        if (x < 1 || x > 60) {
            return false;
        }
        return true;
    }
    
    private boolean secondIsValid(int x) {
        if (x < 1 || x > 60) {
            return false;
        }
        return true;
    }
}
