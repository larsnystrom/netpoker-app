package Client;

import java.net.InetAddress;

public class TodoList {

	String s = null;

	public TodoList() {

	}

	public synchronized void write(String s, InetAddress address, int port) {
		while (this.s != null) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		notifyAll();
		this.s = s;
	}

	public synchronized String clear() {
		while (s == null) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		String temp = s;
		s = null;
		notifyAll();
		return temp;
	}
}
