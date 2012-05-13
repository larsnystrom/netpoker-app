package model.udpconnection;

import model.chat.ChatClient;
import model.texasholdem.Player;

public class HandStartedPacket extends Packet {
	public static final String command = "NEWHAND";
	private Player player;
	
	public HandStartedPacket(int packetNbr, Player player) {
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
		params[0] = player.serialize("#2;"); //Player attribute delimiter
		
		return params;
	}

	public static HandStartedPacket parse(String message) {
		String[] parts = split(message);
		
		Player player = Player.deserialize(parts[2], "#2;");
		
		return new HandStartedPacket(Integer.parseInt(parts[0]), player);
	}

	@Override
	public void runClient(ChatClient client) {
		client.handStarted(player);
		
	}

}
