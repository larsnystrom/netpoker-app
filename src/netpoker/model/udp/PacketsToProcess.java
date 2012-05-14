package netpoker.model.udp;

import java.util.LinkedList;


public class PacketsToProcess {
	private LinkedList<ReceivedPacket> packets;
	
	public PacketsToProcess() {
		packets = new LinkedList<ReceivedPacket>();
	}
	
	public synchronized void add(ReceivedPacket packet) {
		packets.addLast(packet);
		notifyAll();
	}
	
	public synchronized ReceivedPacket poll() {
		while (packets.size() == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return packets.poll();
	}
}
