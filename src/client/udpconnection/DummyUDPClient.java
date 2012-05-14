package client.udpconnection;

import java.net.InetAddress;

import client.texasholdem.clients.DummyClient;


import model.chat.ChatClient;
import model.udpconnection.AckManager;
import model.udpconnection.ListenerThread;

public class DummyUDPClient {
	
	private AckManager ackmanager;
	private String thisPlayer;

	public DummyUDPClient(String thisPlayer, String[] playerNames, InetAddress hostAddress, int port) {
		this.thisPlayer = thisPlayer;
		
		ackmanager = new AckManager();

		ChatClient client = new DummyClient();

		// Send "Join message"
		ackmanager.sendOnce("000##X##Opening firewall",
				hostAddress, port);

		// start a ClientReciever
		ListenerThread thread = new ListenerThread(ackmanager, client);
		thread.start();
		
	}

	public int getPortAddress() {
		return ackmanager.getSocket().getLocalPort();
	}
}
