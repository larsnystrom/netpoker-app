package model.udpconnection;

import model.chat.ChatClient;

public class ChatMessagePacket extends Packet {
	
	public static final String command = "CHAT";
	
	private String message;
	private String username;

	public ChatMessagePacket(int packetNbr, String message, String username) {
		super(packetNbr);
		this.message = message;
		this.username = username;
	}

	@Override
	protected String getCommand() {
		return command;
	}

	@Override
	protected String[] getParameters() {
		String[] params = new String[2];
		
		params[0] = message;
		params[1] = username;
		
		return params;
	}
	
	public static ChatMessagePacket parse(String message) {
		String[] parts = split(message);
		
		return new ChatMessagePacket(Integer.parseInt(parts[0]), parts[2], parts[3]);
	}

	@Override
	public void runClient(ChatClient client) {
		client.chatMessage(message, username);
		
	}

}
