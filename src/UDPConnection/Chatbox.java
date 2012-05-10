package UDPConnection;

import java.net.InetAddress;

import client.ClientPlayer;


public class Chatbox {

	String s = null;
	ClientPlayer[] players;

	public Chatbox(ClientPlayer[] players) {
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
		
		ClientPlayer p = new ClientPlayer(inetAddress, port);
		
		notifyAll();
		this.s =  getPlayerName(p) + ": " + s;
	}
	
	private String getPlayerName(ClientPlayer player){
		for(ClientPlayer p: players){
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
