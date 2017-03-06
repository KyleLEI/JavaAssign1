package warriors;
import world.Team;
/**
 * This main class tests the functionality of warrior classes.
 *
 * @author  Kyle Lei
 * @version  1.0.0
 */
public class TestWarriors {

	public static void main(String[] args) {
		Warrior b = new Wolf(1, 50, 15,Team.red);
		Warrior a = new Dragon(1, 60, 10,Team.blue);

		while (true) {
			// attack routine
			try {
				a.beforeMove();
				b.beforeMove();
				a.attack(b);
				// announce here
				try{b.counter(a);}
				catch(Cheer c){System.out.println(c);}
				// announce here
				b.attack(a);
				// announce here
				try{a.counter(b);}
				catch(Cheer c){System.out.println(c);}
				// announce here
			} catch (Death d) {
				//process death
				System.out.println(d);
				break;
			}
			System.out.println(a + ".HP = " + a.getHP());
			System.out.println(b + ".HP= " + b.getHP());
		}
	}

}
