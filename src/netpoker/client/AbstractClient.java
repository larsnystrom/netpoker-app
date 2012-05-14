package netpoker.client;

import netpoker.model.Action;
import netpoker.model.udp.ChatClient;

public abstract class AbstractClient implements ChatClient {
	
	private Action latestAction;

	@Override
	public synchronized void actedSet(Action action) {
		latestAction = action;
		notifyAll();
	}

	@Override
	public synchronized Action actedGet() {
		while (null == latestAction) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Action temp = latestAction;
		latestAction = null;
		return temp;
	}
}
