package model.udpconnection;

import java.net.InetAddress;

import model.chat.ChatClient;

public class ChatReceiverThread extends Thread {

	AckManager ackManager;
	ChatClient client;
	private ProcessedPackets receivedPackets;
	private PacketsToProcess toProcess;

	public ChatReceiverThread(AckManager ackManager, ChatClient client,
			ProcessedPackets receivedPackets, PacketsToProcess toProcess) {
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

			int packetNbr = packet.getPacketNbr();

			ackManager
					.send(new AckPacket(packetNbr), clientAddress, clientPort);

			if (receivedPackets.add(recPacket)) {
				packet.runClient(client);
			}

		}

	}
}
