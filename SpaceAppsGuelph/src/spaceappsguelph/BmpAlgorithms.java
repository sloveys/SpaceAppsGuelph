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
    private static final int MetadataStartLine = 9000;
    private static final int JUMPSPEED = 7;
    private static final int VALIDPIX = 2000;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // testing main
        Image testImage = null;
        try
        {
            testImage = ImageIO.read(new File("503Image0001.tif"));
        }
        catch (Exception e)
        {
            System.out.println("File not found!");
            System.exit(1);
        }
        BufferedImage testBuff = toBufferedImage(testImage);
        int [] temp = new int [2];
        temp[0] = 1280;
        temp[1] = 1155;
        calculateVolume(temp, testBuff);
        System.out.println("central point: (" + temp[0] + ", " + temp[1] + ")");
        ImageIO.write(testBuff, "tif", new File("image.tif"));
    }
    
    
    
    public MetadataToURL masterAlgo(String bmp, String url) {
        
        return null;
    }
    
    /**
     * 
     * @return 
     */
    private ArrayList<int[]> getWhiteSpaces(BufferedImage image) {
        int[] pos = new int[2];
        ArrayList<int[]> xys = new ArrayList<>(5);
        for (int i = 0; i < image.getWidth(); i+=JUMPSPEED) {
            for (int j=MetadataStartLine; j<image.getHeight(); j+=JUMPSPEED) {
                if (image.getRGB(i, j) != BLACK) {
                    pos[0] = i;
                    pos[1] = j;
                    pos = calculateVolume(pos, image);
                    if (pos[1] != 0) {
                        xys.add(pos);
                    }
                }
            }
        }
        return xys;
    }
    
    private static int[] calculateVolume(int[] xy, BufferedImage image) {   //Ask Alejandro if you have questions, Sam knows the logic pretty well too
        int numPixels = 0;
        int minX = xy[0];
        int maxX = xy[0];
        int minY = xy[1];
        int maxY = xy[1];
        //queue of items to delete
        Queue<Point> queue = new LinkedList<>();
        if (image.getRGB(xy[0], xy[1]) == BLACK) {
            System.out.println("error: started with a black spot");
            return xy;
        }
        //initialize queue with 1 value
        queue.add(new Point(xy[0], xy[1]));
        //iterate until there are no more white pixels
        while (!queue.isEmpty()) {
            Point p = queue.remove();
            //if we find a white pixel
            if (image.getRGB(p.x, p.y) != BLACK) {
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
                image.setRGB(p.x, p.y, BLACK);
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
        if (numPixels < VALIDPIX) {
            xy[0] = 0;
            xy[1] = 0;
        }
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
    
}
