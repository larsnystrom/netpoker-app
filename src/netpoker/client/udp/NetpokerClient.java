package netpoker.client.udp;

import java.net.InetAddress;

import netpoker.client.gui.Gui;
import netpoker.model.udp.AckManager;
import netpoker.model.udp.ListenerThread;



public class NetpokerClient {
	private AckManager ackmanager;

	public NetpokerClient(String thisPlayer, String[] playerNames, InetAddress hostAddress, int port) {

		ackmanager = new AckManager();

		Gui gui = new Gui(thisPlayer, playerNames, ackmanager, hostAddress, port);
		

		// Send "Join message"
		ackmanager.sendOnce("000##X##Opening firewall",
				hostAddress, port);

		// start a ClientReciever
		ListenerThread thread = new ListenerThread(ackmanager, gui);
		thread.start();
	}


	public int getPortAddress() {
		return ackmanager.getSocket().getLocalPort();
	}

}
