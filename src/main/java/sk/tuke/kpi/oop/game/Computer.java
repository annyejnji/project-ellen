package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class Computer extends AbstractActor implements EnergyConsumer {
    private static final String COMPUTER_ANIMATION = "sprites/computer.png";
    private boolean powered;

    public Computer() {
        Animation computerAnimation = new Animation(COMPUTER_ANIMATION, 80, 48, 0.2f, Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(computerAnimation);
        powered = false;
    }

    public int add(int x, int y) {
        if (powered) {
            return x + y;
        } else {
            return 0;
        }
    }

    public float add(float x, float y) {
        if (powered) {
            return x + y;
        } else {
            return 0;
        }
    }

    public int sub(int x, int y) {
        if (powered) {
            return x - y;
        } else {
            return 0;
        }
    }

    public float sub(float x, float y) {
        if (powered) {
            return x - y;
        } else {
            return 0;
        }
    }


    @Override
    public void setPowered(boolean energy) {
        this.powered = energy;
        if (powered) {
            getAnimation().play();
        } else {
            getAnimation().stop();
        }
    }
}
