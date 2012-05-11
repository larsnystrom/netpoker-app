package UDPConnection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;

import Client.Player;
import Client.TodoList;

public class UDPServer {

	int port = 30000;
	int threadName = 0 ;
	DatagramSocket socket;
	
	public UDPServer(Player[] players) {
		
		//port = players[0].getPortAddress();
		Chatbox chatbox = new Chatbox(players);
		TodoList todo = new TodoList();

		// Create a DatagramSocket
		try {
			socket = new DatagramSocket(30000);
		} catch (SocketException e) {
			System.out.println("Could not create socket!");
			System.exit(1);
		}
				
		//Open firewall for incoming messages from all players
		for(Player player : players){
			send("Opening firewall", player);
		}
		
		// Create a DatagramPacket to hold the incoming message
		//byte[] data = new byte[65507];
		byte[] data = "0123##J## Hej hopp!".getBytes();
		DatagramPacket dp = new DatagramPacket(data, data.length);
		
		//Start chatboxReader
		ChatReaderThread chatReader = new ChatReaderThread(chatbox, players, socket);
		chatReader.setName("chatReader");
		chatReader.start();

		while (true) {
			// Extract the message and start receiver thread
			try {
				System.out.println("UDPServer, Waiting for message ...");
				socket.receive(dp);
			
				// Start reciever thread
				RecieverThread thread = new RecieverThread(socket, dp, chatbox, todo);
				thread.setName("Thread " + threadName++);
				thread.start();
				System.out.println("Started " + thread.getName());
			
			} catch (IOException e) {
				System.out.println("An IOException occured: " + e);
				System.exit(1);
			}
		}
	}

	public void send(String message, Player player) {
		// Create a DatagramPacket to send
		byte[] data1 = (message + "\n").getBytes();

		System.out.println("UDPServer send: "+message);
		DatagramPacket dpsend = null;
		dpsend = new DatagramPacket(data1, data1.length, 
				player.getAddress(), player.getPortAddress());

		// Send the datagram
		try {
			socket.send(dpsend);
		} catch (IOException e) {
			System.out.println("An IOException occured: " + e);
		}
	}
}
