package client.udpconnection;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashSet;

import client.texasholdem.gui.Gui;

import model.udpconnection.AckManager;

public class UDPClient {

	DatagramSocket socket;
	AckManager ackmanager;
	InetAddress hostAddress = null;
	Boolean myTurn = false;
	int port;
	int messageNbr = 0;
	HashSet<Integer> ackList = new HashSet<Integer>();

	public UDPClient(String thisPlayer, String[] playerNames, InetAddress hostAddress, int port) {
		this.hostAddress = hostAddress;
		this.port = port;
		// Create a DatagramSocket on any free port
		try {
			socket = new DatagramSocket(); 
		} catch (SocketException e) {
			System.out.println("Client: Could not create socket!");
			System.exit(1);
		}

		ackmanager = new AckManager(socket);

		Gui gui = new Gui(thisPlayer, playerNames, ackmanager, hostAddress, port);
		

		// Send "Join message"
		ackmanager.sendOnce("000##X##Opening firewall",
				hostAddress, port);

		// start a ClientReciever
		ClientListenerThread thread = new ClientListenerThread(ackmanager, gui);
		thread.start();
	}


	public int getPortAddress() {
		return socket.getLocalPort();
	}

}
