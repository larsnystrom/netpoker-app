package netpoker.model.udp;

import java.net.InetAddress;

public class PacketProcessorThread extends Thread {

	private ChatClient client;
	private ProcessedPackets receivedPackets;
	private PacketsToProcess toProcess;
	private AckManager ackManager;

	public PacketProcessorThread(AckManager ackManager, ChatClient client,
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

//			System.out.println(getName() + " Received: " + packet.toString()
//					+ " from " + clientAddress.toString() + ":" + clientPort);

			int packetNbr = packet.getPacketNbr();

			ackManager.sendOnce(new AckPacket(packetNbr).toString(),
					clientAddress, clientPort);

			if (receivedPackets.add(recPacket)) {
//				System.out.println(getName() + " Runs: " + packet.toString()
//						+ " from " + clientAddress.toString() + ":"
//						+ clientPort);
				packet.runClient(client);
				Packet response = packet.getResponsePacket(
						ackManager.getMessageNbr(), client);
				if (null != response) {
					ackManager.send(response, clientAddress, clientPort);
				}
			}

		}

	}
}
