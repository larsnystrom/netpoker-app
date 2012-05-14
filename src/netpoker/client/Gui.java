// This file is part of the 'texasholdem' project, an open source
// Texas Hold'em poker application written in Java.
//
// Copyright 2009 Oscar Stigter
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package netpoker.client;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;

import netpoker.model.Action;
import netpoker.model.Card;
import netpoker.model.Player;
import netpoker.model.udp.AckManager;
import netpoker.model.udp.ChatClient;
import netpoker.model.udp.ChatMessagePacket;
import netpoker.model.udp.SenderThread;

/**
 * The game's main frame.
 * 
 * This is the core class of the Swing UI client application.
 * 
 * @author Oscar Stigter
 */
public class Gui extends JFrame implements ChatClient {

	/** Serial version UID. */
	private static final long serialVersionUID = 1L;

	/** The GridBagConstraints. */
	private final GridBagConstraints gc;

	/** The board panel. */
	private final BoardPanel boardPanel;

	/** The control panel. */
	private final ControlPanel controlPanel;

	/** The player panels. */
	private final Map<String, PlayerPanel> playerPanels;

	/** The current dealer's name. */
	private String dealerName;

	/** The current actor's name. */
	private String actorName;

	private ChatPanel chat;

	private Action latestAction;

	private AckManager ackmanager;

	private InetAddress hostAddress;

	private int hostPort;

	private String thisPlayer;

	/**
	 * Constructor.
	 * 
	 * @throws Exception
	 */
	public Gui(String thisPlayer, String[] playerNames, AckManager ackmanager,
			InetAddress hostAddress, int hostPort) {
		super("Limit Texas Hold'em poker");

		this.ackmanager = ackmanager;
		this.hostAddress = hostAddress;
		this.hostPort = hostPort;
		this.thisPlayer = thisPlayer;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setBackground(UIConstants.TABLE_COLOR);
		setLayout(new GridBagLayout());

		gc = new GridBagConstraints();

		controlPanel = new ControlPanel();

		boardPanel = new BoardPanel(controlPanel);
		addComponent(boardPanel, 1, 1, 1, 1);

		playerPanels = new HashMap<String, PlayerPanel>();
		int i = 0;
		for (String player : playerNames) {
			PlayerPanel panel = new PlayerPanel();
			playerPanels.put(player, panel);
			switch (i++) {
			case 0:
				// North position.
				addComponent(panel, 1, 0, 1, 1);
				break;
			case 1:
				// East position.
				addComponent(panel, 2, 1, 1, 1);
				break;
			case 2:
				// South position.
				addComponent(panel, 1, 2, 1, 1);
				break;
			case 3:
				// West position.
				addComponent(panel, 0, 1, 1, 1);
				break;
			default:
				// Do nothing.
			}
		}

		JFrame chatFrame = new JFrame();
		chat = new ChatPanel(this);
		chatFrame.add(chat);
		chatFrame.setUndecorated(true);
		chatFrame.setAlwaysOnTop(true);
		chatFrame.setLocationByPlatform(true);
		chatFrame.setSize(400, 120);
		chatFrame.setVisible(true);

		// Show the frame.
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ozsoft.texasholdem.Client#joinedTable(int, java.util.List)
	 */
	public void joinedTable(int bigBlind, List<Player> players) {
		for (Player player : players) {
			PlayerPanel playerPanel = null;
			try {
				playerPanel = playerPanels.get(player.getName());
			} catch (NullPointerException e) {
				System.out.println(player.getName());
				e.printStackTrace();
				System.exit(1);
			}
			if (playerPanel != null) {
				playerPanel.update(player);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ozsoft.texasholdem.Client#messageReceived(java.lang.String)
	 */

	public void messageReceived(String message) {
		boardPanel.setMessage(message);
		boardPanel.waitForUserInput();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ozsoft.texasholdem.Client#handStarted(org.ozsoft.texasholdem.Player)
	 */
	public void handStarted(Player dealer) {
		setDealer(false);
		dealerName = dealer.getName();
		setDealer(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ozsoft.texasholdem.Client#actorRotated(org.ozsoft.texasholdem.Player)
	 */
	public void actorRotated(Player actor) {
		setActorInTurn(false);
		actorName = actor.getName();
		setActorInTurn(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ozsoft.texasholdem.Client#boardUpdated(java.util.List, int, int)
	 */
	public void boardUpdated(List<Card> cards, int bet, int pot) {
		boardPanel.update(cards, bet, pot);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ozsoft.texasholdem.Client#playerUpdated(org.ozsoft.texasholdem.Player
	 * )
	 */
	public void playerUpdated(Player player) {
		PlayerPanel playerPanel = playerPanels.get(player.getName());
		if (playerPanel != null) {
			playerPanel.update(player);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ozsoft.texasholdem.Client#playerActed(org.ozsoft.texasholdem.Player)
	 */
	public void playerActed(Player player) {
		String name = player.getName();
		PlayerPanel playerPanel = playerPanels.get(name);
		if (playerPanel != null) {
			playerPanel.update(player);
			Action action = player.getAction();
			if (action != null) {
				boardPanel.setMessage(String.format("%s %s.", name,
						action.getVerb()));
				// FIXME: Determine whether actor is the human player (not by
				// name).
				if (!name.equals(thisPlayer)) {
					boardPanel.waitForUserInput();
				}
			}
		} else {
			throw new IllegalStateException(String.format(
					"No PlayerPanel found for player '%s'", name));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ozsoft.texasholdem.Client#act(java.util.Set)
	 */
	public Action act(Set<Action> allowedActions) {
		boardPanel.setMessage("Please select an action.");
		Action action = controlPanel.getUserInput(allowedActions);
		controlPanel.waitForUserInput();
		return action;
	}

	/**
	 * Adds an UI component.
	 * 
	 * @param component
	 *            The component.
	 * @param x
	 *            The column.
	 * @param y
	 *            The row.
	 * @param width
	 *            The number of columns to span.
	 * @param height
	 *            The number of rows to span.
	 */
	private void addComponent(Component component, int x, int y, int width,
			int height) {
		gc.gridx = x;
		gc.gridy = y;
		gc.gridwidth = width;
		gc.gridheight = height;
		gc.weightx = 0.0;
		gc.weighty = 0.0;
		gc.anchor = GridBagConstraints.CENTER;
		gc.fill = GridBagConstraints.NONE;
		getContentPane().add(component, gc);
	}

	/**
	 * Sets whether the actor is in turn.
	 * 
	 * @param isInTurn
	 *            Whether the actor is in turn.
	 */
	private void setActorInTurn(boolean isInTurn) {
		if (actorName != null) {
			PlayerPanel playerPanel = playerPanels.get(actorName);
			if (playerPanel != null) {
				playerPanel.setInTurn(isInTurn);
			}
		}
	}

	/**
	 * Sets the dealer.
	 * 
	 * @param isDealer
	 *            Whether the player is the dealer.
	 */
	private void setDealer(boolean isDealer) {
		if (dealerName != null) {
			PlayerPanel playerPanel = playerPanels.get(dealerName);
			if (playerPanel != null) {
				playerPanel.setDealer(isDealer);
			}
		}
	}

	@Override
	public synchronized void chatMessage(String message, String username) {
		chat.updateChat(username + ": " + message);
	}

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

	@Override
	public synchronized void sendMessage(String message) {
		ChatMessagePacket packet = new ChatMessagePacket(
				ackmanager.getMessageNbr(), message, thisPlayer);
		ackmanager.send(packet, hostAddress, hostPort);
	}

	@Override
	public SenderThread getCurrentSender() {
		// TODO Auto-generated method stub
		return null;
	}

}
