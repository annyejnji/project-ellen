package sk.tuke.kpi.oop.game;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class Cooler extends AbstractActor implements Switchable {
    private static final String COOLER_ANIMATION = "sprites/fan.png";
    private static final float COOLER_DURATION = 0.2f;
    private static final int COOLER_WIDTH = 32;
    private static final int COOLER_HEIGHT = 32;
    private final Animation coolerAnimation;
    private final Reactor reactor;
    private boolean turnedOn = false;

    public Cooler(Reactor reactor) {
        this.reactor = reactor;
        coolerAnimation = new Animation(COOLER_ANIMATION, COOLER_WIDTH, COOLER_HEIGHT, COOLER_DURATION, Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(coolerAnimation);

        if (!turnedOn) {
            coolerAnimation.stop();
        } else {
            coolerAnimation.play();
        }
    }

    public void turnOn() {
        turnedOn = true;
        coolerAnimation.play();
    }


    public void turnOff() {
        turnedOn = false;
        coolerAnimation.stop();
    }

    public boolean isOn() {
        return turnedOn;
    }

    private void coolReactor() {
        if (turnedOn && reactor != null) {
            reactor.decreaseTemperature(1);
        }
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        new Loop<>(new Invoke<>(this::coolReactor)).scheduleFor(this);
    }
}
