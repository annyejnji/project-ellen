package sk.tuke.kpi.oop.game.items;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.characters.Alive;
import sk.tuke.kpi.oop.game.characters.Ripley;
import sk.tuke.kpi.oop.game.weapons.Fireable;

import java.util.Objects;

public class Trap extends AbstractActor implements Fireable {
    private final Animation activatedTrapAnimation;
    private final Animation unactivatedTrapAnimation;
    private static final String ACTIVATED_TRAP_ANIMATION = "sprites/atrap.png";
    private static final String UNACTIVATED_TRAP_ANIMATION = "sprites/utrap.png";
    private boolean activated;
    private TrapObserver observer;


    public Trap() {
        this.activatedTrapAnimation = new Animation(ACTIVATED_TRAP_ANIMATION);
        this.unactivatedTrapAnimation = new Animation(UNACTIVATED_TRAP_ANIMATION);
        setAnimation(this.unactivatedTrapAnimation);
        this.activated = false;
    }

    public void setObserver(TrapObserver observer) {
        this.observer = observer;
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        if (observer != null) {
            observer.addObserver(this);
        }

        new Loop<>(new Invoke<>(this::killOnIntersect)).scheduleFor(this);
    }

    private void killOnIntersect() {
        if (!activated) {
            return;
        }
        Scene scene = Objects.requireNonNull(getScene());
        scene.getActors().stream()
            .filter(actor -> (actor instanceof Alive) && !(actor instanceof Ripley) && intersects(actor))
            .forEach(actor -> {
                Alive alive = (Alive) actor;
                alive.getHealth().drain(alive.getHealth().getValue());
            });
    }

    public void checkIntersectionWithRipley(Object actor) {
        if (!activated && actor instanceof Ripley && intersects((Actor) actor)) {
            activateTrap();
            new ActionSequence<Trap>(
                new Wait<>(5f),
                new Invoke<>(this::deactivateTrap)
            ).scheduleFor(this);
        } else if (activated && actor instanceof Ripley && intersects((Actor) actor)) {
            deactivateTrap();
        }
    }

    private void activateTrap() {
        setAnimation(activatedTrapAnimation);
        activated = true;
        if (observer != null) {
            observer.notifyTrapActivated(this);
        }
    }

    private void deactivateTrap() {
        setAnimation(unactivatedTrapAnimation);
        activated = false;
        if (observer != null) {
            observer.notifyTrapDeactivated(this);
        }
    }

    @Override
    public int getSpeed() {
        return 0;
    }

    public boolean isActivated() {
        return activated;
    }

    @Override
    public void removedFromScene(@NotNull Scene scene) {
        super.removedFromScene(scene);
        if (observer != null) {
            observer.removeObserver(this);
        }
    }
}

