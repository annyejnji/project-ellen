package sk.tuke.kpi.oop.game.openables;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.map.MapTile;
import sk.tuke.kpi.gamelib.messages.Topic;
import sk.tuke.kpi.oop.game.items.Usable;

import java.util.Objects;

public class Door extends AbstractActor implements Openable, Usable<Actor> {
    public enum Orientation {VERTICAL, HORIZONTAL}

    private static final String VERTICAL_DOOR_ANIMATION = "sprites/vdoor.png";
    private static final String HORIZONTAL_DOOR_ANIMATION = "sprites/hdoor.png";
    private boolean isOpen = false;
    private final Animation doorAnimation;
    private final Orientation orientation;


    public static Topic<Door> DOOR_OPENED = Topic.create("door opened", Door.class);
    public static Topic<Door> DOOR_CLOSED = Topic.create("door closed", Door.class);

    public Door(String name, Orientation orientation) {
        super(name);
        this.orientation = orientation;
        if (orientation == Orientation.VERTICAL) {
            this.doorAnimation = new Animation(VERTICAL_DOOR_ANIMATION, 16, 32, 0.1f, Animation.PlayMode.ONCE);
        } else {
            this.doorAnimation = new Animation(HORIZONTAL_DOOR_ANIMATION, 32, 16, 0.1f, Animation.PlayMode.ONCE);
        }
        doorAnimation.stop();
        setAnimation(doorAnimation);
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);

        int tileX = getPosX() / scene.getMap().getTileWidth();
        int tileY = getPosY() / scene.getMap().getTileHeight();

        if (!scene.getMap().getTile(tileX, tileY).isWall()) {
            scene.getMap().getTile(tileX, tileY).setType(MapTile.Type.WALL);
            if (orientation == Orientation.VERTICAL) {
                tileY++;
            } else {
                tileX++;
            }
            scene.getMap().getTile(tileX, tileY).setType(MapTile.Type.WALL);
        }
    }

    @Override
    public void useWith(Actor actor) {
        if (actor == null) {
            return;
        }

        if (isOpen) {
            close();
        } else {
            open();
        }
    }

    @Override
    public Class<Actor> getUsingActorClass() {
        return Actor.class;
    }

    @Override
    public void open() {
        if (!isOpen) {
            doorAnimation.setPlayMode(Animation.PlayMode.ONCE);
            doorAnimation.play();

            int tileX = getPosX() / Objects.requireNonNull(getScene()).getMap().getTileWidth();
            int tileY = getPosY() / Objects.requireNonNull(getScene()).getMap().getTileHeight();

            if (Objects.requireNonNull(getScene()).getMap().getTile(tileX, tileY).isWall()) {
                Objects.requireNonNull(getScene()).getMap().getTile(tileX, tileY).setType(MapTile.Type.CLEAR);
                if (orientation == Orientation.VERTICAL) {
                    tileY++;
                } else {
                    tileX++;
                }
                Objects.requireNonNull(getScene()).getMap().getTile(tileX, tileY).setType(MapTile.Type.CLEAR);

                getScene().getMessageBus().publish(DOOR_OPENED, this);

            }
            isOpen = true;
        }
    }

    @Override
    public void close() {
        if (isOpen) {
            doorAnimation.setPlayMode(Animation.PlayMode.ONCE_REVERSED);
            doorAnimation.play();

            int tileX = getPosX() / Objects.requireNonNull(getScene()).getMap().getTileWidth();
            int tileY = getPosY() / Objects.requireNonNull(getScene()).getMap().getTileHeight();

            if (!Objects.requireNonNull(getScene()).getMap().getTile(tileX, tileY).isWall()) {
                Objects.requireNonNull(getScene()).getMap().getTile(tileX, tileY).setType(MapTile.Type.WALL);
                if (orientation == Orientation.VERTICAL) {
                    tileY++;
                } else {
                    tileX++;
                }
                Objects.requireNonNull(getScene()).getMap().getTile(tileX, tileY).setType(MapTile.Type.WALL);

                getScene().getMessageBus().publish(DOOR_CLOSED, this);

            }
            isOpen = false;
        }
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }
}
