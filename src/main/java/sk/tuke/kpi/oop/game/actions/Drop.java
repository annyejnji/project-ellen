package sk.tuke.kpi.oop.game.actions;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.actions.AbstractAction;
import sk.tuke.kpi.oop.game.Keeper;
import sk.tuke.kpi.oop.game.items.Backpack;
import sk.tuke.kpi.oop.game.items.Collectible;

public class Drop<K extends Keeper> extends AbstractAction<K> {

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

        Collectible collectible = backpack.peek();
        if (collectible == null) {
            setDone(true);
            return;
        }
        backpack.remove(collectible);
        scene.addActor(collectible, keeper.getPosX() + collectible.getWidth() / 2, keeper.getPosY() + collectible.getHeight() / 2);
        setDone(true);
    }
}
