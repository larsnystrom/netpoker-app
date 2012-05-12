package server.udpconnection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class RecieverThread extends Thread{
		
		private Chatbox chatbox;
		private DatagramSocket socket;
		private DatagramPacket dp;
		
		public RecieverThread(DatagramSocket socket, DatagramPacket dp, Chatbox chatbox){
			this.socket = socket;
			this.chatbox = chatbox;
			this.dp = dp;
		}
		
		public void run(){

			while (true) {
				
				String message = new String(dp.getData(),0,dp.getLength());
																					System.out.println(this.getName() + " Message recieved: " + message);
				String[] fullMessage = message.split("##");
				send(fullMessage[0] + "##ack", dp.getAddress(), dp.getPort()); //h�nger sig f�rsta g�ngen!!
				String command = fullMessage[1];
								
				if (command.equals("C")) { //Chatt
					chatbox.write(fullMessage[2], dp.getAddress(), dp.getPort());
				} else if (command.equals("J")) { //Join
					// l�gg till spelaren i joinlistan
				} else {
					
				}
				
				// Create a new DatagramPacket to hold the incoming message
				byte[] data = new byte[65507];
				dp = new DatagramPacket(data, data.length);
				
				// Wait for the message.
				try {
					System.out.println("UDPServer RecTrd, Waiting for message ...");
					socket.receive(dp);
				} catch (IOException e) {
					System.out.println("An IOException occured: " + e);
					System.exit(1);
				}
			}
		}
		
		private void send(String message, InetAddress adress, int port) {
			// Create a DatagramPacket to send
			byte[] outdata = (message + "\n").getBytes();

			DatagramPacket dpsend = new DatagramPacket(outdata, outdata.length, 
					adress, port);
			System.out.println("RecThrd sent: "+ message);

			// Send the datagram
			try {
				socket.send(dpsend);
			} catch (IOException e) {
				System.out.println("An IOException occured: " + e);
			}
		}
	}
