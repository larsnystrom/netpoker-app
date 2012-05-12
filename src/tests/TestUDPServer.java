package tests;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;


import server.udpconnection.ClientInfo;
import server.udpconnection.Player;
import server.udpconnection.UDPServer;


public class TestUDPServer {

	
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
		


		Player[] players = new Player[1]; 
		players[0]	= new Player(hostAddress, 35000);
//		players[1]	= new Player(hostAddress, 40000);
		players[0].setNickName("MrPoker");
//		players[1].setNickName("Kajan");
		
		UDPServer server = new UDPServer(players);
	}
}