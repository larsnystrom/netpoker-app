package UDPConnection;

import model.texasholdem.Player;
import model.texasholdem.Table;
import model.texasholdem.bots.DummyBot;
import client.texasholdem.gui.Gui;

public class TestGui {
	public static void main(String[] args) {

		String[] playerNames = new String[4];
		playerNames[0] = "Lars";
		playerNames[1] = "Olle";
		playerNames[2] = "Bagge";
		playerNames[3] = "KG";
		
		Gui gui1 = new Gui(playerNames);
		Gui gui2 = new Gui(playerNames);
		Gui gui3 = new Gui(playerNames);
		Gui gui4 = new Gui(playerNames);

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
}
