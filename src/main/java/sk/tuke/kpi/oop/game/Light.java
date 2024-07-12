package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class Light extends AbstractActor implements Switchable, EnergyConsumer {

    private boolean turnedOn = false;
    private boolean hasElectricity = false;
    private final Animation offAnimation;
    private final Animation onAnimation;
    private static final String LIGHT_OFF_ANIMATION = "sprites/light_off.png";
    private static final String LIGHT_ON_ANIMATION = "sprites/light_on.png";


    public Light() {
        this.offAnimation = new Animation(LIGHT_OFF_ANIMATION);
        this.onAnimation = new Animation(LIGHT_ON_ANIMATION);
        setAnimation(offAnimation);
    }

    private void changeAnimation() {
        if (hasElectricity && turnedOn) {
            setAnimation(onAnimation);
        } else {
            setAnimation(offAnimation);
        }
    }

    public void toggle() {
        turnedOn = !turnedOn;
        changeAnimation();
    }

    public void setElectricityFlow(boolean hasElectricity) {
        this.hasElectricity = hasElectricity;
        changeAnimation();
    }

    @Override
    public void turnOn() {
        if (hasElectricity) {
            setAnimation(onAnimation);
        } else {
            setAnimation(offAnimation);

        }
        turnedOn = true;
    }

    @Override
    public void turnOff() {
        setAnimation(offAnimation);
        turnedOn = false;
    }

    @Override
    public boolean isOn() {
        return turnedOn;
    }

    @Override
    public void setPowered(boolean powered) {
        setElectricityFlow(powered);
    }
}
