package netpoker.model.udp;

import netpoker.model.Player;

public class PlayerActedPacket extends Packet {
	public static final String command = "PLAYERACTED";
	
	private Player player;

	public PlayerActedPacket(int packetNbr, Player player) {
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

	public static PlayerActedPacket parse(String message) {
		String[] parts = split(message);
		
		Player player = Player.deserialize(parts[2], "#2;");
		
		return new PlayerActedPacket(Integer.parseInt(parts[0]), player);
	}

	@Override
	public void runClient(ChatClient client) {
		client.playerActed(player);
		
	}

}
