package server.udpconnection;

import java.net.DatagramSocket;
import java.net.SocketException;

import server.texasholdem.clients.ServerClient;
import server.texasholdem.clients.UdpClient;

import model.texasholdem.Player;
import model.texasholdem.Table;
import model.udpconnection.AckManager;
import model.udpconnection.Packet;
import model.udpconnection.SenderThread;

public class UDPServer {

	int port = 30000;
	int threadName = 0;
	DatagramSocket socket;
	AckManager ackmanager;

	/** The starting cash per player. */
	private static final int STARTING_CASH = 100;

	/** The size of the big blind. */
	private static final int BIG_BLIND = 2;

	public UDPServer(ClientInfo[] players) {

		// Create a DatagramSocket
		try {
			socket = new DatagramSocket(30000);
		} catch (SocketException e) {
			System.out.println("Server: Could not create socket!");
			System.exit(1);
		}

		ackmanager = new AckManager(socket);
		
		ServerClient serverClient = new ServerClient(players, ackmanager);
		// Start receiver thread
		
		ServerListenerThread thread = new ServerListenerThread(ackmanager, serverClient);
		thread.start();		

		// Open firewall for incoming messages from all players
		for (ClientInfo player : players) {
			ackmanager.sendOnce("000##X##Opening firewall",
					player.getAddress(), player.getPortAddress());
		}
		
		Table table = new Table(BIG_BLIND);
		Player[] pokerPlayers = new Player[4];
		
		for (int i = 0; i < 4; i++) {
			pokerPlayers[i] = new Player(players[i].getNickName(), STARTING_CASH, new UdpClient(players[i], ackmanager, serverClient));
			table.addPlayer(pokerPlayers[i]);
		}
		
		table.start();
		
	}

	public SenderThread send(Packet message, ClientInfo player) {

		// start a SenderThread
		SenderThread sender = new SenderThread(ackmanager, message,
				player.getAddress(), player.getPortAddress());
		sender.start();

		return sender;
	}
}
