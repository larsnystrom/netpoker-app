package server.texasholdem.clients;

import java.util.List;
import java.util.Set;

import server.udpconnection.ClientInfo;

import model.chat.ChatClient;
import model.texasholdem.Action;
import model.texasholdem.Card;
import model.texasholdem.Player;
import model.udpconnection.AckManager;
import model.udpconnection.ActPacket;
import model.udpconnection.ActorRotatedPacket;
import model.udpconnection.BoardUpdatedPacket;
import model.udpconnection.HandStartedPacket;
import model.udpconnection.JoinedTablePacket;
import model.udpconnection.MessageReceivedPacket;
import model.udpconnection.PlayerActedPacket;
import model.udpconnection.PlayerUpdatedPacket;
import model.udpconnection.SenderThread;

public class UdpClient implements ChatClient {
	private ClientInfo clientInfo;
	private AckManager ackManager;
	private ServerClient serverClient;

	private SenderThread currentSender;

	public UdpClient(ClientInfo clientInfo, AckManager ackManager,
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
		System.out.println("Sending Act packet...");
		try {
			currentSender.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// By now we should have received an ack
		System.out.println("Ack has been received, wait for Action...");
		Action action = serverClient.actedGet();
		System.out.println("Action has been received, returning.");
		return action;
	}

	public SenderThread getCurrentSender() {
		return currentSender;
	}

	@Override
	public void chatMessage(String message) {
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
