package client.texasholdem.clients;

import java.util.List;
import java.util.Set;

import model.chat.ChatClient;
import model.texasholdem.Action;
import model.texasholdem.Card;
import model.texasholdem.Player;
import model.udpconnection.SenderThread;

public class DummyClient implements ChatClient {
	
	private Action latestAction;

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
	public Action act(Set<Action> actions) {
        if (actions.contains(Action.CHECK)) {
            return Action.CHECK;
        } else {
            return Action.CALL;
        }
	}

	@Override
	public void chatMessage(String message) {
		// TODO Auto-generated method stub
		
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
