package tests;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;

import netpoker.client.NetpokerClient;
import netpoker.client.dummy.DummyNetpokerClient;
import netpoker.server.ClientInfo;
import netpoker.server.UDPServer;



public class TestUDPClient {

	static InetAddress hostAddress = null;
	static int port;
	static int messageNbr = 0;
	static HashSet<Integer> ackList = new HashSet<Integer>();

	public static void main(String[] args) {
		// --------------------------
		// Step 0: Lobby should know
		// the server's and all clients
		// addresses
		// --------------------------

		InetAddress serverAddress = null;
		InetAddress client1Address = null;
		InetAddress client2Address = null;
		InetAddress client3Address = null;
		InetAddress client4Address = null;

		try {
			serverAddress = InetAddress.getByName("localhost");
			client1Address = InetAddress.getByName("localhost");
			client2Address = InetAddress.getByName("localhost");
			client3Address = InetAddress.getByName("localhost");
			client4Address = InetAddress.getByName("localhost");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// --------------------------
		// Step 0 END
		// --------------------------

		// --------------------------
		// Step 1: Setup server
		// --------------------------

		UDPServer server = new UDPServer();

		int serverPort = server.getPort(); 

		// --------------------------
		// Step 1 END
		// --------------------------
		
		// Send server's port and address, 
		// and all players' names to all clients
		
		String[] playerNames = new String[4];
		playerNames[0] = "Lars";
		playerNames[1] = "Olle";
		playerNames[2] = "Bagge";
		playerNames[3] = "K-G";

		// --------------------------
		// Step 2: Start clients
		// This can be done simultaneously
		// Each client needs to know
		// all players' names and their own name
		// --------------------------
		
		// INPUT: playerNames
		// INPUT: this player's name
		// INPUT: server's address
		// INPUT: server's port

		NetpokerClient client1 = new NetpokerClient(playerNames[0], playerNames, serverAddress,
				serverPort);
		
		NetpokerClient client2 = new NetpokerClient(playerNames[1], playerNames,
				serverAddress, serverPort);
		
		DummyNetpokerClient client3 = new DummyNetpokerClient(playerNames[2], playerNames,
				serverAddress, serverPort);
		
		DummyNetpokerClient client4 = new DummyNetpokerClient(playerNames[3], playerNames,
				serverAddress, serverPort);
		
		// OUTPUT: client port

		// --------------------------
		// Step 2 END
		// --------------------------

		// Send clients' port numbers to the server
		int[] playerPorts = new int[4];
		playerPorts[0] = client1.getPortAddress();
		playerPorts[1] = client2.getPortAddress();
		playerPorts[2] = client3.getPortAddress();
		playerPorts[3] = client4.getPortAddress();

		// --------------------------
		// Step 3: Tell the server client
		// ports and start the game
		// --------------------------
		
		// INPUT: Players' port numbers
		// INPUT: playerNames

		ClientInfo[] players = new ClientInfo[4];
		players[0] = new ClientInfo(playerNames[0], client1Address,
				playerPorts[0]);
		players[1] = new ClientInfo(playerNames[1], client2Address,
				playerPorts[1]);
		players[2] = new ClientInfo(playerNames[2], client3Address,
				playerPorts[2]);
		players[3] = new ClientInfo(playerNames[3], client4Address,
				playerPorts[3]);

		server.startGame(players);

		// --------------------------
		// Step 3 END
		// --------------------------

	}
}