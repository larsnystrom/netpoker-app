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

	@Override
	public boolean equals(Object o) {
		if (false == (o instanceof ReceivedPacket)) {
			return false;
		}
		return o.hashCode() == this.hashCode();
	}

	@Override
	public int hashCode() {
		return packet.getPacketNbr() * 3 + remotePort * 31
				+ remoteAddress.hashCode() * 17;
	}
}
