package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.messages.Topic;

public class Ventilator extends AbstractActor implements Repairable {
    public static final Topic<Ventilator> VENTILATOR_REPAIRED = Topic.create("ventilator repaired", Ventilator.class);
    private static final String VENTILATOR_ANIMATION = "sprites/ventilator.png";
    private final Animation ventilatorAnimation;
    private boolean isRepaired;


    public Ventilator() {
        this.ventilatorAnimation = new Animation(VENTILATOR_ANIMATION, 32, 32, 0.1f, Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(ventilatorAnimation);
        ventilatorAnimation.stop();
        this.isRepaired = false;
    }

    @Override
    public boolean repair() {
        if (!isRepaired) {
            ventilatorAnimation.play();
            isRepaired = true;
            return true;
        }
        return false;
    }
}
