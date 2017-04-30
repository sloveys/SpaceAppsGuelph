/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spaceappsguelph;

import java.awt.Graphics2D;
import java.awt.Image;
import static java.awt.JobAttributes.DestinationType.FILE;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;


/**
 *
 * @author Loveys
 */
public class BmpAlgorithms {
    private static final int BLACK = 0xff111111;
    private static final int MetadataStartLine = 8600;
    private static final int JUMPSPEED = 7;
    private static final int VALIDPIX = 5000;
    private static final int MARGINOFERROR = 75;
    private static final int ROWDIF = 250;
    private static final int COLDIF = 250;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // testing main
        masterAlgo("ftp://ftp.asc-csa.gc.ca/users/OpenData_DonneesOuvertes/pub/AlouetteData/500/Image0121.tif");
    }
    
    public static MetadataToURL masterAlgo(String url) {
        if (url.isEmpty())
            return null;
        BufferedImage image = URLToBMP.getBuff(url);
        System.out.println("URL Downloaded");
        if (image == null)
            return null;
        System.out.println("URL Valid");
        try {
            ImageIO.write(image, "tif", new File("ImageTesting.tif"));
        } catch (IOException ex) {
            Logger.getLogger(BmpAlgorithms.class.getName()).log(Level.SEVERE, null, ex);
        }
        ArrayList<int[]> xys = new ArrayList<>(5);
        getWhiteSpaces(image, xys);
        if (xys.size() < 4)
            return null;
        System.out.println("Data Generated: ");
        int[][][] dots = new int[4][13][2];
        for (int[][] i : dots) {
            for (int[] j : i) {
                for (int k : j) {
                    k = 0;
                }
            }
        }
        for (int[] i : xys) {
            System.out.println(i[0] + ", " + i[1]);
        }
        try {
            ImageIO.write(image, "tif", new File("ImageTesting2.tif"));
        } catch (IOException ex) {
            Logger.getLogger(BmpAlgorithms.class.getName()).log(Level.SEVERE, null, ex);
        }
        // construct where the dots are located in binary (decimal rep.) position
        // find top left 1;
        
        dots[0][0] = xys.get(0);
        for (int[] i : xys) {
            if (i[0] == 0 && i[1] == 0)
                continue;
            if (dots[0][0][0] < i[0] || dots[0][0][1] < i[1]) {
                dots[0][0] = i;
            }
        }
        xys.remove(dots[0][0]);
        // main loop to build points
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 3; j++) {
                if (i == 0 && j == 0) 
                    continue; // skip base condition
                for (int[] k : xys) {
                    if (comparePoints(dots[0][0], k, i, j)) {
                        dots[i][j] = k;
                        xys.remove(k);
                        break;
                    }
                }
            }
        }
        
        return null;
    }
    
    private static boolean comparePoints(int[] base, int[] target, int row, int col) {
        return (base[0] + (row * ROWDIF) + MARGINOFERROR > target[0] && base[0] + (row * ROWDIF) - MARGINOFERROR < target[0] 
                && base[1] + (col * COLDIF) + MARGINOFERROR > target[1] && base[1] + (row * ROWDIF) - MARGINOFERROR < target[1]);
    }
    
    /**
     * 
     * @return 
     */
    private static ArrayList<int[]> getWhiteSpaces(BufferedImage image, ArrayList<int[]> xys) {
        for (int i = 0; i < image.getWidth(); i+=JUMPSPEED) {
            for (int j=MetadataStartLine; j<image.getHeight(); j+=JUMPSPEED) {
                if (!isBlack(image.getRGB(i, j))) {
                    int[] pos = new int[2];
                    pos[0] = i;
                    pos[1] = j;
                    pos = calculateVolume(pos, image);
                    if (pos[1] > MetadataStartLine && pos[0] < image.getWidth() - 150) { // second condition is a patchwork solution to filter out light bleading from the bottom right corner
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
        if (isBlack(image.getRGB(xy[0], xy[1]))) {
            System.out.println("error: started with a black spot");
            return xy;
        }
        //initialize queue with 1 value
        queue.add(new Point(xy[0], xy[1]));
        //iterate until there are no more white pixels
        while (!queue.isEmpty()) {
            Point p = queue.remove();
            //if we find a white pixel
            if (p.x < 0 || p.x >= image.getWidth() || p.y < 0 || p.y >= image.getHeight())
                continue;
            if (!isBlack(image.getRGB(p.x, p.y))) {
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
        //System.out.println("number of white pixels: " + numPixels);
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
    
    private static boolean isBlack(int rgb) {
        return (rgb >= 0xff000000 && rgb < 0xff111111);
    }
}
