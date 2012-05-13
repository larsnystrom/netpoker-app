package model.udpconnection;

import model.chat.ChatClient;

public class ChatMessagePacket extends Packet {
	
	public static final String command = "CHAT";
	
	private String message;

	public ChatMessagePacket(int packetNbr, String message) {
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
	
	public static ChatMessagePacket parse(String message) {
		String[] parts = split(message);
		
		return new ChatMessagePacket(Integer.parseInt(parts[0]), parts[2]);
	}

	@Override
	public void runClient(ChatClient client) {
		client.chatMessage(message);
		
	}

}
