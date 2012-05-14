package netpoker.model.udp;



public abstract class Packet {
	protected int packetNbr;
	
	private static final String delimiter = "##";
	
	public Packet(int packetNbr) {
		this.packetNbr = packetNbr;
	}
	
	protected abstract String getCommand();
	
	protected abstract String[] getParameters();
	
	public int getPacketNbr() {
		return packetNbr;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String[] params = getParameters();
		
		
		sb.append(packetNbr);
		sb.append(delimiter);
		sb.append(getCommand());
		for (int i = 0; i < params.length; i++) {
			sb.append(delimiter);
			sb.append(params[i]);
		}
		
		return sb.toString();
	}
	
	public static String getCommand(String message) {
		String[] parts = message.split(delimiter);
		if (parts.length > 1) {
			return parts[1];
		}
		
		return "no command found in packet";
	}
	
	protected static String[] split(String message) {
		return message.split(delimiter);
	}
	
	public abstract void runClient(ChatClient client);
	
	public Packet getResponsePacket(int packetNbr, ChatClient client) {
		return null;
	}
	
	public boolean equals(Object o) {
		if (false == (o instanceof Packet)) {
			return false;
		}
		
		Packet p = (Packet) o;
		
		return p.packetNbr == packetNbr;
	}
}

