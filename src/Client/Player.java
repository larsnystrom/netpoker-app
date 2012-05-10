package Client;

import java.net.InetAddress;

public class Player {
	
	private int port;
	private InetAddress address;
	private String nickName;

	public Player(InetAddress address, int port){
		this.address = address;
		this.port = port;
	}
	
	public int getPortAddress() {
		return port;
	}
	
	public InetAddress getAddress() {
		return address;
	}
	
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public String getNickName() {
		return nickName;
	}
	
	public Boolean equals(Player p){
		return (p.address.equals(address) && p.port == port);
	}
}
