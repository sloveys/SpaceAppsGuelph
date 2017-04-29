/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spaceappsguelph;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Loveys
 */
public class SpaceAppsGuelph {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        AlouetteGUI alouetteGUI;
        try {
            alouetteGUI = new AlouetteGUI();
            alouetteGUI.setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(SpaceAppsGuelph.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
