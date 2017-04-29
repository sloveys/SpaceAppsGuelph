/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spaceappsguelph;
import java.io.*;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;
import javax.imageio.*;
import java.net.URL;
/**
 *
 * @author Loveys
 */
public class BmpTransfer {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /*
        FileSeekableStream stream = new FileSeekableStream("sas");
        TIFFDecodeParam decodeParam = new TIFFDecodeParam();
        decodeParam.setDecodePaletteAsShorts(true);
        ParameterBlock params = new ParameterBlock();
        params.add(stream);
        RenderedOp image1 = JAI.create("tiff", params);
        BufferedImage img = image1.getAsBufferedImage();
        
        
        
        
        
        
        URL tif;
        try
        {
            tif = new URL("ftp://ftp.asc-csa.gc.ca/users/OpenData_DonneesOuvertes/pub/AlouetteData/500/Image0001.tif");
        }
        catch (Exception e)
        {
            System.out.println("boo!");
        }
        BufferedImage image = ;*/
    }
    
    /*public static String convert (URL inputFile)
    {
        BufferedImage tif = null;
        try
        {
            tif = ImageIO.read(inputFile);
        }
        catch(Exception e)
        {
            System.out.println("Image not found");
            return("FAILURE");
        }
        try
        {
            ImageIO.write(tif, "bmp", new File("temp.bmp"));
        }
        catch (Exception f)
        {
            System.out.println("ERROR WRITING TO FILE");
            return ("FAILURE");
        } 
        return ("temp.bmp");
    }*/
}
