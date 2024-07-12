package sk.tuke.kpi.oop.game.weapons;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.graphics.Point;
import sk.tuke.kpi.oop.game.Direction;

import java.util.Objects;

public class FasterFireable implements Fireable {

    private final Fireable fireable;
    private final int multiplier;

    public FasterFireable(Fireable fireable, int multiplier) {
        this.fireable = fireable;
        this.multiplier = multiplier;
    }

    @Override
    public int getSpeed() {
        return fireable.getSpeed() * multiplier;
    }

    @Override
    public void startedMoving(Direction direction) {
        fireable.startedMoving(direction);
    }

    @Override
    public void stoppedMoving() {
        fireable.stoppedMoving();
    }

    @Override
    public void collidedWithWall() {
        fireable.collidedWithWall();
        Objects.requireNonNull(getScene()).removeActor(this);
    }

    @Override
    public int getPosX() {
        return fireable.getPosX();
    }

    @Override
    public int getPosY() {
        return fireable.getPosY();
    }

    @Override
    public int getWidth() {
        return fireable.getWidth();
    }

    @Override
    public int getHeight() {
        return fireable.getHeight();
    }

    @Override
    public @NotNull String getName() {
        return fireable.getName();
    }

    @Override
    public @NotNull Animation getAnimation() {
        return fireable.getAnimation();
    }

    @Override
    public @Nullable Scene getScene() {
        return fireable.getScene();
    }

    @Override
    public @NotNull Point getPosition() {
        return fireable.getPosition();
    }

    @Override
    public void setPosition(int posX, int posY) {
        fireable.setPosition(posX, posY);
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        fireable.addedToScene(scene);
    }

    @Override
    public void removedFromScene(@NotNull Scene scene) {
        fireable.removedFromScene(scene);
    }

    @Override
    public boolean intersects(@NotNull Actor actor) {
        return fireable.intersects(actor);
    }
}
