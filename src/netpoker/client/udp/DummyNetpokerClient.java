package netpoker.client.udp;

import java.net.InetAddress;

import netpoker.client.clients.DummyClient;
import netpoker.model.udp.AckManager;
import netpoker.model.udp.ChatClient;
import netpoker.model.udp.ListenerThread;




public class DummyNetpokerClient {
	
	private AckManager ackmanager;
	private String thisPlayer;

	public DummyNetpokerClient(String thisPlayer, String[] playerNames, InetAddress hostAddress, int port) {
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
