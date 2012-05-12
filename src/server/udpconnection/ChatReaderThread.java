package server.udpconnection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import model.udpconnection.AckManager;
import model.udpconnection.SenderThread;

public class ChatReaderThread extends Thread {

	private Chatbox chatbox;
	private ClientInfo[] players;
	AckManager ackmanager;

	public ChatReaderThread(Chatbox chatbox, ClientInfo[] players,
			AckManager ackmanager) {
		this.chatbox = chatbox;
		this.players = players;
		this.ackmanager = ackmanager;
	}

	public void run() {
		while (true) {
			String s = chatbox.clear();
			for (ClientInfo player : players) {
				send("C##" + s, player);
				System.out.println("Trying to send ");

			}
		}
	}

	public void send(String message, ClientInfo player) {

		// Removes last character in string
		message = message.substring(0, message.length() - 1);

		// Create a DatagramPacket to send
		int messageNbr = ackmanager.getMessageNbr();
		byte[] outdata = (messageNbr + "##" + message + "\n").getBytes();
		DatagramPacket dp = new DatagramPacket(outdata, outdata.length,
				player.getAddress(), player.getPortAddress());

		System.out.println("Trying to send");
		// Send the datagram
		try {
			DatagramSocket socket = new DatagramSocket();

			socket.send(dp);
		} catch (IOException e) {
			System.out.println("An IOException occured: " + e);
		}

		System.out.println("Sent: " + messageNbr + "##" + message);

		// start a SenderThread
		// SenderThread sender = new SenderThread(ackmanager, message,
		// player.getAddress(), player.getPortAddress());
		// sender.start();
	}
}