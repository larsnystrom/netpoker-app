package tests;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;

import server.udpconnection.ClientInfo;
import server.udpconnection.UDPServer;

import client.udpconnection.DummyUDPClient;
import client.udpconnection.UDPClient;

public class TestUDPClient {

	static InetAddress hostAddress = null;
	static int port;
	static int messageNbr = 0;
	static HashSet<Integer> ackList = new HashSet<Integer>();

	public static void main(String[] args) {
		// --------------------------
		// This part simulates the start of all clients
		// --------------------------
		if (args.length != 2) {
			System.out.println("Usage: java SendUDP <hostname> <port>");
			System.exit(1);
		}

		try {
			hostAddress = InetAddress.getByName(args[0]);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		String[] playerNames = new String[4];
		playerNames[0] = "Lars";
		playerNames[1] = "Olle";
		playerNames[2] = "Bagge";
		playerNames[3] = "KG";

		port = Integer.parseInt(args[1]);
		UDPClient client1 = new UDPClient("Lars", playerNames, hostAddress, port);
		DummyUDPClient client2 = new DummyUDPClient("Olle", playerNames, hostAddress, port);
		DummyUDPClient client3 = new DummyUDPClient("Bagge", playerNames, hostAddress, port);
		DummyUDPClient client4 = new DummyUDPClient("KG", playerNames, hostAddress, port);

		int[] playerPorts = new int[4];
		playerPorts[0] = client1.getPortAddress();
		playerPorts[1] = client2.getPortAddress();
		playerPorts[2] = client3.getPortAddress();
		playerPorts[3] = client4.getPortAddress();

		// --------------------------
		// End of client simulation
		// --------------------------
		
		
		for (int i = 0; i < 4; i++) {
			System.out.println("Client " + i + ": " + playerPorts[i]);
		}

		// --------------------------
		// Server start simulation
		// --------------------------
		
		
		
		ClientInfo[] players = new ClientInfo[4];
		players[0] = new ClientInfo(playerNames[0], hostAddress, playerPorts[0]);
		players[1] = new ClientInfo(playerNames[1], hostAddress, playerPorts[1]);
		players[2] = new ClientInfo(playerNames[2], hostAddress, playerPorts[2]);
		players[3] = new ClientInfo(playerNames[3], hostAddress, playerPorts[3]);
		
		UDPServer server = new UDPServer(players);
		

		// --------------------------
		// End server start simulation
		// --------------------------
		

	}
}