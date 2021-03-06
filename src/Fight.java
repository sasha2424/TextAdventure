import java.util.ArrayList;

public class Fight {

	private Player offender;
	private Player defender;
	private Enemy enemy;

	private boolean PP;

	private ArrayList<Player> spectators;

	private ArrayList<Attack> attacks;

	/*
	 * Player v Player one has to be the offender(person who requests fight)
	 * Player v Enemy player defaults to being the offender although enemy can
	 * just attack
	 */

	public Fight(Player off, Player def) {
		offender = off;
		defender = def;
		off.moveToFight();
		def.moveToFight();
		PP = true;
		spectators = new ArrayList<Player>();
		attacks = new ArrayList<Attack>();
	}

	public Fight(Player off, Enemy e) {
		offender = off;
		enemy = e;
		off.moveToFight();
		e.moveToFight();
		PP = false;
		spectators = new ArrayList<Player>();
		attacks = new ArrayList<Attack>();
	}

	public void tick() {
		for (int i = 0; i < attacks.size(); i++) {
			attacks.get(i).tick();
			if (attacks.get(i).isOver()) {
				if (!attacks.get(i).isOffenders()) {
					offender.takeHit(attacks.get(i));

				} else {
					if (isPlayerOnPlayerFight()) {
						defender.takeHit(attacks.get(i));
					} else {
						enemy.takeHit(attacks.get(i));
					}
				}
				attacks.remove(i);
				i--;
			}
		}
	}

	public boolean isOver() {
		if (offender.isDead()) {
			return true;
		}
		if (isPlayerOnPlayerFight()) {
			if (defender.isDead()) {
				return true;
			}
		} else {
			if (enemy.isDead()) {
				return true;
			}
		}
		return false;
	}

	public boolean isPlayerOnPlayerFight() {
		return PP;
	}

	public boolean isPlayerOnEnemyFight() {
		return !PP;
	}

	public boolean isOffender(String name) {
		return offender.getName().equals(name);
	}

	public void addAttack(Attack a) {
		attacks.add(a);
	}

	public void endFight(Room r) {
		r.removeFight(this);
		if (offender != null) {
			offender.moveOutOfFight();
		}
		if (defender != null) {
			defender.moveOutOfFight();
		}
		if (enemy != null) {
			enemy.moveOutOfFight();
		}
		offender = null;
		defender = null;
		enemy = null;
		spectators.clear();

	}

	public void addSpectator(Player p) {
		spectators.add(p);
	}

	public boolean contains(String name) {
		if (offender.getName().equals(name) || getOpponentName().equals(name)) {
			return true;
		}
		for (Player p : spectators) {
			if (p.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public String getEndString() {
		if (isPlayerOnEnemyFight()) {
			if (offender.getHP() < enemy.getHP()) {
				return "You have lost to a " + enemy.getName();
			} else {
				return "You have defeated a " + enemy.getName();
			}
		} else {
			if (offender.getHP() < defender.getHP()) {
				return "You have lost to " + defender.getName();
			} else {
				return "You have defeated " + defender.getName();
			}
		}

	}

	public boolean containsFighter(String name) {
		if (offender.getName().equals(name) || defender.getName().equals(name) || enemy.getName().equals(name)) {
			return true;
		}

		return false;
	}

	public String getOpponentName() {
		if (enemy == null) {
			return defender.getName();
		}
		return enemy.getName();
	}

	public double getOpponentHP() {
		if (enemy == null) {
			return defender.getHP();
		}
		return enemy.getHP();
	}

	public Player getOffender() {
		return offender;
	}

	public Player getDefender() {
		return defender;
	}

	public Enemy getEnemy() {
		return enemy;
	}

	public ArrayList<Player> getSpectators() {
		return spectators;
	}

	public ArrayList<Attack> getAttacks() {
		return attacks;
	}

}
