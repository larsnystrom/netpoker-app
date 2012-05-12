package model.udpconnection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class SenderThread extends Thread {
	
	DatagramSocket socket;
	AckManager ackmanager;
	InetAddress hostAddress = null;
	int port;
	int messageNbr;
	String message;

	public SenderThread(AckManager ackmanager, String message, InetAddress hostAddress, int port, int messageNbr) {
		this.hostAddress = hostAddress;
		this.message = message;
		this.socket = ackmanager.getSocket();
		this.ackmanager = ackmanager;
		this.port = port;
		this.messageNbr = messageNbr;
	}

	public void run() {
		// Create a DatagramPacket to send
		byte[] outdata = (messageNbr + "##" + message + "\n").getBytes();
		DatagramPacket dp = new DatagramPacket(outdata, outdata.length,
				hostAddress, port);

		while (!ackmanager.ackRecieved(messageNbr)){

			// Send the datagram
			try {
				socket.send(dp);
			} catch (IOException e) {
				System.out.println("An IOException occured: " + e);
			}

			System.out.println("Sent: " + messageNbr + "##" +message);
			
			try {
				Thread.sleep(5000L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
