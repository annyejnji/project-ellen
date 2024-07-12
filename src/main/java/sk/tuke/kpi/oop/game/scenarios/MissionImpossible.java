package sk.tuke.kpi.oop.game.scenarios;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.tuke.kpi.gamelib.*;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.messages.MessageBus;
import sk.tuke.kpi.oop.game.Locker;
import sk.tuke.kpi.oop.game.Ventilator;
import sk.tuke.kpi.oop.game.characters.Ripley;
import sk.tuke.kpi.oop.game.controllers.KeeperController;
import sk.tuke.kpi.oop.game.controllers.MovableController;
import sk.tuke.kpi.oop.game.items.AccessCard;
import sk.tuke.kpi.oop.game.items.Energy;
import sk.tuke.kpi.oop.game.openables.Door;
import sk.tuke.kpi.oop.game.openables.LockedDoor;

import java.util.Objects;

public class MissionImpossible implements SceneListener {
    private Ripley ripley;
    private Disposable energyDecreaseDisposable;

    public void sceneInitialized(@NotNull Scene scene) {
        ripley = scene.getFirstActorByType(Ripley.class);
        if (ripley == null) {
            return;
        }

        MovableController movableController = new MovableController(ripley);
        Disposable movableControllerDisposable = scene.getInput().registerListener(movableController);

        KeeperController keeperController = new KeeperController(ripley);
        Disposable keeperControllerDisposable = scene.getInput().registerListener(keeperController);

        scene.follow(ripley);

        scene.getGame().pushActorContainer(ripley.getBackpack());


        // PROBLEM 4.3
        MessageBus messageBus = scene.getMessageBus();
        messageBus.subscribe(Ripley.RIPLEY_DIED, ripley -> {
            movableControllerDisposable.dispose();
            keeperControllerDisposable.dispose();
        });

        messageBus.subscribe(Door.DOOR_OPENED, door -> {
            energyDecreaseDisposable = new Loop<>(new ActionSequence<>(new Wait<>(0.5f), new Invoke<>(ripley::decreaseEnergy))).scheduleFor(ripley);
        });

        scene.getMessageBus().subscribe(Ventilator.VENTILATOR_REPAIRED, ventilator -> {
            if (energyDecreaseDisposable != null) {
                energyDecreaseDisposable.dispose();
            }
        });
    }

    @Override
    public void sceneUpdating(@NotNull Scene scene) {
        Ripley ripley = scene.getFirstActorByType(Ripley.class);
        if (ripley == null) {
            return;
        }
        ripley.showRipleyState();
    }

    public static class Factory implements ActorFactory {

        @Override
        public @Nullable Actor create(@Nullable String type, @Nullable String name) {
            if (name == null) {
                return null;
            }

            switch (name) {
                case "ellen":
                    return new Ripley();
                case "energy":
                    return new Energy();
                case "door":
                    if (Objects.equals(type, "vertical")) {
                        return new LockedDoor(name, Door.Orientation.VERTICAL);
                    } else {
                        return new LockedDoor(name, Door.Orientation.HORIZONTAL);
                    }
                case "access card":
                    return new AccessCard();
                case "locker":
                    return new Locker();
                case "ventilator":
                    return new Ventilator();
                default:
                    return null;
            }
        }
    }
}
