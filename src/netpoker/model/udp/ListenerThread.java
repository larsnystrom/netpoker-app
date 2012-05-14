package netpoker.model.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;




public class ListenerThread extends Thread {

	private DatagramSocket socket;
	private PacketProcessorThread chatThread;
	private PacketProcessorThread gameThread;
	private AckProcessorThread ackThread;
	private PacketsToProcess gameQueue;
	private PacketsToProcess chatQueue;
	private PacketsToProcess ackQueue;

	public ListenerThread(AckManager ackManager, ChatClient client) {
		
		this.socket = ackManager.getSocket();

		ProcessedPackets receivedPackets = new ProcessedPackets();
		
		this.gameQueue = new PacketsToProcess();
		this.chatQueue = new PacketsToProcess();
		this.ackQueue = new PacketsToProcess();
		
		this.gameThread = new PacketProcessorThread(ackManager, client,
				receivedPackets, gameQueue);
		
		this.chatThread = new PacketProcessorThread(ackManager, client,
				receivedPackets, chatQueue);
		
		this.ackThread = new AckProcessorThread(ackManager, ackQueue);
	}

	public void run() {
		this.gameThread.setName(this.getName() + "-game");
		this.chatThread.setName(this.getName() + "-chat");
		this.ackThread.setName(this.getName() + "-ack");
		ackThread.start();
		chatThread.start();
		gameThread.start();

		while (true) {
			// Create a DatagramPacket to hold the incoming message
			byte[] data = new byte[65507];
			DatagramPacket dp = new DatagramPacket(data, data.length);
			
			try {
				socket.receive(dp);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			String message = new String(dp.getData(), 0, dp.getLength()).trim();

			Packet packet = PacketParser.parse(message);

			if (null == packet) {
				System.out.println("Bad packet: " + message);
				continue;
			}

			if (packet instanceof AckPacket) {
				ackQueue.add(new ReceivedPacket(packet, dp.getAddress(), dp.getPort()));
			} else if (packet instanceof ChatMessagePacket) {
				chatQueue.add(new ReceivedPacket(packet, dp.getAddress(), dp
						.getPort()));
			} else {
				gameQueue.add(new ReceivedPacket(packet, dp.getAddress(), dp
						.getPort()));
			}
		}
	}
}
