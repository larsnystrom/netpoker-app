package model.udpconnection;

import model.chat.ChatClient;
import model.texasholdem.Player;

public class PlayerUpdatedPacket extends Packet {
	public static final String command = "PLAYERUPDATE";

	private Player player;
	
	public PlayerUpdatedPacket(int packetNbr, Player player) {
		super(packetNbr);
		this.player = player;
	}

	@Override
	protected String getCommand() {
		return command;
	}

	@Override
	protected String[] getParameters() {
		String[] params = new String[1];
		params[0] = player.serialize("#2#"); //Player attribute delimiter
		
		return params;
	}

	public static PlayerUpdatedPacket parse(String message) {
		String[] parts = message.split("#2#");
		
		Player player = Player.deserialize(parts[2], "#2#");
		return new PlayerUpdatedPacket(Integer.parseInt(parts[0]), player);
	}

	@Override
	public void runClient(ChatClient client) {
		client.playerUpdated(player);
	}

}
