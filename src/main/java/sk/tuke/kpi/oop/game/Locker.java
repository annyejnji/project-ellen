package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.items.Hammer;
import sk.tuke.kpi.oop.game.items.Usable;

import java.util.Objects;

public class Locker extends AbstractActor implements Usable<Actor> {
    private static final String LOCKER_ANIMATION = "sprites/locker.png";
    private boolean opened;

    public Locker() {
        Animation lockerAnimation = new Animation(LOCKER_ANIMATION);
        setAnimation(lockerAnimation);
        this.opened = false;
    }

    @Override
    public void useWith(Actor actor) {
        Objects.requireNonNull(actor);

        if (!opened) {
            Objects.requireNonNull(getScene()).removeActor(this);
            getScene().addActor(new Hammer(), getPosX(), getPosY());
            opened = true;
        }
    }

    @Override
    public Class<Actor> getUsingActorClass() {
        return Actor.class;
    }
}
