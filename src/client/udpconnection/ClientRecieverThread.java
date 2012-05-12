package client.udpconnection;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.List;


import client.chat.gui.ChatPanel;
import client.texasholdem.gui.Gui;

import model.texasholdem.Player;
import model.udpconnection.AckManager;

public class ClientRecieverThread extends Thread {

	AckManager ackmanager;
	Boolean myTurn;
	ChatPanel chat;
	DatagramPacket dp;
	Gui gui;

	public ClientRecieverThread(DatagramPacket dp, AckManager ackmanager,
			Boolean myTurn, ChatPanel chat, Gui gui) {
		this.chat = chat;
		this.ackmanager = ackmanager;
		this.myTurn = myTurn;
		this.dp = dp;
		this.gui = gui;
	}

	public void run() {

		String message = new String(dp.getData(), 0, dp.getLength());
		System.out.print(this.getName() + " Message recieved: " + message);
		String[] fullMessage = message.split("##");

		// Sends an ack to sender for received message
		if (!fullMessage[1].equals("ack")) {
			ackmanager.sendOnce(fullMessage[0] + "##ack##", dp.getAddress(),
					dp.getPort());
		}
		
		String command = fullMessage[1];

		if (ackmanager.addAck(Integer.parseInt(fullMessage[0]))) {
			if (command.equals("C")) { // Chatt
				chat.updateChat(fullMessage[2]);
			} else if (command.equals("T")) { // Turn to play
				myTurn = true;
			} else if (command.equals("JOIN")) {
				// Läs in parametrar som skickats
				int bigBlind = Integer.parseInt(fullMessage[2]);
				List<Player> players = new ArrayList<Player>();
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
}
