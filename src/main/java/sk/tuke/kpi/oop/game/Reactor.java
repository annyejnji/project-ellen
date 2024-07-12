package sk.tuke.kpi.oop.game;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.actions.PerpetualReactorHeating;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Reactor extends AbstractActor implements Switchable, Repairable {
    private int temperature;
    private int damage;
    private boolean turnedOn = false;
    private Set<EnergyConsumer> devices;

    private static final int REACTOR_WIDTH = 80;
    private static final int REACTOR_HEIGHT = 80;
    private static final float BASIC_DURATION = 0.1f;
    private static final float HOT_DURATION = 0.05f;
    private static final String NORMAL_ANIMATION = "sprites/reactor_on.png";
    private static final String REACTOR_HOT_ANIMATION = "sprites/reactor_hot.png";
    private static final String REACTOR_BROKEN_ANIMATION = "sprites/reactor_broken.png";
    private static final String REACTOR_TURNED_OFF_ANIMATION = "sprites/reactor.png";
    private static final String REACTOR_EXTINGUISHED_ANIMATION = "sprites/reactor_extinguished.png";
    private final Animation normalAnimation;
    private final Animation reactorHotAnimation;
    private final Animation reactorTurnedOffAnimation;
    private final Animation reactorBrokenAnimation;
    private final Animation reactorExtinguishedAnimation;


    public Reactor() {
        this.normalAnimation = new Animation(NORMAL_ANIMATION, REACTOR_WIDTH, REACTOR_HEIGHT, BASIC_DURATION, Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(normalAnimation);
        this.reactorHotAnimation = new Animation(REACTOR_HOT_ANIMATION, REACTOR_WIDTH, REACTOR_HEIGHT, HOT_DURATION, Animation.PlayMode.LOOP_PINGPONG);
        this.reactorBrokenAnimation = new Animation(REACTOR_BROKEN_ANIMATION, REACTOR_WIDTH, REACTOR_HEIGHT, BASIC_DURATION, Animation.PlayMode.LOOP_PINGPONG);
        this.reactorTurnedOffAnimation = new Animation(REACTOR_TURNED_OFF_ANIMATION, REACTOR_WIDTH, REACTOR_HEIGHT);
        this.reactorExtinguishedAnimation = new Animation(REACTOR_EXTINGUISHED_ANIMATION, REACTOR_WIDTH, REACTOR_HEIGHT);
        this.temperature = 0;
        this.damage = 0;

        devices = new HashSet<>();
        setAnimation(this.reactorTurnedOffAnimation);

    }

    public int getTemperature() {
        return temperature;
    }

    public int getDamage() {
        return damage;
    }

    /*
        int increments = increment;

        if (damage >= 33 && damage <= 66) {
            increments = (int) Math.ceil(increments * 1.5);
        } else if (damage > 66) {
            increments = (int) Math.ceil(increments * 2);
        }
        temperature += increments;
        if (temperature >= 2000) {
            damage = (int) Math.ceil((temperature - 2000) * 0.025);
            updateAnimation();
        }
        if (temperature >= 6000 && damage >= 100) {
            //temperature = 6000;
            damage = 100;
            updateAnimation();
        }
        updateAnimation();
    }*/

    public void increaseTemperature(int increment) {
        if (increment <= 0 || !turnedOn) {
            return;
        }
        int i = increment;

        if (damage >= 33 && damage <= 66) {
            i = (int) Math.ceil(i * 1.5);
        } else if (damage > 66) {
            i = (int) Math.ceil(i * 2);
        }
        temperature = temperature + i;

        if (temperature >= 2000) {
            damage = (int) Math.ceil((temperature - 2000) * 0.025);
            updateAnimation();
        }
        if (temperature >= 6000 && damage >= 100) {
            destroy();
        }
        updateAnimation();
    }

        /*if (damage >= 33 && damage <= 66) {
            temperature = (int) (temperature + Math.ceil(increments * 1.5));
        } else if (damage > 66) {
            temperature = temperature + (2 * increment);
        } else {
            temperature = temperature + increment;
        }

        if (temperature > 2000) {
            int current_temperature = temperature - 2000;
            this.damage = Math.round((float) (100 * current_temperature) / 4000);
        }

        if (temperature >= 6000) {
            this.destroy();
            return;
        }*/

        /*temperature += (int) (increment * (damage > 32 ? (damage < 66 ? 1.5 : 2) : 1));
        if (temperature > 2000) {
            int currentDamage = (int) Math.floor((Math.min(temperature, 6000) - 2000) / (double) 40);
            if (currentDamage > damage) {
                damage = Math.min(currentDamage, 100);
            }
        }


        if (temperature >= 6000) {
            this.destroy();
        } else {
            updateAnimation();
        }*/


    private void destroy() {
        damage = 100;
        turnOff();

        updateAnimation();
    }

    public void decreaseTemperature(int decrement) {
        if (decrement < 0) {
            return;
        }
        if (turnedOn) {
            if (damage <= 50) {
                temperature -= decrement;
            } else if (damage < 100) {
                temperature -= (decrement / 2);
            }
            if (temperature <= 4000) {
                if (temperature < 0) {
                    temperature = 0;
                }
                updateAnimation();
            }
        }
    }

    private void updateAnimation() {
        if (turnedOn) {
            if (temperature <= 4000) {
                setAnimation(this.normalAnimation);
            } else if (temperature < 6000) {
                setAnimation(this.reactorHotAnimation);
            } else {
                setAnimation(this.reactorBrokenAnimation);
            }
        } else {
            if (temperature < 6000) {
                setAnimation(this.reactorTurnedOffAnimation);
            } else {
                setAnimation(this.reactorBrokenAnimation);
            }
        }
    }

    @Override
    public boolean repair() {
        if (damage > 99 || damage <= 0) {
            return false;
        }
        damage -= 50;
        if (damage < 0) {
            damage = 0;
        }
        temperature = (damage * 40) + 2000;
        updateAnimation();
        return true;
    }

    public void turnOn() {
        if (damage < 100) {
            turnedOn = true;
            updateAnimation();

            for (EnergyConsumer energyConsumer : devices) {
                energyConsumer.setPowered(true);
            }
        }
    }

    public void turnOff() {
        turnedOn = false;
        updateAnimation();

        for (EnergyConsumer energyConsumer : devices) {
            energyConsumer.setPowered(false);
        }
    }

    @Override
    public boolean isOn() {
        return turnedOn;
    }

    public boolean isTurnedOn() {
        return turnedOn;
    }

    public boolean extinguish() {
        if (temperature > 4000) {
            temperature = 4000;
            setAnimation(this.reactorExtinguishedAnimation);
            return true;
        }
        return false;
    }

    public void addDevice(EnergyConsumer device) {
        Objects.requireNonNull(device);
        devices.add(device);
        if (turnedOn && damage < 100) {
            device.setPowered(true);
        }
    }

    public void removeDevice(EnergyConsumer device) {
        Objects.requireNonNull(device);
        devices.remove(device);
        device.setPowered(false);
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);

        new PerpetualReactorHeating(1).scheduleFor(this);
    }
}

