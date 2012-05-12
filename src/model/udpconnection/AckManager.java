package model.udpconnection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashSet;

public class AckManager {
	
	private DatagramSocket socket;
	private HashSet<Integer> ackList = new HashSet<Integer>();
	private Integer messageNbr;
	
	public AckManager(DatagramSocket socket){
		this.socket = socket;
		messageNbr = 0;
	}
	
	public DatagramSocket getSocket(){
		return socket;
	}
	
	public synchronized int getMessageNbr(){
		messageNbr = messageNbr + 1;
		notifyAll();
		return messageNbr;
	}
	
	public synchronized void sendOnce(String message, InetAddress adress, int port) {
		// Create a DatagramPacket to send
		byte[] outdata = (message + "\n").getBytes();

		DatagramPacket dpsend = new DatagramPacket(outdata, outdata.length,
				adress, port);
		System.out.println("RecThrd sent: " + message);

		// Send the datagram
		try {
			socket.send(dpsend);
		} catch (IOException e) {
			System.out.println("An IOException occured: " + e);
		}
		notifyAll();
	}
	
	public synchronized boolean ackRecieved(int messageNbr){
		notifyAll();
		return ackList.contains(messageNbr);
	}
	
	public synchronized boolean addAck(int messageNbr){
		notifyAll();
		return ackList.add(messageNbr);
	}

}
