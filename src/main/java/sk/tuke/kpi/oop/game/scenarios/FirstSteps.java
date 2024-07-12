package sk.tuke.kpi.oop.game.scenarios;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.GameApplication;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.SceneListener;
import sk.tuke.kpi.gamelib.actions.When;
import sk.tuke.kpi.oop.game.actions.Use;
import sk.tuke.kpi.oop.game.characters.Ripley;
import sk.tuke.kpi.oop.game.controllers.KeeperController;
import sk.tuke.kpi.oop.game.controllers.MovableController;
import sk.tuke.kpi.oop.game.items.Energy;
import sk.tuke.kpi.oop.game.items.FireExtinguisher;
import sk.tuke.kpi.oop.game.items.Hammer;
import sk.tuke.kpi.oop.game.items.Wrench;

public class FirstSteps implements SceneListener {

    @Override
    public void sceneInitialized(@NotNull Scene scene) {
        SceneListener.super.sceneInitialized(scene);
        Ripley ripley = new Ripley();
        int windowHeight = scene.getGame().getWindowSetup().getHeight();
        int yTextPos = windowHeight - GameApplication.STATUS_LINE_OFFSET;

        ripley.getBackpack().add(new Wrench());
        ripley.getBackpack().add(new Wrench());
        ripley.getBackpack().add(new Wrench());
        ripley.getBackpack().add(new Wrench());
        ripley.getBackpack().add(new Wrench());
        ripley.getBackpack().add(new Wrench());
        ripley.getBackpack().add(new Wrench());
        ripley.getBackpack().add(new Hammer());
        ripley.getBackpack().add(new FireExtinguisher());
        ripley.getBackpack().shift();
        scene.getGame().pushActorContainer(ripley.getBackpack());

        MovableController controller = new MovableController(ripley);
        scene.getInput().registerListener(controller);

        KeeperController keeperController = new KeeperController(ripley);
        scene.getInput().registerListener(keeperController);


        scene.addActor(ripley, 0, 0);

        ripley.setEnergy(50);

        Energy energy = new Energy();
        scene.addActor(energy, 100, 100);

        Wrench wrench = new Wrench();
        scene.addActor(wrench, 50, 50);

        Wrench wrench2 = new Wrench();
        scene.addActor(wrench2, 70, 50);

        new When<>(() -> ripley.intersects(energy), new Use<>(energy)).scheduleFor(ripley);
        scene.getGame().getOverlay().drawText("Energy: " + ripley.getEnergy(), 100, yTextPos);

    }
}
