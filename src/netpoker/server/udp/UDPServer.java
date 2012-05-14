package netpoker.server.udp;

import netpoker.model.Player;
import netpoker.model.Table;
import netpoker.model.udp.AckManager;
import netpoker.model.udp.ListenerThread;
import netpoker.model.udp.Packet;
import netpoker.model.udp.SenderThread;
import netpoker.server.clients.RemoteClient;
import netpoker.server.clients.ServerClient;


public class UDPServer {
	private AckManager ackmanager;

	/** The starting cash per player. */
	private static final int STARTING_CASH = 100;

	/** The size of the big blind. */
	private static final int BIG_BLIND = 2;

	public UDPServer() {
		ackmanager = new AckManager();
	}

	public void startGame(ClientInfo[] players) {
		
		ServerClient serverClient = new ServerClient(players, ackmanager);
		
		// Start receiver thread
		ListenerThread thread = new ListenerThread(ackmanager, serverClient);
		thread.start();
		
		// Open firewall for incoming messages from all players
		for (ClientInfo player : players) {
			ackmanager.sendOnce("000##X##Opening firewall",
					player.getAddress(), player.getPortAddress());
		}

		Table table = new Table(BIG_BLIND);
		Player[] pokerPlayers = new Player[4];

		for (int i = 0; i < 4; i++) {
			pokerPlayers[i] = new Player(players[i].getNickName(),
					STARTING_CASH, new RemoteClient(players[i], ackmanager,
							serverClient));
			table.addPlayer(pokerPlayers[i]);
		}

		table.start();
	}

	public int getPort() {
		return ackmanager.getSocket().getLocalPort();
	}

	public SenderThread send(Packet message, ClientInfo player) {

		// start a SenderThread
		SenderThread sender = new SenderThread(ackmanager, message,
				player.getAddress(), player.getPortAddress());
		sender.start();

		return sender;
	}
}
