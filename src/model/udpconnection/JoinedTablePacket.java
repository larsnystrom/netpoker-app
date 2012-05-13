package model.udpconnection;

import java.util.ArrayList;
import java.util.List;


import model.chat.ChatClient;
import model.texasholdem.Player;

public class JoinedTablePacket extends Packet {
	public static final String command = "JOIN";
	
	private int bigBlind;
	private List<Player> players;

	public JoinedTablePacket(int packetNbr, int bigBlind, List<Player> players) {
		super(packetNbr);
		this.bigBlind = bigBlind;
		this.players = players;
	}

	@Override
	protected String getCommand() {
		return command;
	}

	@Override
	protected String[] getParameters() {
		String[] params = new String[2];
		params[0] = new Integer(bigBlind).toString();
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < players.size(); i++) {
			sb.append(players.get(i).serialize("#2#")); //Player attribute delimiter
			if (players.size() != i + 1) {
				sb.append("#1#"); //Player delimiter
			}
		}
		params[1] = sb.toString();
		
		return params;
	}

	public static JoinedTablePacket parse(String message) {
		String[] parts = split(message);
		
		ArrayList<Player> players = new ArrayList<Player>();
		String[] rawPlayers = parts[3].split("#1#");
		for (int i = 0; i < rawPlayers.length; i++) {
			players.add(Player.deserialize(rawPlayers[i], "#2#"));
		}
		
		int bigBlind = Integer.parseInt(parts[2]);
		
		return new JoinedTablePacket(Integer.parseInt(parts[0]), bigBlind, players);
	}

	@Override
	public void runClient(ChatClient client) {
		client.joinedTable(bigBlind, players);
		
	}

}
