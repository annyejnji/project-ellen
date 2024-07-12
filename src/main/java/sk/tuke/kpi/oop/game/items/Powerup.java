package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.characters.Ripley;

import java.util.Objects;
import java.util.Random;

public class Powerup extends AbstractActor implements Usable<Actor> {

    private static final String POWERUP_ANIMATION = "sprites/bolt.png";
    private final Random random = new Random();


    public Powerup() {
        Animation powerUpAnimation = new Animation(POWERUP_ANIMATION);
        setAnimation(powerUpAnimation);
    }

    @Override
    public void useWith(Actor actor) {
        if (actor instanceof Ripley) {
            int randomChoice = random.nextInt(2);
            if (randomChoice == 0) {
                increaseSpeed((Ripley) actor);
            } else {
                restoreHealth((Ripley) actor);
            }
            Objects.requireNonNull(getScene()).removeActor(this);
        }
    }

    @Override
    public Class<Actor> getUsingActorClass() {
        return Actor.class;
    }

    public void increaseSpeed(Ripley actor) {
        actor.setSpeed(4);
        new ActionSequence<>(
            new Wait<>(5),
            new Invoke<>(() -> {
                actor.setSpeed(2);
            })
        ).scheduleFor(actor);
    }

    public void restoreHealth(Ripley actor) {
        actor.getHealth().restore();
    }

}
