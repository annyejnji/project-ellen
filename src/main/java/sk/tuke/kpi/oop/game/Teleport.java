package sk.tuke.kpi.oop.game;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.characters.Ripley;

import java.awt.*;

public class Teleport extends AbstractActor {
    private Teleport destination;
    private boolean ripleyJustTeleported = false;
    private static final String TELEPORT_ANIMATION = "sprites/lift.png";

    public Teleport() {
        Animation teleportAnimation = new Animation(TELEPORT_ANIMATION);
        setAnimation(teleportAnimation);
    }

    public Teleport(String name) {
        super(name);
        Animation teleportAnimation = new Animation(TELEPORT_ANIMATION);
        setAnimation(teleportAnimation);
    }

    public Teleport(Teleport teleport) {
        this();
        setDestination(teleport);
    }

    public Teleport getDestination() {
        return destination;
    }

    public void setDestination(Teleport destinationTeleport) {
        if (destinationTeleport != null && destinationTeleport != this) {
            this.destination = destinationTeleport;
        }
    }

    private void teleportRipley(Ripley ripley) {
        if (ripley != null) {
            ripley.setPosition(this.getPosX() + (this.getWidth() / 2) - (ripley.getWidth() / 2), this.getPosY() + (this.getHeight() / 2) - (ripley.getHeight() / 2));
            ripleyJustTeleported = true;
        }
    }

    @Override
    public boolean intersects(@NotNull Actor actor) {
        if (actor instanceof Ripley) {
            var myRectangle = new Rectangle(getPosX(), getPosY(), getWidth(), getHeight());
            var actorRectangle = new Rectangle(actor.getPosX(), actor.getPosY(), actor.getWidth(), actor.getHeight());
            return myRectangle.intersects(actorRectangle);
        }
        return super.intersects(actor);
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);

        new Loop<>(new Invoke<>(this::magic)).scheduleFor(this);
    }

    private void magic() {
        Scene scene = this.getScene();
        if (scene == null) {
            return;
        }
        Ripley ripley = scene.getFirstActorByType(Ripley.class);
        if (ripley == null) {
            return;
        }

        if (this.intersects(ripley) && !ripleyJustTeleported && destination != null) {
            this.destination.teleportRipley(ripley);
        }

        if (!this.intersects(ripley)) {
            this.ripleyJustTeleported = false;
        }
    }
}
