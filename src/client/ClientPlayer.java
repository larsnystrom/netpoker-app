package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Hashtable;
import java.util.Vector;

public class ClientPlayer {

	private int remotePort;
	private InetAddress remoteAddress;
	private String username;
	private Hashtable<Integer, Message> ackList;

	int messageNbr = 0;

	public ClientPlayer(String username, InetAddress remoteAddress,
			int remotePort, Hashtable<Integer, Message> ackList) {
		this.remoteAddress = remoteAddress;
		this.remotePort = remotePort;
		this.username = username;
		this.ackList = ackList;
	}

	public int getPortAddress() {
		return remotePort;
	}

	public InetAddress getAddress() {
		return remoteAddress;
	}

	public String getUsername() {
		return username;
	}

	public Boolean equals(ClientPlayer p) {
		return (p.remoteAddress.equals(remoteAddress) && p.remotePort == remotePort);
	}

	public void joinAction() {
		send(Message.CLIENT_JOIN);
	}

	public void chatAction(String message) {
		Vector<String> params = new Vector<String>();
		params.add(message);
		send(Message.CHAT, params);
	}
	
	public void foldAction() {
		send(Message.CLIENT_FOLD);
	}

	private void send(String command, Vector<String> params) {
		Message clientMessage = new Message(messageNbr, username, command,
				params);
		ackList.put(messageNbr, clientMessage);
		ClientSendThread sendThread = new ClientSendThread(clientMessage,
				remoteAddress, remotePort);
		sendThread.start();
		messageNbr++;
	}

	private void send(String command) {
		Message clientMessage = new Message(messageNbr, username, command);
		ackList.put(messageNbr, clientMessage);
		ClientSendThread sendThread = new ClientSendThread(clientMessage,
				remoteAddress, remotePort);
		sendThread.start();
		messageNbr++;
	}
	
	/**
	 * Acknowledge a received message
	 * This is a special case of sending a message,
	 * since we don't want to resend until an acknowledge for
	 * this message is received.
	 * 
	 * @param messageNbr
	 */
	public void ackAction(int messageNbr) {
		// Create a DatagramPacket to send
		Message message = new Message(messageNbr, username, Message.ACK);
		byte[] outdata = message.toString().getBytes();
		DatagramPacket dp = new DatagramPacket(outdata, outdata.length,
				remoteAddress, remotePort);

		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket();
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Send the datagram
		try {
			socket.send(dp);
		} catch (IOException e) {
			System.out.println("An IOException occured: " + e);
		}

		System.out.println("UDClient sent: " + message);
		try {
			Thread.sleep(5000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		socket.close();
	}
}
