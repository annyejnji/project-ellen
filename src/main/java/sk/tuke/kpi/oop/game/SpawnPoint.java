package sk.tuke.kpi.oop.game;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.actions.When;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.behaviours.RandomlyMoving;
import sk.tuke.kpi.oop.game.characters.Alien;
import sk.tuke.kpi.oop.game.characters.Ripley;

public class SpawnPoint extends AbstractActor {
    private static final String SPAWN_ANIMATION = "sprites/spawn.png";
    private final int maxNumberOfAliens;
    private int createdAliens;
    private boolean waiting;

    public SpawnPoint(int maxNumberOfAliens) {
        this.maxNumberOfAliens = maxNumberOfAliens;
        this.createdAliens = 0;
        Animation animation = new Animation(SPAWN_ANIMATION);
        setAnimation(animation);
    }


    private void createAlien() {
        if (createdAliens == maxNumberOfAliens || waiting) {
            return;
        }
        Scene scene = getScene();

        if (scene == null) {
            return;
        }
        Ripley ripley = scene.getFirstActorByType(Ripley.class);

        if (ripley == null) {
            return;
        }
        double distance = Math.sqrt(Math.pow(this.getPosX() - ripley.getPosX(), 2) + Math.pow(this.getPosY() - ripley.getPosY(), 2));

        if (distance <= 50) {
            Alien alien = new Alien(100, new RandomlyMoving());
            scene.addActor(alien, this.getPosX(), this.getPosY());
            createdAliens++;
            waiting = true;

            new ActionSequence<>(new Wait<>(3), new Invoke<>(() -> waiting = false)).scheduleFor(this);
        }
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);

        Disposable disposable = new Loop<>(new Invoke<>(this::createAlien)).scheduleFor(this);

        new When<>(() -> createdAliens == maxNumberOfAliens, new Invoke<>(disposable::dispose)).scheduleFor(this);
    }
}


