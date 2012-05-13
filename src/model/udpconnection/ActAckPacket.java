package model.udpconnection;

import model.chat.ChatClient;
import model.texasholdem.Action;

public class ActAckPacket extends AckPacket {
	public static final String command = "ACKACTION";
	
	private Action action;
	
	public ActAckPacket(int packetNbr, Action action) {
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
	
	public static ActAckPacket parse(String message) {
		String[] parts = split(message);
		
		Action action = Action.valueOf(parts[2]);
		
		return new ActAckPacket(Integer.parseInt(parts[0]), action);
	}

	@Override
	public void runClient(ChatClient client) {
		client.actAckSet(action);
	}
	
	
}
