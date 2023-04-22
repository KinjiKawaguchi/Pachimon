package src;

public class Pokemon {
    private int id;
    private String name;
    private String type;
    private double xp;
    private int level;
    Spell[] spell;
    Status status;
    Pokemon evolved;
    Pokemon devolved;
    static int nodeid = 0;

    Pokemon(String name,String type,Spell[] spell,Status status,Pokemon evolved,Pokemon devolved){
        this.id = nodeid;
        this.name = name;
        this.type = type;
        this.xp = 0;
        this.level = 1;
        this.spell = spell;
        this.status = status;
        this.evolved = evolved;
        this.devolved = devolved;
        nodeid++;
    }

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getXp() {
		return this.xp;
	}

	public void setXp(double xp) {
		this.xp = xp;
	}

	public int getLevel() {
		return this.level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Spell[] getSpell() {
		return this.spell;
	}

	public void setSpell(Spell[] spell) {
		this.spell = spell;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Pokemon getEvolved() {
		return this.evolved;
	}

	public void setEvolved(Pokemon evolved) {
		this.evolved = evolved;
	}

	public Pokemon getDevolved() {
		return this.devolved;
	}

	public void setDevolved(Pokemon devolved) {
		this.devolved = devolved;
	}
}
