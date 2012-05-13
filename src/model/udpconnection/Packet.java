package model.udpconnection;

import model.chat.ChatClient;

public abstract class Packet {
	protected int packetNbr;
	
	private static final String delimiter = "##";
	
	public Packet(int packetNbr) {
		this.packetNbr = packetNbr;
	}
	
	protected abstract String getCommand();
	
	protected abstract String[] getParameters();
	
	public int getPacketNbr() {
		return packetNbr;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String[] params = getParameters();
		
		
		sb.append(packetNbr);
		sb.append(delimiter);
		sb.append(getCommand());
		for (int i = 0; i < params.length; i++) {
			sb.append(delimiter);
			sb.append(params[i]);
		}
		
		return sb.toString();
	}
	
	public static String getCommand(String message) throws Exception {
		String[] parts = message.split(delimiter);
		if (parts.length > 2) {
			return parts[1];
		}
		
		throw new Exception("Bad packet");
	}
	
	protected static String[] split(String message) {
		return message.split(delimiter);
	}
	
	public abstract void runClient(ChatClient client);
}

