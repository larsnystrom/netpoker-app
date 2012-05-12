package client.texasholdem.clients;

import java.util.List;
import java.util.Set;

import model.texasholdem.Action;
import model.texasholdem.Card;
import model.texasholdem.Client;
import model.texasholdem.Player;

public class DummyClient implements Client {

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

}
