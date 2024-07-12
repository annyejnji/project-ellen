package sk.tuke.kpi.oop.game.openables;

import sk.tuke.kpi.gamelib.Actor;

public class LockedDoor extends Door {

    private boolean locked = true;

    public LockedDoor(String name, Orientation orientation) {
        super(name, orientation);
    }

    public void lock() {
        locked = true;
        close();
    }

    public void unlock() {
        locked = false;
        open();
    }

    public boolean isLocked() {
        return locked;
    }

    @Override
    public void useWith(Actor actor) {
        if (locked) {
            return;
        }
        super.useWith(actor);
    }
}
