package warriors;
/**
 * This class models a cheer of a {@link Dragon}. 
 * It records the reference to the dragon roaring. 
 * It should be thrown if a dragon roars.
 *
 * @author  Kyle Lei
 * @version  1.0.0
 */
@SuppressWarnings("serial")
public class Cheer extends Throwable {
	private Dragon dragon;
	Cheer(Dragon inDragon){
		super();
		dragon=inDragon;
	}
	
	@Override
	public String toString() {
		return dragon.toString() + " roared";
	}
	
}
