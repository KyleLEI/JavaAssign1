package world;

import warriors.Warrior;
/**
 * The City class records references to warriors in the city, 
 * how much life elements are in the city and the occupying team.
 * It contains the following information:
 * <ol>
 * <li>reference to warriors in the city, 
 * an {@code array} of {@link Warrior} objects</li>
 * <li>the occupying team, a {@link Team}</li>
 * <li>life elements stored in the city, an {@code int}</li>
 * </ol>
 * @author Kyle Lei
 * @version 1.0.0
 */
public class City {
	private Warrior[] warriors;
	private Team occupier=Team.none;
	private int lifeElements=0;
	
	City(){
		warriors=new Warrior[2];//maximum capacity of a city is two warriors
	}
	
	public void produceLifeElements(){lifeElements+=10;}
	
	/**
	 * A warrior takes all life elements from a city.
	 * @return the amount of life elements taken
	 */
	public int takeLifeElements(){
		int ret=lifeElements;
		lifeElements=0;
		return ret;
	}
}
