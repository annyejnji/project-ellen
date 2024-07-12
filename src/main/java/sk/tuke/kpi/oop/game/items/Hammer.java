package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Repairable;

public class Hammer extends BreakableTool<Repairable> implements Collectible {
    private static final String HAMMER_ANIMATION = "sprites/hammer.png";

    public Hammer() {
        this(1);
    }

    public Hammer(int remainingUses) {
        super(remainingUses);
        Animation hammerAnimation = new Animation(HAMMER_ANIMATION);
        setAnimation(hammerAnimation);
    }


    @Override
    public Class<Repairable> getUsingActorClass() {
        return Repairable.class;
    }
}

