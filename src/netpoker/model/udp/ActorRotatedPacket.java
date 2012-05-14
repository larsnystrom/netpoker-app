package netpoker.model.udp;

import netpoker.model.Player;

public class ActorRotatedPacket extends Packet {
	public static final String command = "ROTATED";
	private Player player;
	
	public ActorRotatedPacket(int packetNbr, Player player) {
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
		params[0] = player.serialize("#2;"); //Player param delimiter
		
		return params;
	}

	public static ActorRotatedPacket parse(String message) {
		String[] parts = split(message);
		Player player = Player.deserialize(parts[2], "#2;");
		return new ActorRotatedPacket(Integer.parseInt(parts[0]), player);
	}

	@Override
	public void runClient(ChatClient client) {
		client.actorRotated(player);
	}

}
