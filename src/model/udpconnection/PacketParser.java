package model.udpconnection;


public class PacketParser {
	public static Packet parse(String message) throws Exception {
		String command = Packet.getCommand(message);
		
		if (ActorRotatedPacket.command.equals(command)) {
			return ActorRotatedPacket.parse(message);
			
		} else if (ActPacket.command.equals(command)) {
			return ActPacket.parse(message);
			
		} else if (BoardUpdatedPacket.command.equals(command)) {
			return BoardUpdatedPacket.parse(message);
			
		} else if (HandStartedPacket.command.equals(command)) {
			return HandStartedPacket.parse(message);
			
		} else if (JoinedTablePacket.command.equals(command)) {
			return JoinedTablePacket.parse(message);
			
		} else if (MessageReceivedPacket.command.equals(command)) {
			return MessageReceivedPacket.parse(message);
			
		} else if (PlayerActedPacket.command.equals(command)) {
			return PlayerActedPacket.parse(message);
			
		} else if (PlayerUpdatedPacket.command.equals(command)) {
			return PlayerUpdatedPacket.parse(message);
			
		} else if (ChatMessagePacket.command.equals(command)) {
			return ChatMessagePacket.parse(message);
			
		} else {
			throw new Exception("Invalid command");
		}
	}
}
