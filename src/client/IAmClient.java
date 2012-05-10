package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;




public class IAmClient {

	DatagramSocket socket = null;
	InetAddress hostAddress = null;
	Boolean myTurn = false;
	String username;
	

	public IAmClient(String username, InetAddress remoteAddress, int remotePort, int listenPort) {
		// Create the ackList for this player
		Hashtable<Integer, Message> ackList = new Hashtable<Integer, Message>();
		// Create this player
		ClientPlayer clientPlayer = new ClientPlayer(username, remoteAddress, remotePort, ackList);
		
		// start a ClientReciever for this player
		ClientRecieverThread receiver = new ClientRecieverThread(clientPlayer, ackList, listenPort);
		receiver.start();

		// Send "Join message"
		clientPlayer.joinAction();
	}
}
