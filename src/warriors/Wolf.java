package warriors;
/**
 * This derived class from {@link Warrior} models a wolf. 
 * When the number of enemies who are killed by a wolf’s active attack 
 * reaches an even number(the number of killed enemies counts starting from 1), 
 * the wolf’s HP and attack value double after the battle. 
 * If the killed enemy is a lion, 
 * the HP and attack value double before getting HP from the lion. 
 * The elements rewarded by headquarters come after the doubling process. 
 * Note that doubling only happens when the wolf makes an active attack, 
 * but when it fights back and kills an enemy, this will not happen.
 *
 * @author  Kyle Lei
 * @version  1.0.0
 */
public class Wolf extends Warrior{
	/**
	 * Creates a wolf warrior with the specified ID, HP, and attack.
	 *
	 * @param inID  the ID of the wolf.
	 * @param inHP  the HP of the wolf.
	 * @param inAttackV  the attack value of the wolf.
	 */
	public Wolf(int inID,int inHP,int inAttackV,String inTeam){
		super(inID,inHP,inAttackV,inTeam);
	}
	
	/**
	 * @see warrior.toString
	 */
	@Override
	public String toString(){
		return team+" wolf "+ID;
	}
}
