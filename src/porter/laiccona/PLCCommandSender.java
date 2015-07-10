/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package porter.laiccona;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author Marc Pascual Terrón
 */
public class PLCCommandSender extends Thread{
    static final String HOST = "192.168.0.11";
    static final int PORT = 9600;
    static Socket socket = null;
    static PLCListener listenerThread = null;
    static OutputStream out_stream;
    static DataOutput out_data;

    public PLCCommandSender() {
        try {
            socket = new Socket (HOST,PORT);
            System.out.println("Connected using IP: "+ socket.getLocalSocketAddress()+" Port: "+socket.getLocalPort());
	} catch (IOException e) {	
            e.printStackTrace();
	}
	listenerThread = new PLCListener (socket);
	listenerThread.start();

	try {
            out_data = new DataOutputStream(socket.getOutputStream());
	} catch (IOException e1) {	
            e1.printStackTrace();
	}
		
	Connect();
    }
    
    
    @Override
    public void run() {
	
/*
	try {
            Thread.sleep(500);
	} catch (InterruptedException e) {
            e.printStackTrace();
	}
*/		
	SendOpenDoorFinsCommand();
		
    }
    private static void printSendData(byte [] data) {
        int j=0;
        StringBuilder sb = new StringBuilder();
	for (byte b : data) {
            j++;
	    sb.append(String.format("%02X", b));
	    if (j==4){
                sb.append(" ");
                j=0;
	    }
	}
	System.out.println("Send: "+sb.toString());
    }
		
    private static void Connect() {
        byte[] fins_tcp_header = new byte[20]; // Structure must have the exact size to send!
        fins_tcp_header[0] = (byte) 'F'; // Header
        fins_tcp_header[1] = (byte) 'I';
        fins_tcp_header[2] = (byte) 'N';
        fins_tcp_header[3] = (byte) 'S';
        fins_tcp_header[4] = (byte) 0x00; // Length
        fins_tcp_header[5] = (byte) 0x00;
        fins_tcp_header[6] = (byte) 0x00;
        fins_tcp_header[7] = (byte) 0x0C;
        fins_tcp_header[8] = (byte) 0x00; // Command 
        fins_tcp_header[9] = (byte) 0x00;
        fins_tcp_header[10] = (byte) 0x00;
        fins_tcp_header[11] = (byte) 0x00;
        fins_tcp_header[12] = (byte) 0x00; // Error Code
        fins_tcp_header[13] = (byte) 0x00;
        fins_tcp_header[14] = (byte) 0x00;
        fins_tcp_header[15] = (byte) 0x00;
        fins_tcp_header[16] = (byte) 0x00; // Client Node Add
        fins_tcp_header[17] = (byte) 0x00;
        fins_tcp_header[18] = (byte) 0x00;
        fins_tcp_header[19] = (byte) 0x00; // Fixed to 2
        printSendData(fins_tcp_header);
		
        try{
            out_data.write(fins_tcp_header);
        } catch( Exception e ) {
            e.printStackTrace();
        }
    }
	
    private static void SendOpenDoorFinsCommand() {
        byte[] fins_tcp_header = new byte[16]; // Structure must have the exact size to send!
        byte[] fins_header = new byte[10]; // Structure must have the exact size to send!
        byte[] fins_cmnd = new byte[9]; // Structure must have the exact size to send!
        fins_tcp_header[0] = (byte) 'F'; // Header 
        fins_tcp_header[1] = (byte) 'I';
        fins_tcp_header[2] = (byte) 'N';
        fins_tcp_header[3] = (byte) 'S';
        fins_tcp_header[4] = (byte) 0x00; // Length 
        fins_tcp_header[5] = (byte) 0x00;
        fins_tcp_header[6] = (byte) 0x00;
        fins_tcp_header[7] = (byte) 0x1B; // Length of data from Command up to end of FINS frame 
        fins_tcp_header[8] = (byte) 0x00; // Command 
        fins_tcp_header[9] = (byte) 0x00;
        fins_tcp_header[10] =(byte)  0x00;
        fins_tcp_header[11] = (byte) 0x02;
        fins_tcp_header[12] = (byte) 0x00; // Error Code 
        fins_tcp_header[13] = (byte) 0x00;
        fins_tcp_header[14] = (byte) 0x00;
        fins_tcp_header[15] = (byte) 0x00;
        fins_header[0] = (byte) 0x80; //ICF Information Control Field bit 7 Bridges (allways 1) ohter 0.
        fins_header[1] = (byte) 0x00; //RSV Reserved 00 Always
        fins_header[2] = (byte) 0x03; //GCT Gateway Count Ver.2.0
        fins_header[3] = (byte) 0x00; //DNA Destination network address 00 
        fins_header[4] = (byte) 0x0B; //DA1 Destination node address Ethernet Node 01 CPU unit
        fins_header[5] = (byte) 0x00; //DA2 Destination unit address Ethernet 00 CPU unit
        fins_header[6] = (byte) 0x00; //SNA Source network address 00 
        fins_header[7] = (byte) 0xEF; //SA1 Source node address Fixed to 2.
        fins_header[8] = (byte) 0x00; //SA2 Source unit address 00 Node 00 PC
        fins_header[9] = (byte) 0x02; //SID Service ID. 00 to FF
        fins_cmnd[0] = (byte) 0x01; //MRC Command Code (01,01 read) (01,02 write)
        fins_cmnd[1] = (byte) 0x02; //SRC Command Code
        fins_cmnd[2] = (byte) 0x31; //Memory Type: 31(WR) 
        fins_cmnd[3] = (byte) 0x00; //Begin write address Bytes 0,1
        fins_cmnd[4] = (byte) 0x64; //Begin write address Bytes 2,3
        fins_cmnd[5] = (byte) 0x0A; //Begin write address Bytes 4,5
        fins_cmnd[6] = (byte) 0x00; //Data
        fins_cmnd[7] = (byte) 0x01; //Data Read 00-Nothing/01-D000/02-D000,001...
        fins_cmnd[8] = (byte) 0x01; //Data write
	
        printSendData(fins_tcp_header);
        printSendData(fins_header);
        printSendData(fins_cmnd);
		
        try {	
            out_data.write(fins_tcp_header);
            out_data.write(fins_header);
            out_data.write(fins_cmnd);		
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
