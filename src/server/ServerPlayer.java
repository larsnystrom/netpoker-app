package server;

import java.net.InetAddress;

public class ServerPlayer {
	private String username;
	private InetAddress address;
	private int port;
	
	public ServerPlayer(String username, InetAddress address, int port) {
		this.username = username;
		this.address = address;
		this.port = port;
	}
	
	
}
