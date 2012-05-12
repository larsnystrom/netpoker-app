package client.udpconnection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.List;

import Client.ChatPanel;

import model.texasholdem.Player;

public class ClientRecieverThread extends Thread {

	private DatagramSocket socket;
	HashSet<Integer> ackList;
	Boolean myTurn;
	ChatPanel chat;
	DatagramPacket dp;

	public ClientRecieverThread(DatagramSocket socket, DatagramPacket dp,
			HashSet<Integer> ackList, Boolean myTurn, ChatPanel chat) {
		this.chat = chat;
		this.socket = socket;
		this.ackList = ackList;
		this.myTurn = myTurn;
		this.dp = dp;
	}

	public void run() {

		String message = new String(dp.getData(), 0, dp.getLength());
		System.out.print(this.getName() + " Message recieved: " + message);
		String[] fullMessage = message.split("##");

		// Sends an ack to sender for received message
		if(!fullMessage[1].equals("ack")){
			sendOnce(fullMessage[0] + "##ack##", dp.getAddress(), dp.getPort());
		}
		String command = fullMessage[1];

		if (ackList.add(Integer.parseInt(fullMessage[0]))) {
			if (command.equals("C")) { // Chatt
				chat.updateChat(fullMessage[2]);
			} else if (command.equals("T")) { // Turn to play
				myTurn = true;
			} else if (command.equals("JOIN") {
				// Läs in parametrar som skickats
				int bigBlind = Integer.parseInt(fullMessage[2]);
				List<Player> players = new List<Player>();
				StringBuilder sb = new StringBuilder();
				for (int i = 3; i < fullMessage.length; i++) {
					sb.append(fullMessage[i]);
				}
				String listParam = sb.toString();
				String[] playerParams = listParam.split("#1#");
				for (int i = 0; i < playerParams.length; i++) {
					players.add(Player.deserialize(playerParams[i], "#2#"));
				}
				
				//Anropa rätt funktion i Gui
				gui.joinedTable(bigBlind, players);
			}
		}
	}

	private void sendOnce(String message, InetAddress adress, int port) {
		// Create a DatagramPacket to send
		byte[] outdata = (message + "\n").getBytes();

		DatagramPacket dpsend = new DatagramPacket(outdata, outdata.length,
				adress, port);
		System.out.println("RecThrd sent: " + message);

		// Send the datagram
		try {
			socket.send(dpsend);
		} catch (IOException e) {
			System.out.println("An IOException occured: " + e);
		}
	}
}