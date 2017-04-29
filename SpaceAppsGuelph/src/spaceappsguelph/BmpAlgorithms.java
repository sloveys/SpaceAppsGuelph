/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spaceappsguelph;

import java.util.ArrayList;
import java.awt.image.BufferedImage;

/**
 *
 * @author Loveys
 */
public class BmpAlgorithms {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // testing main
    }
    
    private int metadataStartLine;
    
    /*
    public MetadataToUrl masterAlgo(String bmp, String url) {
        return null;
    } */
    
    
    
    private int getMetadataStartLine() {
        
        return 0;
    }
    
    private int[] getWhiteSpace(int[] xy) {
        
        return xy;
    }
    
    private int[] calculateVolume(int[] xy, BufferedImage image) {
        ArrayList<int[]> pixelQueue = new ArrayList<>(5);
        pixelQueue.add(xy);
        int leftMost = xy[0], rightMost = xy[0], topMost = xy[1], bottomMost = xy[1];
        while (!pixelQueue.isEmpty()) {
            //get next pixel from the list
            //remove said pixel from the end of the list
            //Check if current pixel is leftmost
            //Check if current pixel is rightmost
            //Check if current pixel is bottomMost
            //Check if current pixel is topmost
            
            //increase volume counter
            //if rgb of top pixel is valid then add to list
            //if rgb of bottom pixel is valid then add to list
            //if rgb of left pixel is valid then add to list
            //if rgb of right pixel is valid then add to list
            //recolour current pixel to red
        }
        
        //if volume is not valid or difference of xMost is not valid, return {0,0}
        
        xy[0] = leftMost + (rightMost - leftMost)/2;
        xy[1] = topMost + (bottomMost - topMost)/2;
        return xy;
    }
}
