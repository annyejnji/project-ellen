package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.characters.Alive;

public class Energy extends AbstractActor implements Usable<Alive> {
    private static final String ENERGY_ANIMATION = "sprites/energy.png";

    public Energy() {
        Animation energyAnimation = new Animation(ENERGY_ANIMATION);
        setAnimation(energyAnimation);
    }

    @Override
    public void useWith(Alive actor) {
        if (actor == null) {
            return;
        }
        if (actor.getHealth().getValue() < 100) {
            actor.getHealth().restore();
        }

        Scene scene = actor.getScene();
        if (scene != null) {
            scene.removeActor(this);
        }
    }

    @Override
    public Class<Alive> getUsingActorClass() {
        return Alive.class;
    }
}
