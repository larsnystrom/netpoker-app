package tests;

import java.net.InetAddress;
import java.net.UnknownHostException;

import netpoker.model.udp.ChatMessagePacket;
import netpoker.model.udp.ProcessedPackets;
import netpoker.model.udp.ReceivedPacket;

/**
 * This program tests whether our class ProcessedPackets
 * can add two equal ReceivedPacket
 * 
 * @author lars
 * @see ProcessedPackets
 * @see ReceivedPacket
 */

public class TestPacketsToProcess {
	public static void main(String[] args) {
		ProcessedPackets processed = new ProcessedPackets();
		
		InetAddress a = null;
		int port = 5;
		try {
			a = InetAddress.getByName("localhost");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ChatMessagePacket p = new ChatMessagePacket(1, "hej", "lars");
		
		ReceivedPacket rp1 = new ReceivedPacket(p, a, port);
		ReceivedPacket rp2 = new ReceivedPacket(p, a, port);

		System.out.println(processed.add(rp1));
		System.out.println(processed.add(rp2));
		System.out.println(rp1.equals(rp2));
		
		
	}
}
