package model.udpconnection;

import java.util.ArrayList;
import java.util.List;


import model.chat.ChatClient;
import model.texasholdem.Card;

public class BoardUpdatedPacket extends Packet {

	public static final String command = "BOARDUPDATE";
	
	private List<Card> cards;
	private int bet;
	private int pot;

	public BoardUpdatedPacket(int packetNbr, List<Card> cards, int bet, int pot) {
		super(packetNbr);
		this.cards = cards;
		this.bet = bet;
		this.pot = pot;
	}

	@Override
	protected String getCommand() {
		return command;
	}

	@Override
	protected String[] getParameters() {
		String[] params = new String[3];
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cards.size(); i++) {
			sb.append(cards.get(i).toString());
			if (cards.size() != i + 1) {
				sb.append("#1;"); //Card delimiter
			}
		}
		
		params[0] = sb.toString();
		params[1] = new Integer(bet).toString();
		params[2] = new Integer(pot).toString();
		
		return params;
	}

	public static BoardUpdatedPacket parse(String message) {
		String[] parts = split(message);
		
		ArrayList<Card> cards = new ArrayList<Card>();
		System.out.println(parts[2]);
		
		String[] rawCards = parts[2].split("#1;");
		for (int i = 0; i < rawCards.length; i++) {
			if (false == rawCards[i].equals("")) {
				cards.add(new Card(rawCards[i]));
			}
		}
		int bet = Integer.parseInt(parts[3]);
		int pot = Integer.parseInt(parts[4]);
		
		
		return new BoardUpdatedPacket(Integer.parseInt(parts[0]), cards, bet, pot);
	}

	@Override
	public void runClient(ChatClient client) {
		client.boardUpdated(cards, bet, pot);
	}
	
	

}
