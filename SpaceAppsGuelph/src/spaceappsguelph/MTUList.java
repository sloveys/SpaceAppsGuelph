/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spaceappsguelph;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


/**
 *
 * @author Loveys
 */
public class MTUList {
    private HashMap<Metadata, String> mtuMap;
    
    public MTUList() {
        mtuMap = new HashMap<>();
    }
    
    public void add(Metadata mtd, String url) {
        mtuMap.put(mtd, url);
    }
    
    public String get(Metadata mtd) {
        return mtuMap.get(mtd);
    }
    
    public void loadObj(String fileAddress) {
        try {
            FileInputStream fis = new FileInputStream(fileAddress);
            ObjectInputStream ois = new ObjectInputStream(fis);
            mtuMap = (HashMap<Metadata, String>) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
        }
    }
    
    public void saveObj(String fileAddress) {
        try {
            FileOutputStream fos = new FileOutputStream(fileAddress);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(mtuMap);
            oos.close();
            fos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    public ArrayList<String> searchUrls(int satellite, TimeStamp startDate, TimeStamp endDate, int station) {
        Set<Metadata> keys = mtuMap.keySet();
        ArrayList<String> urls = new ArrayList<>();
        
        for(Metadata i : keys) {
            if(i.getTimeStamp().compareTo(startDate) >= 0 && i.getTimeStamp().compareTo(endDate) <= 0) {
                String url = get(i);
                urls.add(url);
            }
        }
    }
    
}
