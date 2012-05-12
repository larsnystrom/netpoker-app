package client.udpconnection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashSet;

import client.texasholdem.gui.Gui;



public class UDPClient {

	DatagramSocket socket = null;
	InetAddress hostAddress = null;
	Boolean myTurn = false;
	int port;
	int messageNbr = 0;
	HashSet<Integer> ackList = new HashSet<Integer>();

	public UDPClient(String[] playerNames, InetAddress hostAddress, int port) {
		this.hostAddress = hostAddress;
		this.port = port;
		// Create a DatagramSocket on any free port
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			System.out.println("Could not create socket!");
			System.exit(1);
		}
		
		// start a ClientReciever
		ClientRecieverThread receiver = new ClientRecieverThread(socket, ackList, myTurn);
		receiver.start();
		
		Gui gui = new Gui(playerNames);

		// Send "Join message"
		send("J##  Trying to join");
	}
	
	public int getPortAddress() {
		return socket.getPort();
	}

	public void send(String message) {
		messageNbr++;

		// Create a DatagramPacket to send
		byte[] outdata = (messageNbr + "##" + message + "\n").getBytes();
		DatagramPacket dp = new DatagramPacket(outdata, outdata.length,
				hostAddress, port);

		while (!ackList.contains(messageNbr)) {

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
	}
}
