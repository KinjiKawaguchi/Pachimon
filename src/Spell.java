package src;
public class Spell {
    private int id;
    private String name;
    private String type;
    private String classification;
    private int power;
    private int accuracy;
    private int pp;
    private boolean direct;
    private String description;
    static int nodeid = 0;
    
    Spell(String name, String type, String classification,int power,int accuracy,int pp, boolean direct,String description) {
        this.id = nodeid;
        this.name = name;
        this.type = type;
        this.classification = classification;
        this.power = power;
        this.accuracy = accuracy;
        this.pp = pp;
        this.direct = direct;
        this.description = description;
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

    public String getClassification() {
        return this.classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public int getPower() {
        return this.power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getAccuracy() {
        return this.accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public int getPp() {
        return this.pp;
    }

    public void setPp(int pp) {
        this.pp = pp;
    }

    public boolean isDirect() {
        return this.direct;
    }

    public void setDirect(boolean direct) {
        this.direct = direct;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}