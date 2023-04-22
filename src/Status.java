package src;

public class Status {
    private int max_hp;
    private int current_hp;
    private int max_attack;
    private int current_attack;
    private int max_defence;
    private int current_defence;
    private int max_spAttack;
    private int current_spAttack;
    private int max_spDefence;
    private int current_spDefence;
    private int max_speed;
    private int current_speed;

    Status(int max_hp,int max_attack,int max_defence,int max_spAttack,int max_spDefence,int max_speed){
        this.max_hp = max_hp;
        this.current_hp = this.max_hp;
        this.max_attack = max_attack;
        this.current_attack = this.max_attack;
        this.max_defence = max_defence;
        this.current_defence = this.max_defence;
        this.max_spAttack = max_spAttack;
        this.current_spAttack = this.max_spAttack;
        this.max_spDefence = max_spDefence;
        this.current_spDefence = this.max_spDefence;
        this.max_speed = max_speed;
        this.current_speed = this.max_speed;
    }
    public int getMax_hp() {
        return this.max_hp;
    }

    public void setMax_hp(int max_hp) {
        this.max_hp = max_hp;
    }

    public int getCurrent_hp() {
        return this.current_hp;
    }

    public void setCurrent_hp(int current_hp) {
        this.current_hp = current_hp;
    }

    public int getMax_attack() {
        return this.max_attack;
    }

    public void setMax_attack(int max_attack) {
        this.max_attack = max_attack;
    }

    public int getCurrent_attack() {
        return this.current_attack;
    }

    public void setCurrent_attack(int current_attack) {
        this.current_attack = current_attack;
    }

    public int getMax_defence() {
        return this.max_defence;
    }

    public void setMax_defence(int max_defence) {
        this.max_defence = max_defence;
    }

    public int getCurrent_defence() {
        return this.current_defence;
    }

    public void setCurrent_defence(int current_defence) {
        this.current_defence = current_defence;
    }

    public int getMax_spAttack() {
        return this.max_spAttack;
    }

    public void setMax_spAttack(int max_spAttack) {
        this.max_spAttack = max_spAttack;
    }

    public int getCurrent_spAttack() {
        return this.current_spAttack;
    }

    public void setCurrent_spAttack(int current_spAttack) {
        this.current_spAttack = current_spAttack;
    }

    public int getMax_spDefence() {
        return this.max_spDefence;
    }

    public void setMax_spDefence(int max_spDefence) {
        this.max_spDefence = max_spDefence;
    }

    public int getCurrent_spDefence() {
        return this.current_spDefence;
    }

    public void setCurrent_spDefence(int current_spDefence) {
        this.current_spDefence = current_spDefence;
    }

    public int getMax_speed() {
        return this.max_speed;
    }

    public void setMax_speed(int max_speed) {
        this.max_speed = max_speed;
    }

    public int getCurrent_speed() {
        return this.current_speed;
    }

    public void setCurrent_speed(int current_speed) {
        this.current_speed = current_speed;
    }
}
