package client.udpconnection;

import java.net.DatagramPacket;
import java.util.HashSet;

import client.chat.gui.ChatPanel;
import client.texasholdem.gui.Gui;

import model.udpconnection.AckManager;
import model.udpconnection.AckPacket;
import model.udpconnection.Packet;
import model.udpconnection.PacketParser;

public class ClientRecieverThread extends Thread {

	AckManager ackmanager;
	Boolean myTurn;
	ChatPanel chat;
	DatagramPacket dp;
	Gui gui;
	HashSet<Integer> receivedPackets = new HashSet<Integer>();

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

		Packet packet;
		try {
			packet = PacketParser.parse(message);
		} catch (Exception e) {
			System.out.println("Bad packet: " + message);
			return;
		}
		
		int packetNbr = packet.getPacketNbr();
		
		if (packet instanceof AckPacket) {
			ackmanager.addAck(packetNbr);
		} else {
			ackmanager.sendOnce(
					new AckPacket(packetNbr).toString(),
					dp.getAddress(), dp.getPort());
		}
		
		if (false == receivedPackets.contains(packetNbr)) {
			receivedPackets.add(packetNbr);
			packet.runClient(gui);
		}
	}
}
