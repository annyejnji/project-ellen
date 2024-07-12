package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.graphics.Color;

public class PowerSwitch extends AbstractActor {
    private final Switchable device;
    private static final String SWITCH_ANIMATION = "sprites/switch.png";

    public PowerSwitch(Switchable device) {
        this.device = device;
        Animation animation = new Animation(SWITCH_ANIMATION, 16, 16, 0.1f, Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(animation);

        if (device != null && !device.isOn()) {
            getAnimation().setTint(Color.GRAY);
        }
    }

    public Switchable getDevice() {
        return device;
    }

    public void toggle() {
        if (device == null) {
            return;
        }
        if (device.isOn()) {
            switchOff();
        } else {
            switchOn();
        }
    }

    public void switchOn() {
        if (device == null) {
            return;
        }
        getAnimation().setTint(Color.WHITE);
        device.turnOn();

    }

    public void switchOff() {
        if (device == null) {
            return;
        }
        getAnimation().setTint(Color.GRAY);
        device.turnOff();

    }

}
