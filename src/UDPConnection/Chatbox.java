package UDPConnection;

import java.net.InetAddress;


public class Chatbox {

	String s = null;
	Player[] players;

	public Chatbox(Player[] players) {
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
		
		Player p = new Player(inetAddress, port);
		this.s =  getPlayerName(p) + ": " + s;
		notifyAll();
	}
	
	private String getPlayerName(Player player){
		for(Player p: players){
			if(p.equals(player)){
				return p.getNickName();
			}
		}
		return "Noname";
	}

	public synchronized String clear() {
		System.out.println("In chatbox");
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
