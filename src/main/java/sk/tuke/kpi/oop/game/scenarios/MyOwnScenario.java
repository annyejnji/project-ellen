package sk.tuke.kpi.oop.game.scenarios;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.tuke.kpi.gamelib.*;
import sk.tuke.kpi.gamelib.graphics.Color;
import sk.tuke.kpi.gamelib.graphics.Font;
import sk.tuke.kpi.gamelib.graphics.Overlay;
import sk.tuke.kpi.gamelib.messages.MessageBus;
import sk.tuke.kpi.oop.game.Teleport;
import sk.tuke.kpi.oop.game.atm.ATM;
import sk.tuke.kpi.oop.game.behaviours.Observing;
import sk.tuke.kpi.oop.game.behaviours.RandomlyMoving;
import sk.tuke.kpi.oop.game.characters.Alien;
import sk.tuke.kpi.oop.game.characters.AlienThor;
import sk.tuke.kpi.oop.game.characters.MotherAlien;
import sk.tuke.kpi.oop.game.characters.Ripley;
import sk.tuke.kpi.oop.game.controllers.KeeperController;
import sk.tuke.kpi.oop.game.controllers.MovableController;
import sk.tuke.kpi.oop.game.controllers.ShooterController;
import sk.tuke.kpi.oop.game.items.*;
import sk.tuke.kpi.oop.game.openables.Door;
import sk.tuke.kpi.oop.game.openables.Switcher;
import sk.tuke.kpi.oop.game.openables.SwitcherCounter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

public class MyOwnScenario implements SceneListener {
    private Ripley ripley;
    private TrapObserver trapObserver;


    public void sceneInitialized(@NotNull Scene scene) {
        ripley = scene.getFirstActorByType(Ripley.class);
        if (ripley == null) {
            return;
        }
        trapObserver = new TrapObserver();

        scene.getActors()
            .stream()
            .filter(actor -> actor instanceof Trap)
            .map(actor -> (Trap) actor)
            .forEach(trap -> trap.setObserver(trapObserver));

        Teleport teleport1 = (Teleport) Objects.requireNonNull(scene.getFirstActorByName("teleport1"));
        Teleport teleport2 = (Teleport) Objects.requireNonNull(scene.getFirstActorByName("teleport2"));
        Teleport teleport3 = (Teleport) Objects.requireNonNull(scene.getFirstActorByName("teleport3"));
        Teleport teleport4 = (Teleport) Objects.requireNonNull(scene.getFirstActorByName("teleport4"));

        teleport1.setDestination(teleport2);
        teleport3.setDestination(teleport4);

        /**
         inicializacia objektov mapy
         + nezabudnut doplnit factory
         **/

        MovableController movableController = new MovableController(ripley);
        Disposable movableControllerDisposable = scene.getInput().registerListener(movableController);

        KeeperController keeperController = new KeeperController(ripley);
        Disposable keeperControllerDisposable = scene.getInput().registerListener(keeperController);

        ShooterController shooterController = new ShooterController(ripley);
        Disposable shooterControllerDisposable = scene.getInput().registerListener(shooterController);

        scene.follow(ripley);

        scene.getGame().pushActorContainer(ripley.getBackpack());

        if (trapObserver != null) {
            MessageBus messageBus3 = scene.getMessageBus();
            messageBus3.subscribe(TrapObserver.TRAP_OBSERVER_MESSAGE, message -> {
                Overlay overlay = scene.getGame().getOverlay();

                int screenWidth = scene.getGame().getWindowSetup().getWidth();
                int screenHeight = scene.getGame().getWindowSetup().getHeight();

                int centerX = (screenWidth / 2) - 200;
                int centerY = screenHeight / 2;
                Font font = new Font(15, Color.ORANGE);
                overlay.drawText(message, centerX - 50, centerY, font).showFor(1);
            });
        }
        MessageBus messageBus2 = scene.getMessageBus();
        messageBus2.subscribe(Switcher.HEALTH_IS_NOT_ENOUGH, (message) -> {
            Overlay overlay = scene.getGame().getOverlay();

            int screenWidth = scene.getGame().getWindowSetup().getWidth();
            int screenHeight = scene.getGame().getWindowSetup().getHeight();

            int centerX = (screenWidth / 2) - 200;
            int centerY = screenHeight / 2;
            Font font = new Font(15, Color.SKY);
            overlay.drawText(message, centerX - 50, centerY, font).showFor(3);
        });

        // PROBLEM 4.3
        MessageBus messageBus = scene.getMessageBus();
        messageBus.subscribe(Ripley.RIPLEY_DIED, ripley -> {
            movableControllerDisposable.dispose();
            keeperControllerDisposable.dispose();
            shooterControllerDisposable.dispose();
        });

        int screenWidth = scene.getGame().getWindowSetup().getWidth();
        int screenHeight = scene.getGame().getWindowSetup().getHeight();

        messageBus.subscribe(Door.DOOR_OPENED, door -> {
            if (Objects.equals(door.getName(), "exit door") && SwitcherCounter.getInstance().allActivated()) {
                movableControllerDisposable.dispose();
                keeperControllerDisposable.dispose();
                shooterControllerDisposable.dispose();
                scene.cancelActions(ripley);
                int centerX = screenWidth / 2;
                int centerY = screenHeight / 2;

                Font exitfont = new Font(20, Color.LIME);
                Overlay overlay = scene.getGame().getOverlay();
                overlay.drawText("Good job!", centerX - 50, centerY, exitfont).showFor(10);
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
        public static Map<String, BiFunction<String, String, Actor>> factories;

        static {
            factories = new HashMap<>();
            factories.put("ellen", (type, name) -> new Ripley());
            factories.put("energy", (type, name) -> new Energy());
            factories.put("ammo", (type, name) -> new Ammo());
            factories.put("alien", (type, name) -> createAlien(type));
            factories.put("alienthor", (type, name) -> AlienThor.getInstance());
            factories.put("alien mother", (type, name) -> createMotherAlien(type));
            factories.put("front door", MyOwnScenario.Factory::createDoor);
            factories.put("exit door", MyOwnScenario.Factory::createDoor);
            factories.put("switcher", (type, name) -> {
                SwitcherCounter.getInstance().register();
                return new Switcher();
            });
            factories.put("trap", (type, name) -> new Trap());
            factories.put("atm", (type, name) -> new ATM());
            factories.put("mjolnir", (type, name) -> new Mjolnir());
            factories.put("key", (type, name) -> new AccessCard());
            factories.put("powerup", (type, name) -> new Powerup());
            factories.put("armor", (type, name) -> new Armor());
            factories.put("teleport1", (type, name) -> new Teleport(name));
            factories.put("teleport2", (type, name) -> new Teleport(name));
            factories.put("teleport3", (type, name) -> new Teleport(name));
            factories.put("teleport4", (type, name) -> new Teleport(name));
        }

        @Override
        public @Nullable Actor create(@Nullable String type, @Nullable String name) {
            if (name == null) {
                return null;
            }
            BiFunction<String, String, Actor> factory = factories.get(name);

            if (factory == null) {
                return null;
            }
            return factory.apply(type, name);
        }

        @NotNull
        private static Door createDoor(@Nullable String type, @NotNull String name) {
            Door.Orientation orientation;
            if ("vertical".equals(type)) {
                orientation = Door.Orientation.VERTICAL;
            } else {
                orientation = Door.Orientation.HORIZONTAL;
            }
            return new Door(name, orientation);
        }

        @NotNull
        private static MotherAlien createMotherAlien(@Nullable String type) {
            if (Objects.equals(type, "waiting1")) {
                return new MotherAlien(100, new Observing<>(Door.DOOR_OPENED, door -> door.getName().equals("front door"), new RandomlyMoving()));
            } else {
                return new MotherAlien();
            }
        }

        @NotNull
        private static Alien createAlien(@Nullable String type) {
            if (Objects.equals(type, "running")) {
                return new Alien(100, new RandomlyMoving());
            } else if (Objects.equals(type, "waiting1")) {
                return new Alien(100, new Observing<>(Door.DOOR_OPENED, door -> door.getName().equals("front door"), new RandomlyMoving()));
            } else {
                return new Alien();
            }
        }

    }
}

