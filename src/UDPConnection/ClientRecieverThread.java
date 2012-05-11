package UDPConnection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashSet;

public class ClientRecieverThread extends Thread{
		
		private DatagramSocket socket;
		HashSet<Integer> ackList;
		Boolean myTurn;
		
		public ClientRecieverThread(DatagramSocket socket, HashSet<Integer> ackList, Boolean myTurn){
			this.socket = socket;
			this.ackList = ackList;
			this.myTurn = myTurn;
		}
		
		public void run(){

			// Create a DatagramPacket to hold the incoming message
			byte[] data = new byte[65507];
			DatagramPacket dp = new DatagramPacket(data, data.length);

			while (true) {
				// Extract the message and print it on the console.
				try {
					System.out.println("ClRec: Waiting for message ...");
					socket.receive(dp);
				} catch (IOException e) {
					System.out.println("An IOException occured: " + e);
					System.exit(1);
				}
				
				String message = new String(dp.getData(),0,dp.getLength());
				System.out.println("ClRec, message recieved: " + message);

				String[] fullMessage = message.split("##");
				ackList.add(Integer.parseInt(fullMessage[0]));  				
				String command = fullMessage[1];
				
				if (command.equals("C")) { //Chatt
					System.out.println(fullMessage[2]);
				} else if (command.equals("T")) { //Turn to play
					myTurn = true;
				} else{
					System.out.println(fullMessage[1]);
				}
			}
		}
	}
