package server.texasholdem.clients;

import java.net.InetAddress;
import java.util.List;
import java.util.Set;

import server.udpconnection.Chatbox;

import model.chat.ChatClient;
import model.texasholdem.Action;
import model.texasholdem.Card;
import model.texasholdem.Player;

public class ServerClient implements ChatClient {
	
	private Chatbox chatbox;
	private InetAddress address;
	private int port;
	
	private Action latestAction;
	
	public ServerClient(Chatbox chatbox) {
		this.chatbox = chatbox;
	}
	
	public void setClientAddress(InetAddress address) {
		this.address = address;
	}
	
	public void setClientPort(int port) {
		this.port = port;
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
	public void chatMessage(String message) {
//		chatbox.write(message, address, port);
		
	}

	@Override
	public synchronized void actAckSet(Action action) {
		latestAction = action;
		notifyAll();
	}

	@Override
	public synchronized Action actAckGet() {
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

}
