package server.udpconnection;

import java.net.InetAddress;



public class Chatbox {

	String s = null;
	ClientInfo[] players;

	public Chatbox(ClientInfo[] players) {
		this.players = players;
	}

	public synchronized void write(String s, InetAddress inetAddress, int port) {
		while (this.s != null) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		ClientInfo p = new ClientInfo(inetAddress, port);
		
		notifyAll();
		this.s =  getPlayerName(p) + ": " + s;
	}
	
	private String getPlayerName(ClientInfo player){
		for(ClientInfo p: players){
			if(p.equals(player)){
				return p.getNickName();
			}
		}
		return "Noname";
	}

	public synchronized String clear() {
		while (s == null) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		String temp = s;
		s = null;
		notifyAll();
		return temp;
	}
}
