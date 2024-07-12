package sk.tuke.kpi.oop.game.actions;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.actions.AbstractAction;
import sk.tuke.kpi.oop.game.Keeper;
import sk.tuke.kpi.oop.game.items.Backpack;
import sk.tuke.kpi.oop.game.items.Collectible;

public class Take<K extends Keeper> extends AbstractAction<K> {

    @Override
    public void execute(float deltaTime) {
        K keeper = getActor();
        if (keeper == null) {
            setDone(true);
            return;
        }

        Scene scene = keeper.getScene();
        if (scene == null) {
            setDone(true);
            return;
        }

        Backpack backpack = keeper.getBackpack();
        if (backpack == null) {
            setDone(true);
            return;
        }

        for (Actor actor : scene) {
            if (actor instanceof Collectible && actor.intersects(keeper)) {
                Collectible collectible = (Collectible) actor;

                try {
                    backpack.add(collectible);
                    scene.removeActor(collectible);
                } catch (IllegalStateException ex) {
                    scene.getOverlay().drawText(ex.getMessage(), -200, 10).showFor(2);
                }
                break;
            }
        }

        setDone(true);
    }

}
