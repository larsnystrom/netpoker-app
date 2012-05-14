package netpoker.model.udp;

import netpoker.model.Action;
import netpoker.model.Client;

public interface ChatClient extends Client {
	public void chatMessage(String message, String username);
	
	public void sendMessage(String message);
	
	public void actedSet(Action action);
	
	public Action actedGet();
	
}
