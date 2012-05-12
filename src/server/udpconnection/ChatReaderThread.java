package server.udpconnection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;




public class ChatReaderThread extends Thread {

	private Chatbox chatbox;
	private ClientInfo[] players;
	DatagramSocket socket;

	public ChatReaderThread(Chatbox chatbox, ClientInfo[] players, DatagramSocket socket) {
		this.chatbox = chatbox;
		this.players = players;
		this.socket = socket;
	}

	public void run() {
		while (true) {
			String s = chatbox.clear();
			for (ClientInfo player : players) {
				send("C##" + s, player);
			}
		}
	}
	
	private void send(String message, ClientInfo player) {
		// Create a DatagramPacket to send
		byte[] data1 = (message + "\n").getBytes();

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