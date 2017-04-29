/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spaceappsguelph;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Loveys
 */
public class BmpAlgorithms {
    
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
        temp[1] = 70 ;
        calculateVolume(temp, testBuff);
        File outputfile = new File("image.jpg");
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
        ArrayList<int[]> pixelQueue = new ArrayList<>(5);
        pixelQueue.add(xy);
        int l = 0, r = 0, t = 0, b = 0;
        int leftMost = xy[0], rightMost = xy[0], topMost = xy[1], bottomMost = xy[1];
        int currentPixel;
        while (!pixelQueue.isEmpty()) {
//            System.out.println(pixelQueue.size());
            int tempArray[] = new int[2];
            int dupeFlag = 0;
//            if (image.getRGB(pixelQueue.get(currentPixel)[0], pixelQueue.get(currentPixel)[1] - 1) > 0xff777777)
            currentPixel = 0; //keeping track of the pixel we are inspecting
            System.out.print(pixelQueue.size());
            //get next pixel from the list
            if (pixelQueue.get(currentPixel)[0] < leftMost)
                leftMost = pixelQueue.get(currentPixel)[0];
            //Check if current pixel is leftmost
            if (pixelQueue.get(currentPixel)[0] > rightMost)
                rightMost = pixelQueue.get(currentPixel)[0];
            //Check if current pixel is rightmost
            if (pixelQueue.get(currentPixel)[1] > bottomMost)
                bottomMost = pixelQueue.get(currentPixel)[1];
            //Check if current pixel is bottomMost
            if (pixelQueue.get(currentPixel)[1] < topMost)
                topMost = pixelQueue.get(currentPixel)[1];
            //Check if current pixel is topmost
            
            //increase volume counter
            if (image.getRGB(pixelQueue.get(currentPixel)[0], pixelQueue.get(currentPixel)[1] - 1) != 0xff000000)
            {   //if rgb of top pixel is valid then add to list
                tempArray[0] = pixelQueue.get(currentPixel)[0];
                tempArray[1] = pixelQueue.get(currentPixel)[1] - 1;
                t+=attemptAdd(pixelQueue, tempArray);
                
            }
            dupeFlag = 0;
            if (image.getRGB(pixelQueue.get(currentPixel)[0], pixelQueue.get(currentPixel)[1] + 1) != 0xff000000)
            {   //if rgb of bottom pixel is valid then add to list
                tempArray[0] = pixelQueue.get(currentPixel)[0];
                tempArray[1] = pixelQueue.get(currentPixel)[1] + 1;
                b+=attemptAdd(pixelQueue, tempArray);
            }
            dupeFlag = 0;
            if (image.getRGB(pixelQueue.get(currentPixel)[0] - 1, pixelQueue.get(currentPixel)[1]) != 0xff000000)
            {   //if rgb of left pixel is valid then add to list
                tempArray[0] = pixelQueue.get(currentPixel)[0] - 1;
                tempArray[1] = pixelQueue.get(currentPixel)[1];
                l+=attemptAdd(pixelQueue, tempArray);
            }
            dupeFlag = 0;
            if (image.getRGB(pixelQueue.get(currentPixel)[0] + 1, pixelQueue.get(currentPixel)[1]) != 0xff000000)
            {   //if rgb of right pixel is valid then add to list
                tempArray[0] = pixelQueue.get(currentPixel)[0] + 1;
                tempArray[1] = pixelQueue.get(currentPixel)[1];
                r+=attemptAdd(pixelQueue, tempArray);
            }
            image.setRGB(pixelQueue.get(currentPixel)[0], pixelQueue.get(currentPixel)[1], 0xff000000); //This marks the pixel as already looked at since it is less than grey (0xff777777) AKA it is black
            //I chose black because fuck it (In reality I can't choose another colour)
            pixelQueue.remove(currentPixel);
            System.out.println(pixelQueue.size());
        }
        
        //if volume is not valid or difference of xMost is not valid, return {0,0}
        //Still have to test this algorithm on things
        
        xy[0] = leftMost + (rightMost - leftMost)/2;
        xy[1] = topMost + (bottomMost - topMost)/2;
        System.out.println(t + ", " + b + ", " + l + ", " + r);
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
