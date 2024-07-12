package sk.tuke.kpi.oop.game.characters;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.behaviours.Observing;
import sk.tuke.kpi.oop.game.behaviours.RandomlyMoving;
import sk.tuke.kpi.oop.game.items.Backpack;
import sk.tuke.kpi.oop.game.items.Collectible;
import sk.tuke.kpi.oop.game.items.Mjolnir;
import sk.tuke.kpi.oop.game.openables.Door;

import java.util.Objects;

public class AlienThor extends Alien {
    private static final String ALIENTHOR_ANIMATION = "sprites/alienThor.png";
    private static AlienThor instance;

    private AlienThor() {
        super(100, new Observing<>(Door.DOOR_OPENED, door -> door.getName().equals("front door"), new RandomlyMoving()));
        Animation alienThorAnimation = new Animation(ALIENTHOR_ANIMATION, 32, 32, 0.2f);
        setAnimation(alienThorAnimation);
    }

    public static AlienThor getInstance() {
        if (instance == null) {
            instance = new AlienThor();
        }
        return instance;
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);

        new Loop<>(new Invoke<>(this::dieByMjolnir)).scheduleFor(this);
    }

    private void dieByMjolnir() {
        Scene scene = Objects.requireNonNull(getScene());
        scene.getActors().stream()
            .filter(actor -> actor instanceof Ripley && intersects(actor))
            .forEach(actor -> {
                Ripley ripley = (Ripley) actor;
                Backpack backpack = ripley.getBackpack();
                Mjolnir mjolnir = findMjolnir(backpack);

                if (mjolnir != null) {
                    backpack.remove(mjolnir);
//                    this.getHealth().drain(this.getHealth().getValue());
                    scene.removeActor(this);
                    // daj ripleyovej zdravie na maximum
                    ripley.getHealth().restore();
                }
            });
    }

    private Mjolnir findMjolnir(Backpack backpack) {
        if (backpack == null) {
            return null;
        }
        for (Collectible item : backpack.getContent()) {
            if (item instanceof Mjolnir) {
                return (Mjolnir) item;
            }
        }
        return null;
    }

}
