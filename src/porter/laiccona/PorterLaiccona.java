/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package porter.laiccona;

import javax.swing.JFrame;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.ds.ipcam.IpCamDeviceRegistry;
import com.github.sarxos.webcam.ds.ipcam.IpCamDriver;
import com.github.sarxos.webcam.ds.ipcam.IpCamMode;
import java.awt.BorderLayout;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marc Pascual Terrón
 */
public class PorterLaiccona {


    static {
	Webcam.setDriver(new IpCamDriver());
        try {
            IpCamDeviceRegistry.register("Camara porta", "http://192.168.0.8/mjpg/video.mjpg", IpCamMode.PUSH);
        } catch (MalformedURLException ex) {
            Logger.getLogger(PorterLaiccona.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String[] args) throws MalformedURLException {
        createFrame();

	
    }
    
    public static void createFrame(){
        WebcamPanel panel = new WebcamPanel(Webcam.getWebcams().get(0));
	panel.setFPSLimit(1);

	JFrame frame = new JFrame("Porter Laiccona");
        frame.getContentPane().add(panel, BorderLayout.NORTH);

        OpenButton ob = new OpenButton("Obrir porta");
        ob.setSize( 300, 200 );     
        
        frame.getContentPane().add(ob, BorderLayout.PAGE_END);
 
	frame.pack();
	frame.setVisible(true);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
