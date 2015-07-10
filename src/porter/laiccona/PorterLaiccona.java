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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author Marc Pascual Terrón
 */
public class PorterLaiccona {
    //private static PLCCommandSender commandSender = new PLCCommandSender();

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
        ImageIcon img = new ImageIcon(new File("").getAbsolutePath()+"\\logo.gif");
        frame.setIconImage(img.getImage());
        frame.getContentPane().add(panel, BorderLayout.NORTH);

        OpenButton ob = new OpenButton("Obrir porta");
        ob.setPreferredSize(new Dimension(60, 60)); 
        ob.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                new PLCCommandSender().start();
                
            }
        });
        frame.getContentPane().add(ob, BorderLayout.PAGE_END);
 
	frame.pack();
	frame.setVisible(true);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
