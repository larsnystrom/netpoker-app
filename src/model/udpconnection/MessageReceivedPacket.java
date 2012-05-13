package model.udpconnection;

import model.chat.ChatClient;

public class MessageReceivedPacket extends Packet {
	public static final String command = "MESSAGE";
	private String message;
	
	public MessageReceivedPacket(int packetNbr, String message) {
		super(packetNbr);
		this.message = message;
	}

	@Override
	protected String getCommand() {
		return command;
	}

	@Override
	protected String[] getParameters() {
		String[] params = new String[1];
		params[0] = message;
		return params;
	}
	
	public static MessageReceivedPacket parse(String message) {
		String[] parts = split(message);
		
		return new MessageReceivedPacket(Integer.parseInt(parts[0]), parts[2]);
	}

	@Override
	public void runClient(ChatClient client) {
		client.messageReceived(message);
	}
	
	
}
