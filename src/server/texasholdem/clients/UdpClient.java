package server.texasholdem.clients;

import java.util.List;
import java.util.Set;

import server.udpconnection.ClientInfo;
import server.udpconnection.UDPServer;

import model.texasholdem.Action;
import model.texasholdem.Card;
import model.texasholdem.Client;
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

public class UdpClient implements Client {
	private ClientInfo clientInfo;
	private UDPServer sender;
	private AckManager ackManager;
	private ServerClient serverClient;
	
	private SenderThread currentSender;

	public UdpClient(ClientInfo clientInfo, UDPServer sender,
			AckManager ackManager, ServerClient serverClient) {
		this.clientInfo = clientInfo;
		this.sender = sender;
		this.ackManager = ackManager;
		this.serverClient = serverClient;
	}

	@Override
	public void messageReceived(String message) {
		MessageReceivedPacket packet = new MessageReceivedPacket(
				ackManager.getMessageNbr(), message);
		currentSender = sender.send(packet.toString(), clientInfo);
	}

	@Override
	public void joinedTable(int bigBlind, List<Player> players) {
		JoinedTablePacket packet = new JoinedTablePacket(
				ackManager.getMessageNbr(), bigBlind, players);
		currentSender = sender.send(packet.toString(), clientInfo);

	}

	@Override
	public void handStarted(Player dealer) {
		HandStartedPacket packet = new HandStartedPacket(
				ackManager.getMessageNbr(), dealer);
		currentSender = sender.send(packet.toString(), clientInfo);
	}

	@Override
	public void actorRotated(Player actor) {
		ActorRotatedPacket packet = new ActorRotatedPacket(
				ackManager.getMessageNbr(), actor);
		currentSender = sender.send(packet.toString(), clientInfo);

	}

	@Override
	public void playerUpdated(Player player) {
		PlayerUpdatedPacket packet = new PlayerUpdatedPacket(
				ackManager.getMessageNbr(), player);
		currentSender = sender.send(packet.toString(), clientInfo);

	}

	@Override
	public void boardUpdated(List<Card> cards, int bet, int pot) {
		BoardUpdatedPacket packet = new BoardUpdatedPacket(
				ackManager.getMessageNbr(), cards, bet, pot);
		currentSender = sender.send(packet.toString(), clientInfo);

	}

	@Override
	public void playerActed(Player player) {
		PlayerActedPacket packet = new PlayerActedPacket(
				ackManager.getMessageNbr(), player);
		currentSender = sender.send(packet.toString(), clientInfo);
		
	}

	@Override
	public Action act(Set<Action> allowedActions) {
		ActPacket packet = new ActPacket(
				ackManager.getMessageNbr(), allowedActions);
		currentSender = sender.send(packet.toString(), clientInfo);
		
		try {
			currentSender.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// By now we should have received an ack
		return serverClient.actAckGet();
	}
	
	public SenderThread getCurrentSender() {
		return currentSender;
	}
}
