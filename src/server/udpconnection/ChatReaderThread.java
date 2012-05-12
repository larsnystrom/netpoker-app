package server.udpconnection;


import model.udpconnection.AckManager;
import model.udpconnection.SenderThread;


public class ChatReaderThread extends Thread {

	private Chatbox chatbox;

	private ClientInfo[] players;
	AckManager ackmanager;
	int messageNbr;

	public ChatReaderThread(Chatbox chatbox, ClientInfo[] players,
			AckManager ackmanager, int messageNbr) {
		this.chatbox = chatbox;
		this.players = players;
		this.ackmanager = ackmanager;
		this.messageNbr = messageNbr;
	}

	public void run() {
		while (true) {
			String s = chatbox.clear();
			for (ClientInfo player : players) {
				send("C##" + s, player);
			}
		}
	}

	public void send(String message, ClientInfo player) {
		messageNbr++;

		// Removes last character in string
		message = message.substring(0, message.length() - 1);

		// start a SenderThread
		SenderThread sender = new SenderThread(ackmanager, message,
				player.getAddress(), player.getPortAddress(), messageNbr);
		sender.start();
	}
}