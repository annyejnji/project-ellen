package sk.tuke.kpi.oop.game.actions;

import org.jetbrains.annotations.Nullable;
import sk.tuke.kpi.gamelib.actions.Action;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Movable;

import java.util.Objects;

public class Move<T extends Movable> implements Action<T> {
    private final Direction direction;
    private float duration = 0;

    private float durationBeforeChange = 0;
    private T actor;
    private boolean isDone = false;
    private boolean isStarted = false;

    public Move(Direction direction, float duration) {
        this.direction = direction;
        this.duration = duration;
        this.durationBeforeChange = duration;
    }

    public Move(Direction direction) {
        this.direction = direction;
    }

    @Override
    public @Nullable T getActor() {
        return actor;
    }

    @Override
    public void setActor(@Nullable T actor) {
        this.actor = actor;
    }

    @Override
    public boolean isDone() {
        return isDone;
    }

    @Override
    public void execute(float deltaTime) {
        if (actor == null || isDone) {
            return;
        }
        if (!isStarted) {
            actor.startedMoving(direction);
            isStarted = true;
        }
        duration -= deltaTime;

        if (duration <= 1e-5) {
            stop();
        } else {
            int dx = actor.getPosX() + actor.getSpeed() * direction.getDx();
            int dy = actor.getPosY() + actor.getSpeed() * direction.getDy();

            actor.setPosition(dx, dy);
        }

        if (Objects.requireNonNull(actor.getScene()).getMap().intersectsWithWall(actor)) {
            actor.setPosition(actor.getPosX() - actor.getSpeed() * direction.getDx(),
                actor.getPosY() - actor.getSpeed() * direction.getDy());

            stop();
            actor.collidedWithWall();
        }
    }

    public void stop() {
        if (actor != null) {
            isDone = true;
            isStarted = false;
            actor.stoppedMoving();
        }
    }

    @Override
    public void reset() {
        actor.stoppedMoving();
        isDone = false;
        isStarted = false;
        duration = durationBeforeChange;
    }

}
