package server.texasholdem.clients;

import model.texasholdem.Client;

public interface RemoteClient extends Client {
	
	public boolean lastAcked();
	
	public void clearLast();
}
