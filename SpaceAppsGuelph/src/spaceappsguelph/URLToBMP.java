/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spaceappsguelph;

import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;

/**
 *
 * @author Loveys
 */
public class URLToBMP {
    public static BufferedImage getBuff(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (Exception ex) {
            return null;
        }
    }
    public static BufferedImage getBuffOfTiff(String tif) {
        return null;
    }
}

