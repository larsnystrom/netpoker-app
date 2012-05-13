package model.udpconnection;

import model.chat.ChatClient;

public class AckPacket extends Packet {
	public static final String command = "ACK";

	public AckPacket(int packetNbr) {
		super(packetNbr);
	}

	@Override
	protected String getCommand() {
		return command;
	}

	@Override
	protected String[] getParameters() {
		return new String[0];
	}
	
	public static AckPacket parse(String message) {
		String[] parts = split(message);
		
		return new AckPacket(Integer.parseInt(parts[0]));
	}

	@Override
	public void runClient(ChatClient client) {
		
		
	}

}
