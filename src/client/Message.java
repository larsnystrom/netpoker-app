package client;

import java.util.Vector;

public class Message {
	private int messageNbr;
	private String username;
	private String command;
	private Vector<String> params;
	private boolean isAcked;
	
	private static final String delimiter = "##";
	
	
	public static final String ACK = "ACK";
	public static final String CHAT = "CHAT";
	
	/**
	 * Client wants to join the game
	 */
	public static final String CLIENT_JOIN = "JOIN";
	/**
	 * Client folds.
	 */
	public static final String CLIENT_FOLD = "FOLD";
	/**
	 * Client show her cards.
	 */
	public static final String CLIENT_TELL = "TELL";
	/**
	 * Client raises bet.
	 * Raise with 0 equals Check
	 * Raise to current bet equals Call
	 * Raise over current bet equals Raise.
	 */
	public static final String CLIENT_RAISE = "RAISE";
	
	/**
	 * Server requests action from client
	 * Add possible actions as parameters.
	 */
	public static final String SERVER_REQUEST = "REQUEST";
	/**
	 * A player has won the game and this round is over.
	 */
	public static final String SERVER_ROUND_DONE = "ROUND_DONE";
	/**
	 * Server hands out cards.
	 */
	public static final String SERVER_GIVE_HAND = "GIVE_HAND";
	
	
	public Message(String receivedMesssage) throws Exception {
		String[] parsed = receivedMesssage.split(delimiter);
		if (parsed.length < 3) {
			throw new Exception("Invalid messsage: " + receivedMesssage);
		}
		
		this.messageNbr = Integer.parseInt(parsed[0]);
		this.username = parsed[1];
		this.command = parsed[2];
		this.params = new Vector<String>();
		for (int i = 3; i < parsed.length; i++) {
			this.params.add(parsed[i]);
		}
		isAcked = false;
	}
	
	public Message(int messageNbr, String username, String command, Vector<String> params) {
		this.messageNbr = messageNbr;
		this.username = username;
		this.command = command;
		this.params = params;
		this.isAcked = false;
	}
	
	public Message(int messageNbr, String username, String command) {
		this.messageNbr = messageNbr;
		this.username = username;
		this.command = command;
		this.params = new Vector<String>();
		this.isAcked = false;
	}

	public int getMessageNbr() {
		return messageNbr;
	}

	public String getUsername() {
		return username;
	}
	
	public String getCommand() {
		return command;
	}
	
	public Vector<String> getParams() {
		return params;
	}
	
	public synchronized boolean isAcked() {
		return isAcked;
	}
	
	public synchronized void ack() {
		isAcked = true;
	}
	
	public String toString() {
		String message = messageNbr + delimiter + username + delimiter + command + delimiter;
		for (int i = 0; i < params.size(); i++) {
			message += params.get(i) + delimiter;
		}
		message += "\n";
		return message;
	}
}
