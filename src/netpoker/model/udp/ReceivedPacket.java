package netpoker.model.udp;

import java.net.InetAddress;

public class ReceivedPacket {
	private Packet packet;
	private InetAddress remoteAddress;
	private int remotePort;

	public ReceivedPacket(Packet packet, InetAddress remoteAddress,
			int remotePort) {
		this.packet = packet;
		this.remoteAddress = remoteAddress;
		this.remotePort = remotePort;
	}

	public Packet getPacket() {
		return packet;
	}

	public InetAddress getAddress() {
		return remoteAddress;
	}

	public int getPort() {
		return remotePort;
	}

	public boolean equals(Object o) {
		if (false == (o instanceof ReceivedPacket)) {
			return false;
		}
		ReceivedPacket p = (ReceivedPacket) o;

		return (p.getPacket().equals(packet))
				&& (p.getAddress().equals(remoteAddress))
				&& (p.getPort() == remotePort);
	}
}
