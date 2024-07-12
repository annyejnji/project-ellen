package sk.tuke.kpi.oop.game.weapons;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.characters.Alive;
import sk.tuke.kpi.oop.game.characters.Ripley;

import java.util.Objects;

public class Bullet extends AbstractActor implements Fireable {
    private static final String BULLET_ANIMATION = "sprites/bullet.png";

    public Bullet() {
        Animation bulletAnimation = new Animation(BULLET_ANIMATION, 16, 16);
        setAnimation(bulletAnimation);
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);

        new Loop<>(new Invoke<>(actor -> {
            for (Actor otherActor : scene.getActors()) {
                if (otherActor == actor) {
                    continue;
                }
                if (actor.intersects(otherActor) && (otherActor instanceof Alive) && !(otherActor instanceof Ripley)) {
                    Alive alive = (Alive) otherActor;
                    alive.getHealth().drain(15);
                    scene.removeActor(actor);
                    break;
                }
            }
        })).scheduleFor(this);
    }

    @Override
    public int getSpeed() {
        return 4;
    }

    @Override
    public void startedMoving(Direction direction) {
        Objects.requireNonNull(direction);
        if (direction == Direction.NONE) {
            return;
        }
        getAnimation().setRotation(direction.getAngle());
    }

    @Override
    public void collidedWithWall() {
        Fireable.super.collidedWithWall();

        Objects.requireNonNull(getScene()).removeActor(this);
    }
}
