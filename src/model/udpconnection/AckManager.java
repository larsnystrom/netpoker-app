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

	public AckManager(DatagramSocket socket) {
		this.socket = socket;
		messageNbr = 0;
	}

	public synchronized DatagramSocket getSocket() {
		return socket;
	}

	public synchronized int getMessageNbr() {
		messageNbr = messageNbr + 1;
		return messageNbr;
	}

	public synchronized void sendOnce(String message, InetAddress adress,
			int port) {
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
	}

	public synchronized boolean waitForAck(int messageNbr) {
		if (false == ackList.contains(messageNbr)) {
			try {
				wait(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ackList.contains(messageNbr);
	}

	public synchronized boolean addAck(int messageNbr) {
		boolean added = ackList.add(messageNbr);
		notifyAll();
		return added;
	}

	public synchronized SenderThread send(Packet message,
			InetAddress recipientAddress, int recipientPort) {

		// start a SenderThread
		SenderThread sender = new SenderThread(this, message, recipientAddress,
				recipientPort);
		sender.setName(new Integer(this.getSocket().getLocalPort()).toString());
		sender.start();

		return sender;

	}

}
