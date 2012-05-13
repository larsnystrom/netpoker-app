package tests;

import server.udpconnection.ClientInfo;
import model.texasholdem.Client;
import model.texasholdem.Player;
import model.texasholdem.Table;
import model.texasholdem.bots.DummyBot;
import client.texasholdem.clients.DummyClient;
import client.texasholdem.gui.Gui;

public class TestGui {
	public static void main(String[] args) {

		
		TestGui test = new TestGui();
		
		

		String[] playerNames = new String[4];
		playerNames[0] = "Lars";
		playerNames[1] = "Olle";
		playerNames[2] = "Bagge";
		playerNames[3] = "KG";
		
		
		
		test.startClient(playerNames);
		
		
		Client gui2 = new DummyBot();
		Client gui3 = new DummyBot();
		Client gui4 = new DummyBot();

		Table table = new Table(2);

		Player player1 = new Player(playerNames[0], 50, gui1);
		Player player2 = new Player(playerNames[1], 50, gui2);
		Player player3 = new Player(playerNames[2], 50, gui3);
		Player player4 = new Player(playerNames[3], 50, gui4);

		table.addPlayer(player1);
		table.addPlayer(player2);
		table.addPlayer(player3);
		table.addPlayer(player4);

		table.start();
	}
	
	public TestGui() {
		
	}
	
	public void startClient(String[] playerNames) {
		
		Gui gui1 = new Gui(playerNames);
	}
	
	public void startServer() {
		ClientInfo client1 = new ClientInfo();
	}
}
