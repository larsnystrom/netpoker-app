package netpoker.server;

import java.util.List;
import java.util.Set;

import netpoker.model.Action;
import netpoker.model.Card;
import netpoker.model.Player;
import netpoker.model.udp.AckManager;
import netpoker.model.udp.ActPacket;
import netpoker.model.udp.ActorRotatedPacket;
import netpoker.model.udp.BoardUpdatedPacket;
import netpoker.model.udp.ChatClient;
import netpoker.model.udp.HandStartedPacket;
import netpoker.model.udp.JoinedTablePacket;
import netpoker.model.udp.MessageReceivedPacket;
import netpoker.model.udp.PlayerActedPacket;
import netpoker.model.udp.PlayerUpdatedPacket;
import netpoker.model.udp.SenderThread;



public class RemoteClient implements ChatClient {
	private ClientInfo clientInfo;
	private AckManager ackManager;
	private ServerClient serverClient;

	private SenderThread currentSender;

	public RemoteClient(ClientInfo clientInfo, AckManager ackManager,
			ServerClient serverClient) {
		this.clientInfo = clientInfo;
		this.ackManager = ackManager;
		this.serverClient = serverClient;
	}

	@Override
	public void messageReceived(String message) {
		MessageReceivedPacket packet = new MessageReceivedPacket(
				ackManager.getMessageNbr(), message);
		currentSender = ackManager.send(packet, clientInfo.getAddress(), clientInfo.getPortAddress());
	}

	@Override
	public void joinedTable(int bigBlind, List<Player> players) {
		JoinedTablePacket packet = new JoinedTablePacket(
				ackManager.getMessageNbr(), bigBlind, players);
		currentSender = ackManager.send(packet, clientInfo.getAddress(), clientInfo.getPortAddress());

	}

	@Override
	public void handStarted(Player dealer) {
		HandStartedPacket packet = new HandStartedPacket(
				ackManager.getMessageNbr(), dealer);
		currentSender = ackManager.send(packet, clientInfo.getAddress(), clientInfo.getPortAddress());
	}

	@Override
	public void actorRotated(Player actor) {
		ActorRotatedPacket packet = new ActorRotatedPacket(
				ackManager.getMessageNbr(), actor);
		currentSender = ackManager.send(packet, clientInfo.getAddress(), clientInfo.getPortAddress());

	}

	@Override
	public void playerUpdated(Player player) {
		PlayerUpdatedPacket packet = new PlayerUpdatedPacket(
				ackManager.getMessageNbr(), player);
		currentSender = ackManager.send(packet, clientInfo.getAddress(), clientInfo.getPortAddress());

	}

	@Override
	public void boardUpdated(List<Card> cards, int bet, int pot) {
		BoardUpdatedPacket packet = new BoardUpdatedPacket(
				ackManager.getMessageNbr(), cards, bet, pot);
		currentSender = ackManager.send(packet, clientInfo.getAddress(), clientInfo.getPortAddress());

	}

	@Override
	public void playerActed(Player player) {
		PlayerActedPacket packet = new PlayerActedPacket(
				ackManager.getMessageNbr(), player);
		currentSender = ackManager.send(packet, clientInfo.getAddress(), clientInfo.getPortAddress());

	}

	@Override
	public Action act(Set<Action> allowedActions) {
		ActPacket packet = new ActPacket(ackManager.getMessageNbr(),
				allowedActions);
		currentSender = ackManager.send(packet, clientInfo.getAddress(), clientInfo.getPortAddress());
		
		try {
			currentSender.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return serverClient.actedGet();
	}

	public SenderThread getCurrentSender() {
		return currentSender;
	}

	@Override
	public void chatMessage(String message, String username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendMessage(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actedSet(Action action) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Action actedGet() {
		// TODO Auto-generated method stub
		return null;
	}
}
