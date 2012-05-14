package netpoker.server;

import java.util.List;
import java.util.Set;

import netpoker.model.Action;
import netpoker.model.Card;
import netpoker.model.Player;
import netpoker.model.udp.AckManager;
import netpoker.model.udp.ChatClient;
import netpoker.model.udp.ChatMessagePacket;
import netpoker.model.udp.SenderThread;



public class ServerClient implements ChatClient {

	private ClientInfo[] clients;
	private AckManager ackManager;

	private Action latestAction;

	public ServerClient(ClientInfo[] clients, AckManager ackManager) {
		this.clients = clients;
		this.ackManager = ackManager;
	}

	@Override
	public void messageReceived(String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void joinedTable(int bigBlind, List<Player> players) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handStarted(Player dealer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actorRotated(Player actor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void playerUpdated(Player player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void boardUpdated(List<Card> cards, int bet, int pot) {
		// TODO Auto-generated method stub

	}

	@Override
	public void playerActed(Player player) {
		// TODO Auto-generated method stub

	}

	@Override
	public Action act(Set<Action> allowedActions) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void chatMessage(String message, String username) {
		SenderThread[] senders = new SenderThread[clients.length];
		
		for (int i = 0; i < clients.length; i++) {
			ChatMessagePacket packet = new ChatMessagePacket(
					ackManager.getMessageNbr(), message, username);
			senders[i] = ackManager.send(packet, clients[i].getAddress(),
					clients[i].getPortAddress());
		}
		
		for (int i = 0; i < senders.length; i++) {
			try {
				senders[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public synchronized void actedSet(Action action) {
		latestAction = action;
		notifyAll();
	}

	@Override
	public synchronized Action actedGet() {
		while (null == latestAction) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Action temp = latestAction;
		latestAction = null;
		return temp;
	}

	@Override
	public void sendMessage(String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public SenderThread getCurrentSender() {
		// TODO Auto-generated method stub
		return null;
	}

}
