package UDPConnection;

import java.net.DatagramPacket;

public class ClientRecieverThread extends Thread {

	AckManager ackmanager;
	Boolean myTurn;
	ChatPanel chat;
	DatagramPacket dp;

	public ClientRecieverThread(DatagramPacket dp, AckManager ackmanager,
			Boolean myTurn, ChatPanel chat) {
		this.chat = chat;
		this.ackmanager = ackmanager;
		this.myTurn = myTurn;
		this.dp = dp;
	}

	public void run() {

		String message = new String(dp.getData(), 0, dp.getLength());
		System.out.print(this.getName() + " Message recieved: " + message);
		String[] fullMessage = message.split("##");

		// Sends an ack to sender for received message
		if (!fullMessage[1].equals("ack")) {
			ackmanager.sendOnce(fullMessage[0] + "##ack##", dp.getAddress(),
					dp.getPort());
		}
		String command = fullMessage[1];

		if (ackmanager.addAck(Integer.parseInt(fullMessage[0]))) {
			if (command.equals("C")) { // Chatt
				chat.updateChat(fullMessage[2]);
			} else if (command.equals("T")) { // Turn to play
				myTurn = true;
			} else {
			}
		}
	}
}
