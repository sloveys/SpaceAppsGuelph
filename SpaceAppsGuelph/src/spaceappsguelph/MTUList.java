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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Loveys
 */
public class MTUList {
    private HashMap<Metadata, String> mtuMap;
    
    public MTUList() {
        mtuMap = new HashMap<>(5);
    }
    
    public void add(Metadata mtu, String url) {
        mtuMap.put(mtu, url);
    }
    
    public String get(Metadata mtu) {
        return mtuMap.get(mtu);
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
    
}
