package server.udpconnection;

import java.net.DatagramPacket;

import model.udpconnection.AckManager;


public class ServerRecieverThread extends Thread {

	private Chatbox chatbox;
	private DatagramPacket dp;
	private AckManager ackmanager;

	public ServerRecieverThread(AckManager ackmanager, DatagramPacket dp,
			Chatbox chatbox) {
		this.chatbox = chatbox;
		this.ackmanager = ackmanager;
		this.dp = dp;
	}

	public void run() {

		String message = new String(dp.getData(), 0, dp.getLength());
		System.out.print(this.getName() + " Message recieved: " + message);
		String[] fullMessage = message.split("##");

		// Sends an ack to sender for received message
		if (!(fullMessage[1].equals("ack"))) {
			ackmanager.sendOnce(fullMessage[0] + "##ack##", dp.getAddress(),
					dp.getPort());
		}

		String command = fullMessage[1];

		if (ackmanager.addAck(Integer.parseInt(fullMessage[0]))) {

			if (command.equals("C")) { // Chatt
				chatbox.write(fullMessage[2], dp.getAddress(), dp.getPort());
				System.out.println("Writing to chatbox: " + fullMessage[2]);
			} else if (command.equals("J")) { // Join

			} else {
			}
		}
	}
}
