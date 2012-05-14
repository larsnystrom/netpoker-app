package netpoker.model.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class SenderThread extends Thread {
	
	DatagramSocket socket;
	AckManager ackmanager;
	InetAddress hostAddress = null;
	int port;
	Packet message;

	public SenderThread(AckManager ackmanager, Packet message, InetAddress hostAddress, int port) {
		this.ackmanager = ackmanager;
		this.message = message;
		this.hostAddress = hostAddress;
		this.port = port;
		this.socket = this.ackmanager.getSocket();
	}

	public void run() {
		// Create a DatagramPacket to send
		byte[] outdata = (message + "\n").getBytes();
		DatagramPacket dp = new DatagramPacket(outdata, outdata.length,
				hostAddress, port);

		int messageNbr = message.getPacketNbr();
		
		do {
			// Send the datagram
			try {
				socket.send(dp);
			} catch (IOException e) {
				System.out.println("An IOException occured: " + e);
			}
			
		} while (false == ackmanager.waitForAck(messageNbr));
	}
}
