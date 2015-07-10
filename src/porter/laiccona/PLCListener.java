/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package porter.laiccona;

/**
 *
 * @author Marc Pascual Terrón
 */
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class PLCListener extends Thread {

	Socket listener;
	boolean running = false;
	DataInputStream in_data = null;
	byte nodeNumber = 0;
	
	public PLCListener(Socket listener_socket) {
		listener = listener_socket;
		running = true;
	}
	public void run(){
		while(running) {
			byte [] received_buffer = new byte [40];
			try{
				System.out.println("Listening...");
				in_data = new DataInputStream(listener.getInputStream());
				in_data.read(received_buffer);
			} catch (IOException e) {
				e.printStackTrace();
				running = false;
			}
			PrintData(received_buffer);
			if (received_buffer[15] == 0x03) {
				running = false;
				try {
					listener.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
        private void PrintData(byte [] buffer) {
            int j=0;
            StringBuilder sb = new StringBuilder();
	    for (byte b : buffer) {
	    	j++;
	        sb.append(String.format("%02X", b));
	    	if (j==4){
                    sb.append(" ");
                    j=0;
	    	}
	    }
	    System.out.println("Receive: "+sb.toString());
	}
	public void stopListening() {
		running = false;
	}
}
