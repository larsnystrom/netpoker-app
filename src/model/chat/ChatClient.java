package model.chat;

import model.texasholdem.Action;
import model.texasholdem.Client;

public interface ChatClient extends Client {
	public void chatMessage(String message);
	
	public void actAckSet(Action action);
	
	public Action actAckGet();
}
