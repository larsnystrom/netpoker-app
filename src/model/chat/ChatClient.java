package model.chat;

import model.texasholdem.Action;
import model.texasholdem.Client;

public interface ChatClient extends Client {
	public void chatMessage(String message);
	
	public void sendMessage(String message);
	
	public void actedSet(Action action);
	
	public Action actedGet();
	
}
