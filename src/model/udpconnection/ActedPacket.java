package model.udpconnection;

import model.chat.ChatClient;
import model.texasholdem.Action;

public class ActedPacket extends Packet {
	public static final String command = "ACTED";
	
	private Action action;
	
	public ActedPacket(int packetNbr, Action action) {
		super(packetNbr);
		this.action = action;
	}

	@Override
	protected String getCommand() {
		return command;
	}

	@Override
	protected String[] getParameters() {
		String[] params = new String[1];
		params[0] = action.toString();
		// TODO Auto-generated method stub
		return params;
	}
	
	public static ActedPacket parse(String message) {
		String[] parts = split(message);
		
		Action action = Action.fromString(parts[2]);
		
		return new ActedPacket(Integer.parseInt(parts[0]), action);
	}

	@Override
	public void runClient(ChatClient client) {
		System.out.println("Action has been received!");
		client.actedSet(action);
	}
	
	
}
