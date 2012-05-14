package model.udpconnection;

public class AckProcessorThread extends Thread {
	AckManager ackManager;
	private PacketsToProcess toProcess;

	public AckProcessorThread(AckManager ackManager, PacketsToProcess toProcess) {
		this.ackManager = ackManager;
		this.toProcess = toProcess;
	}
	
	public void run() {
		while (true) {
			ReceivedPacket recPacket = toProcess.poll();

			Packet packet = recPacket.getPacket();

			int packetNbr = packet.getPacketNbr();
			
			ackManager.addAck(packetNbr);
		}
	}
}
