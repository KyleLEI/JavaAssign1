package world;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import warriors.Warrior;

/**
 * The world class handles the core game logic, including moving warriors and
 * combat. It keeps the following information:
 * <ol>
 * <li>the world clock, a {@link Clock} object</li>
 * <li>HP and attack of warrior types in the order of dragon, ninja, iceman,
 * lion and wolf, two {@code array} of 5 {@code int}</li>
 * <li>containers for warriors of the two teams, two {@link LinkedList}</li>
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
	private ArrayList<Warrior> redWarriors;
	private ArrayList<Warrior> blueWarriors;
	private City[] cities;
	private Headquarter[] hq;
	private WarriorType type;
	private int redHQOccupierCount = 0;
	private int blueHQOccupierCount = 0;
	private boolean end = false;

	World(int inLifeElement, int numberOfCities, int endTime) {
		clock = new Clock();
		redWarriors = new ArrayList<Warrior>();
		blueWarriors = new ArrayList<Warrior>();
		cities = new City[numberOfCities];
		for (int i = 0; i < numberOfCities; ++i)
			cities[i] = new City();
		hq = new Headquarter[2];
		hq[0] = new Headquarter(inLifeElement, Team.red);
		hq[1] = new Headquarter(inLifeElement, Team.blue);
		T = endTime;
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
			redWarriors.add(redNew);
			hq[0].warriorInHQ.add(redNew);
			System.out.println(clock + " " + redNew + " born");
		}
		Warrior blueNew = hq[1].spawnWarrior();
		if (blueNew != null) {
			blueNew.location = cities.length + 1;// blue HQ
			blueWarriors.add(blueNew);
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
	 */
	private void move() {
		LinkedList<Warrior> moved = new LinkedList<Warrior>();
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
			cities[0].warriorInCity.add(w);
			announceCityMove(w, w.location);
			moved.add(w);
		}

		if (cities.length != 1) {
			it = cities[0].warriorInCity.iterator();// handle city 1;
			while (it.hasNext() && !end) {
				w = it.next();
				if (moved.contains(w))
					continue;
				if (w.getTeam() == Team.red) {
					w.beforeMove();
					++w.location;
					it.remove();
					cities[1].warriorInCity.add(w);
					announceCityMove(w, w.location);
					moved.add(w);
				} else {
					w.beforeMove();
					redHQOccupierCount++;
					it.remove();
					blueWarriors.remove(w);
					announceHQMove(w, Team.red);
					moved.add(w);
				}
			}
			// if(!end)checkVictory();

			for (int i = 1; i < cities.length - 1; ++i) {// handle regular city
				// moves
				it = cities[i].warriorInCity.iterator();
				while (it.hasNext() && !end) {
					w = it.next();
					if (moved.contains(w))
						continue;
					if (w.getTeam() == Team.red) {
						w.beforeMove();
						++w.location;
						it.remove();
						cities[i + 1].warriorInCity.add(w);
						announceCityMove(w, w.location);
						moved.add(w);
					} else {
						w.beforeMove();
						--w.location;
						it.remove();
						cities[i - 1].warriorInCity.add(w);
						announceCityMove(w, w.location);
						moved.add(w);
					}
				}
			}

			it = cities[cities.length - 1].warriorInCity.iterator();// handle
																	// the last
																	// city
			while (it.hasNext() && !end) {
				w = it.next();
				if (moved.contains(w))
					continue;
				if (w.getTeam() == Team.blue) {
					w.beforeMove();
					--w.location;
					it.remove();
					cities[cities.length - 2].warriorInCity.add(w);
					announceCityMove(w, w.location);
					moved.add(w);
				} else {
					w.beforeMove();
					blueHQOccupierCount++;
					it.remove();
					redWarriors.remove(w);
					announceHQMove(w, Team.blue);

					moved.add(w);
				}
			}
			checkVictory();
		} else {// if only one city
			it = cities[0].warriorInCity.iterator();
			while (it.hasNext() && !end) {
				w = it.next();
				if (moved.contains(w))
					continue;
				if (w.getTeam() == Team.red) {
					w.beforeMove();
					blueHQOccupierCount++;
					it.remove();
					announceHQMove(w, Team.blue);
					// checkVictory();
					moved.add(w);
				} else {
					w.beforeMove();
					redHQOccupierCount++;
					it.remove();
					blueWarriors.remove(w);
					announceHQMove(w, Team.red);

					moved.add(w);
				}
			}
			checkVictory();
		}

		it = hq[1].warriorInHQ.iterator();// handle moves from blue HQ
		while (it.hasNext() && !end) {
			w = it.next();
			w.beforeMove();
			--w.location;
			it.remove();
			cities[cities.length - 1].warriorInCity.add(w);
			announceCityMove(w, w.location);
		}

	}

	private void announceCityMove(Warrior w, int cityIndex) {
		System.out.println(clock + " " + w + " marched to city " + cityIndex + " " + w.getDetails());
	}

	private void announceHQMove(Warrior w, Team HQ) {
		System.out.println(clock + " " + w + " reached " + HQ + " headquarter " + w.getDetails());
	}

	private void announceHQTaken(Team team) {
		System.out.println(clock + " " + team + " headquarter was taken");
	}

	private void checkVictory() {
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
				if (cities[i].warriorInCity.get(0).getTeam() == Team.red)
					hq[0].addLE(cities[i].takeLifeElements());
				else
					hq[1].addLE(cities[i].takeLifeElements());
			}
		}
	}

	private void report() {
		System.out.println(clock + " " + hq[0]);
		System.out.println(clock + " " + hq[1]);
	}

	/**
	 * The main function of the game logic.
	 */
	public void run() {
		while (clock.getTime() < T) {
			switch (clock.getMinute()) {
			case 0:
				spawn();
				break;
			case 10:
				move();
				if (end)
					return;// end game
				break;
			case 20:
				produceLE();
				break;
			case 30:
				takeLE();
				break;
			case 40:
				break;
			case 50:
				report();
			}
			clock.tick();
		}
	}

}
