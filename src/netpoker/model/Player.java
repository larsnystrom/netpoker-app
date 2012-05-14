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

package netpoker.model;

import java.util.List;
import java.util.Set;

/**
 * A Texas Hold'em player.
 * 
 * The player's actions are delegated to a PlayerClient, which can be either
 * human-controlled or AI-controlled (bot).
 * 
 * @author Oscar Stigter
 */
public class Player {

	/** Name. */
	private final String name;

	/** Client application responsible for the actual behavior. */
	protected final Client client;

	/** Hand of cards. */
	private final Hand hand;

	/** Current amount of cash. */
	private int cash;

	/** Whether the player has his hole cards being dealt. */
	private boolean hasCards;

	/** Current bet. */
	private int bet;

	/** Number of bets and raises in the current betting round. */
	private int raises;

	/** Pot when all-in. */
	private int allInPot;

	/** Last action performed. */
	private Action action;

	/** Last action's bet increment. */
	private int betIncrement;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            The player's name.
	 * @param cash
	 *            The player's starting amount of cash.
	 * @param client
	 *            The client application.
	 */
	public Player(String name, int cash, Client client) {
		this.name = name;
		this.cash = cash;
		this.client = client;

		hand = new Hand();

		resetHand();
	}
	
	private Player(String name, int cash, Client client, Hand hand) {
		this.name = name;
		this.cash = cash;
		this.client = client;
		this.hand = hand;
	}

	/**
	 * Returns the client.
	 * 
	 * @return The client.
	 */
	public Client getClient() {
		return client;
	}

	/**
	 * Prepares the player for another hand.
	 */
	public void resetHand() {
		hand.removeAllCards();
		hasCards = false;
		resetBet();
	}

	/**
	 * Resets the player's bet.
	 */
	public void resetBet() {
		bet = 0;
		action = null;
		raises = 0;
		allInPot = 0;
		betIncrement = 0;
	}

	/**
	 * Sets the hole cards.
	 */
	public void setCards(List<Card> cards) {
		hand.removeAllCards();
		if (cards == null) {
			hasCards = false;
		} else {
			if (cards.size() == 2) {
				hand.addCards(cards);
				hasCards = true;
//				System.out.format("[CHEAT] %s's cards:\t%s\n", name, hand);
			} else {
				throw new IllegalArgumentException("Invalid number of cards");
			}
		}
	}

	/**
	 * Returns whether the player has his hole cards dealt.
	 * 
	 * @return True if the hole cards are dealt, otherwise false.
	 */
	public boolean hasCards() {
		return hasCards;
	}

	/**
	 * Returns the player's name.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the player's current amount of cash.
	 * 
	 * @return The amount of cash.
	 */
	public int getCash() {
		return cash;
	}

	/**
	 * Returns whether the player is broke.
	 * 
	 * @return True if the player is broke, otherwise false.
	 */
	public boolean isBroke() {
		return (cash == 0);
	}

	/**
	 * Returns the player's current bet.
	 * 
	 * @return The current bet.
	 */
	public int getBet() {
		return bet;
	}

	/**
	 * Returns the number of raises the player has done in this betting round.
	 * 
	 * @return The number of raises.
	 */
	public int getRaises() {
		return raises;
	}

	/**
	 * Returns the player's action.
	 * 
	 * @return the action
	 */
	public Action getAction() {
		return action;
	}

	/**
	 * Returns the bet increment of the last action.
	 * 
	 * @return The bet increment.
	 */
	public int getBetIncrement() {
		return betIncrement;
	}

	/**
	 * Returns the player's hand of cards.
	 * 
	 * @return The hand of cards.
	 */
	public Hand getHand() {
		return hand;
	}

	/**
	 * Returns the player's hole cards.
	 * 
	 * @return The hole cards.
	 */
	public Card[] getCards() {
		return hand.getCards();
	}

	/**
	 * Posts the small blind.
	 * 
	 * @param blind
	 *            The small blind.
	 */
	public void postSmallBlind(int blind) {
		action = Action.SMALL_BLIND;
		cash -= blind;
		bet += blind;
	}

	/**
	 * Posts the big blinds.
	 * 
	 * @param blind
	 *            The big blind.
	 */
	public void postBigBlind(int blind) {
		action = Action.BIG_BLIND;
		cash -= blind;
		bet += blind;
	}

	/**
	 * Returns the part of the pot this player has a stake in when all-in.
	 * 
	 * @return The all-in pot.
	 */
	public int getAllInPot() {
		return allInPot;
	}

	/**
	 * Sets the part of the pot this player has a stake in when all-in.
	 * 
	 * @param allInPot
	 *            The all-in pot.
	 */
	public void setInAllPot(int allInPot) {
		this.allInPot = allInPot;
	}

	/**
	 * Asks the player to act and returns his selected action.
	 * 
	 * Determining the player's action is handled by the client application.
	 * 
	 * @param actions
	 *            The allowed actions.
	 * @param minBet
	 *            The minimum bet.
	 * @param currentBet
	 *            The current bet.
	 * 
	 * @return The selected action.
	 */
	public Action act(Set<Action> actions, int minBet, int currentBet) {
		action = client.act(actions);
		switch (action) {
		case CHECK:
			break;
		case CALL:
			betIncrement = currentBet - bet;
			if (betIncrement > cash) {
				// TODO: All-in with partial Call.
				betIncrement = cash;
			}
			cash -= betIncrement;
			bet += betIncrement;
			break;
		case BET:
			betIncrement = minBet;
			if (betIncrement >= cash) {
				// TODO: All-in with partial Bet.
				betIncrement = cash;
			}
			cash -= betIncrement;
			bet += betIncrement;
			raises++;
			break;
		case RAISE:
			currentBet += minBet;
			betIncrement = currentBet - bet;
			cash -= betIncrement;
			bet += betIncrement;
			raises++;
			break;
		case FOLD:
			hand.removeAllCards();
			break;
		}
		return action;
	}

	/**
	 * Wins the pot.
	 * 
	 * @param pot
	 *            The pot.
	 */
	public void win(int pot) {
		cash += pot;
	}

	/**
	 * Returns a clone of this player with only public information.
	 * 
	 * @return The cloned player.
	 */
	public Player publicClone() {
		Player clone = new Player(name, cash, null);
		clone.hasCards = hasCards;
		clone.bet = bet;
		clone.raises = raises;
		clone.action = action;
		return clone;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}

	public String serialize(String delimiter) {
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		sb.append(delimiter);
		
		sb.append(cash);
		sb.append(delimiter);
		
		sb.append(hand.serialize("#3;"));
		sb.append(delimiter);
		
		sb.append(hasCards);
		sb.append(delimiter);
		
		sb.append(bet);
		sb.append(delimiter);
		
		sb.append(raises);
		sb.append(delimiter);
		
		sb.append(allInPot);
		sb.append(delimiter);
		
		if (null != action) {
			sb.append(action);
		}
		sb.append(delimiter);
		
		sb.append(betIncrement);
		
		return sb.toString();

	}

	public static Player deserialize(String player, String delimiter) {
		String[] info = player.split(delimiter);
		
		String playerName = info[0];
		int startingCash = Integer.parseInt(info[1]);
		Hand hand = Hand.deserialize(info[2], "#3;");
		
		Player des = new Player(playerName, startingCash, null, hand);
		des.hasCards = Boolean.parseBoolean(info[3]);
		des.bet = Integer.parseInt(info[4]);
		des.raises = Integer.parseInt(info[5]);
		des.allInPot = Integer.parseInt(info[6]);
		if (false == info[7].equals("")) {
			des.action = Action.fromString(info[7]);
		}
		des.betIncrement = Integer.parseInt(info[8]);
		
		return des;
	}

}
