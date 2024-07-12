package sk.tuke.kpi.oop.game.characters;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.behaviours.Behaviour;

import java.util.Objects;

public class Alien extends AbstractActor implements Movable, Alive, Enemy {
    private static final String ALIEN_ANIMATION = "sprites/alien.png";
    private Health health;
    private Behaviour<? super Alien> behaviour;

    public Alien() {
        this(100, null);
    }

    public Alien(int healthValue, Behaviour<? super Alien> behaviour) {
        Animation alienAnimation = new Animation(ALIEN_ANIMATION, 32, 32, 0.1f);
        setAnimation(alienAnimation);
        health = new Health(healthValue);
        this.behaviour = behaviour;

        health.onFatigued(() -> Objects.requireNonNull(getScene()).removeActor(this));
    }

    @Override
    public Health getHealth() {
        return health;
    }

    @Override
    public int getSpeed() {
        return 1;
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        if (behaviour != null) {
            behaviour.setUp(this);
        }

        new Loop<>(new ActionSequence<>(new Invoke<>(this::energyDecrease), new Wait<>(0.1f))).scheduleFor(this);
    }

    private void energyDecrease() {
        Objects.requireNonNull(getScene()).getActors().stream()
            .filter(actor -> actor instanceof Alive && !(actor instanceof Enemy) && intersects(actor))
            .forEach(actor -> {
                if (actor instanceof Ripley) {
                    Ripley ripley = (Ripley) actor;
                    if (!ripley.isArmor()) {
                        ((Alive) actor).getHealth().drain(1);
                    }
                } else {
                    ((Alive) actor).getHealth().drain(1);
                }
            });
    }

}
