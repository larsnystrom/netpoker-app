package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;

public class ClientRecieverThread extends Thread{
		
		private ClientPlayer clientPlayer;
		private int listenPort;
		private Hashtable<Integer, Message> ackList;
		
		public ClientRecieverThread(ClientPlayer clientPlayer, Hashtable<Integer, Message> ackList, int listenPort){
			this.clientPlayer = clientPlayer;
			this.ackList = ackList;
			this.listenPort = listenPort;
		}
		
		public void run() {
			
			DatagramSocket socket = null;
			try {
				socket = new DatagramSocket(listenPort);
			} catch (SocketException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

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
				
				Message message = null;
				try {
					message = new Message(new String(dp.getData(),0,dp.getLength()));
				} catch (Exception e) {
					System.out.println(e.getMessage());
					continue;
				}
				
				String command = message.getCommand();
				
				if (command.equals("ACK")) {
					Message origMsg = ackList.get(message.getMessageNbr());
					if (null != origMsg) {
						origMsg.ack();
					}
				} else if (command.equals("CHAT")) { //Chatt
					System.out.println(message.getParams().get(0));
					clientPlayer.ackAction(message.getMessageNbr());
				} else {
					System.out.println("Unknown command: " + message.toString());
				}
				
			}
		}
	}
