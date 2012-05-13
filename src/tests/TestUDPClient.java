package tests;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;

import client.udpconnection.UDPClient;




public class TestUDPClient {

	
	static InetAddress hostAddress = null;
	static int port;
	static int messageNbr = 0;
	static HashSet<Integer> ackList = new HashSet<Integer>();

	public static void main(String[] args) {
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
		UDPClient client1 = new UDPClient(playerNames, hostAddress, 30000);
		
	}
}