package sk.tuke.kpi.oop.game.characters;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Health {
    private int currentHealth;
    private int maximumHealth;
    private List<FatigueEffect> fatigueEffects = new ArrayList<>();
    private boolean fatigued = false;

    public Health(int initialHealth, int maximumHealth) {
        this.currentHealth = initialHealth;
        this.maximumHealth = maximumHealth;
    }

    public Health(int initHealth) {
        this.currentHealth = initHealth;
        this.maximumHealth = initHealth;
    }

    public int getValue() {
        return currentHealth;
    }

    public void refill(int amount) {
        if (currentHealth + amount <= maximumHealth) {
            currentHealth += amount;
        } else {
            restore();
        }
    }

    public void restore() {
        currentHealth = maximumHealth;
    }

    public void drain(int amount) {
        if (currentHealth > 0 && !fatigued) {
            if (currentHealth - amount >= 0) {
                currentHealth -= amount;
            } else {
                currentHealth = 0;
                exhaust();
            }

            if (currentHealth == 0) {
                fatigueEffects.forEach(FatigueEffect::apply);
                fatigued = true;
            }
        }
    }

    public void exhaust() {
        if (currentHealth > 0 && !fatigued) {
            currentHealth = 0;
            fatigueEffects.forEach(FatigueEffect::apply);
            fatigued = true;
        }
    }

    @FunctionalInterface
    public interface FatigueEffect {
        void apply();
    }

    public void onFatigued(FatigueEffect effect) {
        Objects.requireNonNull(effect);
        fatigueEffects.add(effect);
    }

    @Override
    public String toString() {
        return String.format("%d", getValue());
    }
}

