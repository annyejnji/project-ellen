package sk.tuke.kpi.oop.game.openables;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.messages.Topic;
import sk.tuke.kpi.oop.game.characters.Ripley;

import java.util.Objects;

public class Switcher extends AbstractActor {
    private static final String GREEN_SWITCH_ANIMATION = "sprites/green_switch.png";
    private static final String RED_SWITCH_ANIMATION = "sprites/red_switch.png";
    private final Animation greenswitchAnimation;
    public static Topic<String> HEALTH_IS_NOT_ENOUGH = Topic.create("*You need at least 50 health to do this*", String.class);
    private boolean isGreen;

    public Switcher() {
        Animation redswitchAnimation = new Animation(RED_SWITCH_ANIMATION);
        this.greenswitchAnimation = new Animation(GREEN_SWITCH_ANIMATION);
        setAnimation(redswitchAnimation);
        isGreen = false;
    }

    public void turnGreen(Actor actor) {
        if (!isGreen && actor instanceof Ripley && intersects(actor)) {
            Ripley ripley = (Ripley) actor;
            if (ripley.getHealth().getValue() > 50) {
                isGreen = true;
                setAnimation(this.greenswitchAnimation);
                SwitcherCounter.getInstance().activate();
            } else {
                Objects.requireNonNull(getScene()).getMessageBus().publish(HEALTH_IS_NOT_ENOUGH, "*You need at least 50 health to do this*");
            }
        }
    }
}

