package warriors;
/**
 * This main class tests the functionality of warrior classes.
 *
 * @author  Kyle Lei
 * @version  1.0.0
 */
public class TestWarriors {

	public static void main(String[] args) {
		Warrior b = new Lion(1, 10, 4,"red");
		Warrior a = new Dragon(1, 8, 2,"blue");

		while (true) {
			// attack routine
			try {
				a.attack(b);
				// announce here
				try{b.counter(a);}
				catch(Cheer c){System.out.println(c.toString());}
				// announce here
				b.attack(a);
				// announce here
				try{a.counter(b);}
				catch(Cheer c){System.out.println(c.toString());}
				// announce here
			} catch (Death d) {
				System.out.println(d.toString());
				break;
			}
			System.out.println(a.toString() + ".HP = " + a.getHP());
			System.out.println(b.toString() + ".HP= " + b.getHP());
		}
	}

}
