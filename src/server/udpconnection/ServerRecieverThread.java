package server.udpconnection;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.HashSet;

import server.texasholdem.clients.ServerClient;


import model.udpconnection.AckManager;
import model.udpconnection.AckPacket;
import model.udpconnection.Packet;
import model.udpconnection.PacketParser;

public class ServerRecieverThread extends Thread {

	private Chatbox chatbox;
	private DatagramPacket dp;
	private AckManager ackmanager;
	HashSet<Integer> receivedPackets = new HashSet<Integer>();
	private ServerClient serverClient;

	public ServerRecieverThread(AckManager ackmanager, DatagramPacket dp,
			Chatbox chatbox) {
		this.chatbox = chatbox;
		this.ackmanager = ackmanager;
		this.dp = dp;
		this.serverClient = new ServerClient(chatbox);
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
		InetAddress clientAddress = dp.getAddress();
		int clientPort = dp.getPort();
		
		if (packet instanceof AckPacket) {
			ackmanager.addAck(packetNbr);
		} else {
			ackmanager.sendOnce(
					new AckPacket(packetNbr).toString(),
					clientAddress, clientPort);
		}
		
		serverClient.setClientAddress(clientAddress);
		serverClient.setClientPort(clientPort);
		
		if (false == receivedPackets.contains(packetNbr)) {
			receivedPackets.add(packetNbr);
			packet.runClient(serverClient);
		}
		
	}
}
