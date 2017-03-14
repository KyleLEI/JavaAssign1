package world;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

import warriors.Cheer;
import warriors.Death;
import warriors.Warrior;

/**
 * The world class handles the core game logic, including moving warriors and
 * combat. It keeps the following information:
 * <ol>
 * <li>the world clock, a {@link Clock} object</li>
 * <li>HP and attack of warrior types in the order of dragon, ninja, iceman,
 * lion and wolf, two {@code array} of 5 {@code int}</li>
 * <li>container for cities, an {@code array}</li>
 * <li>headquarters of the two team, an {@code array} of
 * {@link Headquarter}</li>
 * </ol>
 *
 * @author Kyle Lei
 * @version 1.0.0
 */

public class World {
	private Clock clock;
	private int T;
	private int[] HP;
	private int[] attack;
	private City[] cities;
	private Headquarter[] hq;
	private WarriorType type;
	private int redHQOccupierCount = 0;
	private int blueHQOccupierCount = 0;
	private boolean end = false;
	private LinkedList<MoveMessage> moves;

	World(int inLifeElement, int numberOfCities, int endTime) {
		clock = new Clock();
		cities = new City[numberOfCities];
		for (int i = 0; i < numberOfCities; ++i)
			cities[i] = new City();
		hq = new Headquarter[2];
		hq[0] = new Headquarter(inLifeElement, Team.red);
		hq[1] = new Headquarter(inLifeElement, Team.blue);
		T = endTime;
		moves = new LinkedList<MoveMessage>();
	}

	public void setHP(int dragon, int ninja, int iceman, int lion, int wolf) {
		HP = new int[] { dragon, ninja, iceman, lion, wolf };
	}

	public void setAttack(int dragon, int ninja, int iceman, int lion, int wolf) {
		attack = new int[] { dragon, ninja, iceman, lion, wolf };
		type = new WarriorType();
		type.initialize(HP, attack);
		hq[0].initialize(type);
		hq[1].initialize(type);
	}

	/**
	 * Spawn and announce warrior according to specified sequence from two HQs.
	 */
	private void spawn() {
		Warrior redNew = hq[0].spawnWarrior();
		if (redNew != null) {
			redNew.location = 0;// red HQ
			hq[0].warriorInHQ.add(redNew);
			System.out.println(clock + " " + redNew + " born");
		}
		Warrior blueNew = hq[1].spawnWarrior();
		if (blueNew != null) {
			blueNew.location = cities.length + 1;// blue HQ
			hq[1].warriorInHQ.add(blueNew);
			System.out.println(clock + " " + blueNew + " born");
		}
	}

	/**
	 * The method follows the routine of
	 * <ol>
	 * <li>Iterate over HQ and cities from west to east</li>
	 * <li>Iterate over warriors within the location</li>
	 * <li>check if the warrior has been moved in this round</li>
	 * <li>if not, remove it from the current location and move to the next</li>
	 * <li>register that this warrior has been moved this round</li>
	 * </ol>
	 * finally, it checks whether victory has been achieved and announce the
	 * moves in an orderly manner.
	 */
	private void move() {
		ArrayList<Warrior> moved = new ArrayList<Warrior>();
		Iterator<Warrior> it;
		Warrior w;

		it = hq[0].warriorInHQ.iterator();// handle moves from red HQ
		while (it.hasNext()) {
			w = it.next();
			if (moved.contains(w))
				continue;
			w.beforeMove();
			++w.location;
			it.remove();
			cities[0].warriorInCity.addFirst(w);
			moves.add(new MoveMessage(w));
			moved.add(w);
		}

		if (cities.length != 1) {
			it = cities[0].warriorInCity.iterator();// handle city 1;
			while (it.hasNext()) {
				w = it.next();
				if (moved.contains(w))
					continue;
				if (w.getTeam() == Team.red) {
					w.beforeMove();
					++w.location;
					it.remove();
					cities[1].warriorInCity.addFirst(w);
					moves.add(new MoveMessage(w));
					moved.add(w);
				} else {
					w.beforeMove();
					redHQOccupierCount++;
					it.remove();
					--w.location;
					moves.add(new MoveMessage(w));
					moved.add(w);
				}
			}

			for (int i = 1; i < cities.length - 1; ++i) {// handle regular city
				// moves
				it = cities[i].warriorInCity.iterator();
				while (it.hasNext()) {
					w = it.next();
					if (moved.contains(w))
						continue;
					if (w.getTeam() == Team.red) {
						w.beforeMove();
						++w.location;
						moves.add(new MoveMessage(w));
						it.remove();
						cities[i + 1].warriorInCity.addFirst(w);
						moved.add(w);
					} else {
						w.beforeMove();
						--w.location;
						moves.add(new MoveMessage(w));
						it.remove();
						cities[i - 1].warriorInCity.addLast(w);
						moved.add(w);
					}
				}
			}

			it = cities[cities.length - 1].warriorInCity.iterator();// handle
																	// the last
																	// city
			while (it.hasNext()) {
				w = it.next();
				if (moved.contains(w))
					continue;
				if (w.getTeam() == Team.blue) {
					w.beforeMove();
					--w.location;
					moves.add(new MoveMessage(w));
					it.remove();
					cities[cities.length - 2].warriorInCity.addLast(w);
					moved.add(w);
				} else {
					w.beforeMove();
					blueHQOccupierCount++;
					++w.location;
					moves.add(new MoveMessage(w));
					it.remove();
					moved.add(w);
				}
			}
		} else {// if only one city
			it = cities[0].warriorInCity.iterator();
			while (it.hasNext()) {
				w = it.next();
				if (moved.contains(w))
					continue;
				if (w.getTeam() == Team.red) {
					w.beforeMove();
					blueHQOccupierCount++;
					++w.location;
					moves.add(new MoveMessage(w));
					it.remove();
					moved.add(w);
				} else {
					w.beforeMove();
					redHQOccupierCount++;
					--w.location;
					moves.add(new MoveMessage(w));
					it.remove();
					moved.add(w);
				}
			}

		}
		it = hq[1].warriorInHQ.iterator();// handle moves from blue HQ
		while (it.hasNext()) {
			w = it.next();
			w.beforeMove();
			--w.location;
			moves.add(new MoveMessage(w));
			it.remove();
			cities[cities.length - 1].warriorInCity.addLast(w);
		}
		checkVictory();

	}

	class MoveMessage implements Comparable<MoveMessage> {
		public Warrior w;

		MoveMessage(Warrior w) {
			this.w = w;
		}

		@Override
		public int compareTo(MoveMessage other) {
			if (this.w.location != other.w.location)
				return this.w.location - other.w.location;
			else if (w.getTeam() == Team.red)
				return -1;
			else
				return 1;
		}

		@Override
		public String toString() {
			if (w.location != 0 && w.location != cities.length + 1) {
				return clock + " " + w + " marched to city " + w.location + " " + w.getDetails();
			} else {
				if (w.getTeam() == Team.red)
					return (clock + " " + w + " reached blue headquarter " + w.getDetails());
				else
					return (clock + " " + w + " reached red headquarter " + w.getDetails());
			}
		}
	}

	private void announceHQTaken(Team team) {
		System.out.println(clock + " " + team + " headquarter was taken");
	}

	private void checkVictory() {
		moves.sort(new Comparator<MoveMessage>() {
			@Override
			public int compare(MoveMessage o1, MoveMessage o2) {
				return o1.compareTo(o2);
			}
		});
		moves.forEach(System.out::println);
		moves.clear();

		if (redHQOccupierCount == 2) {
			end = true;
			announceHQTaken(Team.red);
		}
		if (blueHQOccupierCount == 2) {
			end = true;
			announceHQTaken(Team.blue);
		}
	}

	private void produceLE() {
		for (int i = 0; i < cities.length; ++i) {
			cities[i].produceLifeElements();
		}
	}

	private void takeLE() {
		for (int i = 0; i < cities.length; ++i) {
			if (cities[i].warriorInCity.size() == 1) {
				int LE = cities[i].takeLifeElements();
				if (cities[i].warriorInCity.get(0).getTeam() == Team.red) {
					hq[0].addLE(LE);
					announceLE(cities[i].warriorInCity.get(0), LE);
				} else {
					hq[1].addLE(LE);
					announceLE(cities[i].warriorInCity.get(0), LE);
				}
			}
		}
	}

	private void announceLE(Warrior w, int LE) {
		System.out.println(clock + " " + w + " earned " + LE + " elements for his headquarter");
	}

	private void combat() {
		for (int i = 0; i < cities.length; ++i) {
			if (cities[i].warriorInCity.size() == 2) {// if there are two
														// warriors in a city
				if (cities[i].flag == Team.none) {
					if (i % 2 == 0)// "city index" odd, red attacks
						attack(cities[i].warriorInCity.getFirst(), cities[i].warriorInCity.getLast(), i);
					else
						attack(cities[i].warriorInCity.getLast(), cities[i].warriorInCity.getFirst(), i);
				}

				else if (cities[i].flag == Team.red) {
					attack(cities[i].warriorInCity.getFirst(), cities[i].warriorInCity.getLast(), i);
				}

				else {
					attack(cities[i].warriorInCity.getLast(), cities[i].warriorInCity.getFirst(), i);
				}
			}
		}
	}

	/**
	 * @param wa
	 *            the attacker
	 * @param wb
	 *            the victim
	 * @param cityIndex
	 *            city index
	 */
	private void attack(Warrior wa, Warrior wb, int cityIndex) {
		try {
			System.out.println(clock + " " + wa + " attacked " + wb + " in city " + (cityIndex + 1) + " with "
					+ wa.getHP() + " elements and force " + wa.getAttackV());
			wa.attack(wb);
			try {
				System.out.println(clock + " " + wb + " fought back against " + wa + " in city " + (cityIndex + 1));
				wb.counter(wa);
			} catch (Cheer c) {
				System.out.println(clock + " " + c + " in city " + (cityIndex + 1));
			}
		} catch (Death d) {
			System.out.println(clock + " " + d + " in city " + (cityIndex + 1));// announce
																				// death
			rewardWarrior(d);
			int LE = hqGetLE(d, cities[cityIndex]);
			announceLE(d.getKiller(), LE);
			cities[cityIndex].warriorInCity.remove(d.getVictim());
			changeFlag(cityIndex, d);
		}
	}

	private void rewardWarrior(Death d) {
		if (d.getVictim().getTeam() == Team.red)
			d.getKiller().addHP(hq[1].rewardLE());
		else
			d.getKiller().addHP(hq[0].rewardLE());
	}

	private int hqGetLE(Death d, City c) {
		int LE = c.takeLifeElements();
		if (d.getKiller().getTeam() == Team.red) {
			hq[0].addLE(LE);
		} else {
			hq[1].addLE(LE);
		}
		return LE;
	}

	private void changeFlag(int cityI, Death d) {
		Team newFlag = Team.none;
		if (cities[cityI].lastKillerTeam == d.getKiller().getTeam()) {
			newFlag = d.getKiller().getTeam();
		} else
			cities[cityI].lastKillerTeam = d.getKiller().getTeam();

		if (newFlag != cities[cityI].flag) {
			cities[cityI].flag = newFlag;
			announceFlag(cityI, newFlag);
		}

	}

	private void announceFlag(int cityI, Team flag) {
		System.out.println(clock + " " + flag + " flag raised in city " + (cityI + 1));
	}

	private void report() {
		System.out.println(clock + " " + hq[0]);
		System.out.println(clock + " " + hq[1]);
	}

	/**
	 * The main function of the game logic.
	 */
	public void run() {
		while (clock.getTime() <= T) {
			switch (clock.getMinute()) {
			case 0:
				spawn();
				break;
			case 10:
				move();
				if (end)
					return;// end game (victory)
				break;
			case 20:
				produceLE();
				break;
			case 30:
				takeLE();
				break;
			case 40:
				combat();
				break;
			case 50:
				report();
			}
			clock.tick();
		}
		return;// end game (time out)
	}

}
