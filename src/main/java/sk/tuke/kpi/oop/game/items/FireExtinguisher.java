package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Reactor;

public class FireExtinguisher extends BreakableTool<Reactor> implements Collectible {
    private static final String FIRE_EXTINGUISHER_ANIMATION = "sprites/extinguisher.png";

    public FireExtinguisher() {
        super(1);
        Animation fireExtinguisherAnimation = new Animation(FIRE_EXTINGUISHER_ANIMATION);
        setAnimation(fireExtinguisherAnimation);
    }

    public void useWith(Reactor reactor) {
        if (reactor == null) {
            return;
        }
        boolean extinguished = reactor.extinguish();

        if (extinguished) {
            super.useWith(reactor);
        }
    }

    @Override
    public Class<Reactor> getUsingActorClass() {
        return Reactor.class;
    }
}
