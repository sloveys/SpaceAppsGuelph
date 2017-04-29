/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spaceappsguelph;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import javax.imageio.ImageIO;

/**
 *
 * @author Loveys
 */
public class BmpAlgorithms {
    private static final int BLACK = 0xff000000;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // testing main
        Image testImage = null;
        try
        {
            testImage = ImageIO.read(new File("NUMBAHONE.bmp"));
        }
        catch (Exception e)
        {
            System.out.println("File not found!");
            System.exit(1);
        }
        BufferedImage testBuff = toBufferedImage(testImage);
        int [] temp = new int [2];
        temp[0] = 105;
        temp[1] = 130 ;
        calculateVolume(temp, testBuff);
        File outputfile = new File("image.jpg");
        System.out.println("central point: (" + temp[0] + ", " + temp[1] + ")");
        ImageIO.write(testBuff, "jpg", outputfile);
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
    
    private static int[] calculateVolume(int[] xy, BufferedImage image) {   //Ask Alejandro if you have questions, Sam knows the logic pretty well too
        int numPixels = 0;
        int minX = xy[0];
        int maxX = xy[0];
        int minY = xy[1];
        int maxY = xy[1];
        //queue of items to delete
        Queue<Point> queue = new LinkedList<>();
        if (image.getRGB(xy[0], xy[1]) == 0xff000000) {
            System.out.println("error: started with a black spot");
            return xy;
        }
        //initialize queue with 1 value
        queue.add(new Point(xy[0], xy[1]));
        //iterate until there are no more white pixels
        while (!queue.isEmpty()) {
            Point p = queue.remove();
            //if we find a white pixel
            if (image.getRGB(p.x, p.y) != 0xff000000) {
                if (p.x < minX) {
                    minX = p.x;
                }
                if (p.x > maxX) {
                    maxX = p.x;
                }
                if (p.y < minY) {
                    minY = p.y;
                }
                if (p.y > maxY) {
                    maxY = p.y;
                }
                image.setRGB(p.x, p.y, 0xff000000);
                numPixels++;
                //add adjacent pixels to check
                queue.add(new Point(p.x+1, p.y));
                queue.add(new Point(p.x-1, p.y));
                queue.add(new Point(p.x, p.y-1));
                queue.add(new Point(p.x, p.y+1));
            }
        }
        xy[0] = (minX + maxX) / 2;
        xy[1] = (minY + maxY) / 2;
        System.out.println("number of white pixels: " + numPixels);
        return xy;
    }
    
    //Sourced from
    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }
    
    static int attemptAdd(ArrayList<int[]> pixelQueue, int[] tempArray) {
        int dupeFlag = 0;
        for (int i = 0; i < pixelQueue.size(); i++)
        {
            if ((pixelQueue.get(i)[0] == tempArray[0])&&(pixelQueue.get(i)[1] == tempArray[1]))
            {   //Horrible coding but w/e its 4 am and i don't care
                dupeFlag = 1;
            } 
        }
        if (dupeFlag == 0)
            pixelQueue.add(tempArray);
        return dupeFlag;
    }
}
