package model.udpconnection;

import java.net.InetAddress;

import model.chat.ChatClient;

public class PacketProcessorThread extends Thread {

	private ChatClient client;
	private ProcessedPackets receivedPackets;
	private PacketsToProcess toProcess;
	private AckManager ackManager;
	
	public PacketProcessorThread(AckManager ackManager,
			ChatClient client, ProcessedPackets receivedPackets, PacketsToProcess toProcess) {
		
		this.ackManager = ackManager;
		this.client = client;
		this.receivedPackets = receivedPackets;
		this.toProcess = toProcess;
	}

	public void run() {
		
		while (true) {
			ReceivedPacket recPacket = toProcess.poll();
			
			Packet packet = recPacket.getPacket();
			InetAddress clientAddress = recPacket.getAddress();
			int clientPort = recPacket.getPort();
			
			System.out.println("Received: " + packet.toString());
			
			int packetNbr = packet.getPacketNbr();
			
			ackManager.sendOnce(new AckPacket(packetNbr).toString(),
					clientAddress, clientPort);
			
			if (receivedPackets.add(recPacket)) {
				packet.runClient(client);
				Packet response = packet.getResponsePacket(ackManager.getMessageNbr(), client);
				if (null != response) {
					ackManager.send(response, clientAddress, clientPort);
				}
			}
			
		}

	}
}
