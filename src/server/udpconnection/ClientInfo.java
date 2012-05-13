package server.udpconnection;

import java.net.InetAddress;

public class ClientInfo {
	
	private int port;
	private InetAddress address;
	private String nickName;

	public ClientInfo(String nickName, InetAddress address, int port){
		this.address = address;
		this.port = port;
		this.nickName = nickName;
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
	
	public Boolean equals(ClientInfo p){
		return (p.address.equals(address) && p.port == port);
	}
}
