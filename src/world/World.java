package world;

import warriors.Warrior;
import java.util.LinkedList;

/**
 * The world class handles the core game logic, including moving warriors and combat.
 * It keeps the following information:
 * <ol>
 * <li>the world clock, a {@link Clock} object</li>
 * <li>HP and attack of warrior types in the order of 
 * dragon, ninja, iceman, lion and wolf, two {@code array} of 5 {@code int}</li>
 * <li>containers for warriors of the two teams, two {@link LinkedList}</li>
 * <li>container for cities, an {@code array}</li>
 * <li>headquarters of the two team, an {@code array} of {@link Headquarter}</li>
 * </ol>
 *
 * @author  Kyle Lei
 * @version  1.0.0
 */

public class World {
	private Clock clock;
	private int T;
	private int[] HP;
	private int[] attack;
	private LinkedList<Warrior> redWarriors;
	private LinkedList<Warrior> blueWarriors;
	private City[] cities;
	private Headquarter[] hq;
	WarriorType type;
	
	World(int inLifeElement,int numberOfCities,int endTime){
		clock=new Clock();
		redWarriors=new LinkedList<Warrior>();
		blueWarriors=new LinkedList<Warrior>();
		cities=new City[numberOfCities];
		for(int i=0;i<numberOfCities;++i)
			cities[i]=new City();
		hq=new Headquarter[2];
		hq[0]=new Headquarter(Team.red);
		hq[1]=new Headquarter(Team.blue);
		T=endTime;
	}
	public void setHP(int dragon,int ninja,int iceman,int lion, int wolf){
		HP=new int[]{dragon,ninja,iceman,lion,wolf};
	}
	public void setAttack(int dragon,int ninja,int iceman,int lion, int wolf){
		attack=new int[]{dragon,ninja,iceman,lion,wolf};
		type=new WarriorType();
		type.initialize(HP, attack);
		hq[0].initialize(type);
		hq[1].initialize(type);
		
	}
	
	private void spawn(){
		Warrior redNew=hq[0].spawnWarrior();
		redNew.location=0;//red HQ
		redWarriors.add(redNew);
		System.out.println(clock+" "+redNew+" born");
		Warrior blueNew=hq[1].spawnWarrior();
		blueNew.location=cities.length+1;//blue HQ
		blueWarriors.add(blueNew);
		System.out.println(clock+" "+blueNew+" born");
	}
	
	/**
	 * The real main function of the program.
	 */
	public void run(){
		while(clock.getTime()<T){
			switch(clock.getMinute()){
			case 0:
				spawn();
				break;
			case 10:
			case 20:
			case 30:
			case 40:
			case 50:
			}
			clock.tick();
		}
	}
	
}
