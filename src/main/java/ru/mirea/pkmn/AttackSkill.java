package ru.mirea.pkmn;

import java.io.Serial;
import java.io.Serializable;

public class AttackSkill implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public AttackSkill() {
    }

    public AttackSkill(String name, String description, String cost, int damage) {
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.damage = damage;
    }

    public AttackSkill(String name, String cost, int damage) {
        this.name = name;
        this.cost = cost;
        this.damage = damage;
        this.description = "";
    }
    private String name;

    public String getName() {
        return name;
    }
    public void setName(String value) {
        name = value;
    }
    private String description;

    public String getDescription() {
        return description;
    }
    public void setDescription(String value) {
        description = value;
    }
    private String cost;

    public String getCost() {
        return cost;
    }
    public void setCost(String value) {
        cost = value;
    }
    private int damage;

    public int getDamage() {
        return damage;
    }
    public void setDamage(int value) {
        damage = value;
    }

    @Override
    public String toString() {
//        return "AttackSkill{" +
//                "name='" + name + '\'' +
//                ", description='" + description + '\'' +
//                ", cost='" + cost + '\'' +
//                ", damage=" + damage +
//                '}';

        return "Name: " + name + ", " +
                "Description: " + (description != null ? description : "no desc") + ", " +
                "Cost: " + cost + ", " +
                "Damage: " + damage;
    }
}
