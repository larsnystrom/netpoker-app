package client.udpconnection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashSet;

import client.chat.gui.ChatPanel;
import javax.swing.JFrame;

import model.udpconnection.AckManager;
import model.udpconnection.SenderThread;



public class UDPClient {

	DatagramSocket socket;
	AckManager ackmanager;
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
			socket = new DatagramSocket(35000); //ta bort portnummret sedan!
		} catch (SocketException e) {
			System.out.println("Could not create socket!");
			System.exit(1);
		}
	
		ackmanager = new AckManager(socket);

		//endast f�r testning av chatten - skall tas bort
		//------------------------------------------------
		JFrame frame = new JFrame();
		ChatPanel chat = new ChatPanel(this);
		frame.add(chat);
		frame.setSize(400, 200);
		frame.setVisible(true);
		//------------------------------------------------
		
		// Send "Join message"
		send("J## Trying to join");
		
		// Create a DatagramPacket to hold the incoming message
		byte[] data = new byte[65507];
		DatagramPacket dp = new DatagramPacket(data, data.length);
		
		while (true) {
			// Extract the message and start receiver thread
			try {
				System.out.println("UDPClient, Waiting for message ...");
				socket.receive(dp);
				
				// start a ClientReciever
				ClientRecieverThread receiver = new ClientRecieverThread(dp, ackmanager, myTurn, chat, null);
				receiver.start();

			} catch (IOException e) {
				System.out.println("An IOException occured: " + e);
				System.exit(1);
			}
		}
	}
	
	public int getPortAddress() {
		return socket.getPort();
	}

	public void send(String message) {
		messageNbr++;

		// start a SenderThread
		SenderThread sender = new SenderThread(ackmanager, message, hostAddress, messageNbr);
		sender.start();
		
	}
}
