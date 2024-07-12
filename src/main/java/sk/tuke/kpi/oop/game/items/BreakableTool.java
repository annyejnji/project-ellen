package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.framework.AbstractActor;

import java.util.Objects;

abstract class BreakableTool<A extends Actor> extends AbstractActor implements Usable<A> {
    private int remainingUses;

    public BreakableTool(int remainingUses) {
        this.remainingUses = remainingUses;
    }

    public int getRemainingUses() {
        return remainingUses;
    }

    @Override
    public void useWith(A actor) {
        remainingUses--;
        if (remainingUses == 0) {
            Objects.requireNonNull(getScene()).removeActor(this);
        }
    }

}
