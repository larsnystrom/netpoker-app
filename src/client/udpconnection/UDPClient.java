package client.udpconnection;

import java.net.InetAddress;

import client.texasholdem.gui.Gui;

import model.udpconnection.AckManager;
import model.udpconnection.ListenerThread;

public class UDPClient {
	private AckManager ackmanager;

	public UDPClient(String thisPlayer, String[] playerNames, InetAddress hostAddress, int port) {

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
