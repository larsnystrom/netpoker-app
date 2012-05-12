package UDPConnection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPServer {

	int port = 30000;
	int threadName = 0;
	DatagramSocket socket;
	int messageNbr = 0;
	AckManager ackmanager;

	public UDPServer(Player[] players) {

		// port = players[0].getPortAddress();
		Chatbox chatbox = new Chatbox(players);

		// Create a DatagramSocket
		try {
			socket = new DatagramSocket(30000);
		} catch (SocketException e) {
			System.out.println("Could not create socket!");
			System.exit(1);
		}

		ackmanager = new AckManager(socket);
		
		// Start chatboxReader
		ChatReaderThread chatReader = new ChatReaderThread(chatbox, players,
				ackmanager, messageNbr);
		chatReader.setName("chatReader");
		chatReader.start();

		// Open firewall for incoming messages from all players
		for (Player player : players) {
			ackmanager.sendOnce("000##X##Opening firewall",
					player.getAddress(), player.getPortAddress());
		}

		// Create a DatagramPacket to hold the incoming message
		byte[] data = new byte[65507];
		DatagramPacket dp = new DatagramPacket(data, data.length);

		while (true) {
			// Extract the message and start receiver thread
			try {
				System.out.println("UDPServer, Waiting for message ...");
				socket.receive(dp);

				// Start receiver thread
				ServerRecieverThread thread = new ServerRecieverThread(
						ackmanager, dp, chatbox);
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
		messageNbr++;

		// start a SenderThread
		SenderThread sender = new SenderThread(ackmanager, message,
				player.getAddress(), player.getPortAddress(), messageNbr);
		sender.start();
	}
}
