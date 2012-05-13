package model.udpconnection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


import model.chat.ChatClient;
import model.texasholdem.Action;

public class ActPacket extends Packet {
	public static final String command = "ACT";
	
	private Set<Action> allowedActions;

	public ActPacket(int packetNbr, Set<Action> allowedActions) {
		super(packetNbr);
		this.allowedActions = allowedActions;
	}

	@Override
	protected String getCommand() {
		return command;
	}

	@Override
	protected String[] getParameters() {
		String[] params = new String[1];
		
		StringBuilder sb = new StringBuilder();
		ArrayList<Action> actions = new ArrayList<Action>(allowedActions);
		for (int i = 0; i < actions.size(); i++) {
			sb.append(actions.get(i).toString());
			if (actions.size() != i + 1) {
				sb.append("#1;"); //Action delimiter
			}
		}
		params[0] = sb.toString();
		
		return params;
	}

	public static ActPacket parse(String message) {
		String[] parts = split(message);
		Set<Action> allowedActions = new HashSet<Action>();
		
		String[] actions = parts[2].split("#1;");
		for (int i = 0; i < actions.length; i++) {
			allowedActions.add(Action.fromString(actions[i]));
		}
		
		return new ActPacket(Integer.parseInt(parts[0]), allowedActions);
	}

	@Override
	public void runClient(ChatClient client) {
		Action action = client.act(allowedActions);
		client.actedSet(action);
	}
	
	@Override
	public Packet getResponsePacket(int PacketNbr, ChatClient client) {
		return new ActedPacket(packetNbr, client.actedGet());
	}

}
