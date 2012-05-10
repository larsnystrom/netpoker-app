package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


public class ClientSendThread extends Thread {
	private Message message;
	private int messageNbr;
	private InetAddress address;
	private int port;
	
	public ClientSendThread(Message message, InetAddress address, int port) {
		this.message = message;
		this.address = address;
		this.port = port;
	}
	
	public void run() {
		// Create a DatagramPacket to send
		byte[] outdata = message.toString().getBytes();
		DatagramPacket dp = new DatagramPacket(outdata, outdata.length,
				address, port);
		
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket();
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		while (false == message.isAcked()) {

			// Send the datagram
			try {
				socket.send(dp);
			} catch (IOException e) {
				System.out.println("An IOException occured: " + e);
			}
			
			System.out.println("UDClient sent: " +message);
			try {
				Thread.sleep(5000L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		socket.close();
	}
}
