package netpoker.model.udp;

import java.util.HashSet;

public class ProcessedPackets {
	private HashSet<ReceivedPacket> receivedPackets;
	
	public ProcessedPackets() {
		receivedPackets = new HashSet<ReceivedPacket>();
	}
	
	public synchronized boolean add(ReceivedPacket packet) {
		return receivedPackets.add(packet);
	}
}
