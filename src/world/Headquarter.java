package world;
import warriors.*;
/**
 * The Headquarter class stores how many life elements are left in the headquarter.
 * It records the following information:
 * <ol>
 * <li>life elements in the HQ, an {@code int}</li>
 * <li>the team of this HQ, an {@code enum} {@link Team}</li>
 * <li>the spawn sequence of the HQ</li>
 * </ol>
 * @author Kyle Lei
 * @version 1.0.0
 *
 */
public class Headquarter {
	private int lifeElements=0;
	private Team team;
	private int spawnIndex=0;
	private WarriorType type;
	
	Headquarter(Team inTeam){
		team=inTeam;
	}

	/**
	 * Initialize the spawn sequence with initial HP and attack of each type of warrior.
	 * @param  inType initialized WarriorType enum
	 */
	public void initialize(WarriorType inType){
		type=inType;
	}
	/**
	 * Outputs number of elements + team headquarter
	 */
	@Override
	public String toString() {
		return lifeElements+" elements in "+team+" headquarter";
	}
	
	/**
	 * Spawn warrior in specific sequence.
	 * @return the reference to the spawned warrior
	 */
	public Warrior spawnWarrior(){
		Warrior ret=null;
		int ID=spawnIndex/5+1;
		int TypeIndex=spawnIndex%5;
		if(team==Team.red){
			switch(TypeIndex){
			case 0:
				ret=new Iceman(ID,type.getHP(WarriorType.type.ICEMAN),type.geAttack(WarriorType.type.ICEMAN),Team.red);
				break;
			case 1:
				ret=new Lion(ID,type.getHP(WarriorType.type.LION),type.geAttack(WarriorType.type.LION),Team.red);
				break;
			case 2:
				ret=new Wolf(ID,type.getHP(WarriorType.type.WOLF),type.geAttack(WarriorType.type.WOLF),Team.red);
				break;
			case 3:
				ret=new Ninja(ID,type.getHP(WarriorType.type.NINJA),type.geAttack(WarriorType.type.NINJA),Team.red);
				break;
			case 4:
				ret=new Dragon(ID,type.getHP(WarriorType.type.DRAGON),type.geAttack(WarriorType.type.DRAGON),Team.red);
				break;
			}
		}else{
			switch(TypeIndex){
			case 0:
				ret=new Lion(ID,type.getHP(WarriorType.type.LION),type.geAttack(WarriorType.type.LION),Team.blue);
				break;
			case 1:
				ret=new Dragon(ID,type.getHP(WarriorType.type.DRAGON),type.geAttack(WarriorType.type.DRAGON),Team.blue);
				break;
			case 2:
				ret=new Ninja(ID,type.getHP(WarriorType.type.NINJA),type.geAttack(WarriorType.type.NINJA),Team.blue);
				break;
			case 3:
				ret=new Iceman(ID,type.getHP(WarriorType.type.ICEMAN),type.geAttack(WarriorType.type.ICEMAN),Team.blue);
				break;
			case 4:
				ret=new Wolf(ID,type.getHP(WarriorType.type.WOLF),type.geAttack(WarriorType.type.WOLF),Team.blue);
				break;
			}
		}
		++spawnIndex;
		return ret;
	}
}
