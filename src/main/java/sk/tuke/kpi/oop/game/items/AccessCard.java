package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.openables.LockedDoor;

import java.util.Objects;

public class AccessCard extends AbstractActor implements Collectible, Usable<LockedDoor> {
    private static final String ACCESCARD_ANIMATION = "sprites/key.png";


    public AccessCard() {
        Animation accessCardAnimation = new Animation(ACCESCARD_ANIMATION);
        setAnimation(accessCardAnimation);
    }

    @Override
    public void useWith(LockedDoor door) {
        Objects.requireNonNull(door);

        if (door.isLocked()) {
            door.unlock();
        }
    }

    @Override
    public Class<LockedDoor> getUsingActorClass() {
        return LockedDoor.class;
    }
}
