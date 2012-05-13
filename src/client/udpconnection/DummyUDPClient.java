package client.udpconnection;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashSet;

import client.texasholdem.clients.DummyClient;


import model.chat.ChatClient;
import model.udpconnection.AckManager;

public class DummyUDPClient {

	DatagramSocket socket;
	AckManager ackmanager;
	InetAddress hostAddress = null;
	Boolean myTurn = false;
	int port;
	int messageNbr = 0;
	HashSet<Integer> ackList = new HashSet<Integer>();
	private String thisPlayer;

	public DummyUDPClient(String thisPlayer, String[] playerNames, InetAddress hostAddress, int port) {
		this.hostAddress = hostAddress;
		this.port = port;
		this.thisPlayer = thisPlayer;
		// Create a DatagramSocket on any free port
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			System.out.println("DummyClient: Could not create socket!");
			System.exit(1);
		}

		ackmanager = new AckManager(socket);

		ChatClient client = new DummyClient();
		


		// Send "Join message"
		ackmanager.sendOnce("000##X##Opening firewall",
				hostAddress, port);

		// start a ClientReciever
		ClientListenerThread thread = new ClientListenerThread(ackmanager, client);
		thread.start();
		
	}

	public int getPortAddress() {
		return socket.getLocalPort();
	}
}
