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

/**
 *
 * @author Loveys
 */
public class MTUList {
    private ArrayList<MetadataToURL> mtuArray;
    
    public MTUList() {
        mtuArray = new ArrayList<>(5);
    }
    
    public void add(MetadataToURL mtu) {
        mtuArray.add(mtu);
    }
    
    public MetadataToURL get(int i) {
        return mtuArray.get(i);
    }
    
    public void loadObj(String fileAddress) {
        try {
            FileInputStream fis = new FileInputStream(fileAddress);
            ObjectInputStream ois = new ObjectInputStream(fis);
            mtuArray = (ArrayList) ois.readObject();
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
            oos.writeObject(mtuArray);
            oos.close();
            fos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
}
