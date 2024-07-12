package sk.tuke.kpi.oop.game.scenarios;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.tuke.kpi.gamelib.*;
import sk.tuke.kpi.gamelib.graphics.Overlay;
import sk.tuke.kpi.gamelib.messages.MessageBus;
import sk.tuke.kpi.oop.game.SpawnPoint;
import sk.tuke.kpi.oop.game.behaviours.Observing;
import sk.tuke.kpi.oop.game.behaviours.RandomlyMoving;
import sk.tuke.kpi.oop.game.characters.Alien;
import sk.tuke.kpi.oop.game.characters.MotherAlien;
import sk.tuke.kpi.oop.game.characters.Ripley;
import sk.tuke.kpi.oop.game.controllers.KeeperController;
import sk.tuke.kpi.oop.game.controllers.MovableController;
import sk.tuke.kpi.oop.game.controllers.ShooterController;
import sk.tuke.kpi.oop.game.openables.Door;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

public class EscapeRoom implements SceneListener {

    private Ripley ripley;

   /* @Override
    public void sceneCreated(@NotNull Scene scene) {
        SceneListener.super.sceneCreated(scene);

        MessageBus messageBus = scene.getMessageBus();
        messageBus.subscribe(World.ACTOR_ADDED_TOPIC, actor -> {
            if (actor instanceof Alien) {
                RandomlyMoving behaviour = new RandomlyMoving();
                behaviour.setUp((Alien) actor);
            }
        });
    }*/

    public void sceneInitialized(@NotNull Scene scene) {
        ripley = scene.getFirstActorByType(Ripley.class);
        if (ripley == null) {
            return;
        }

        MovableController movableController = new MovableController(ripley);
        Disposable movableControllerDisposable = scene.getInput().registerListener(movableController);

        KeeperController keeperController = new KeeperController(ripley);
        Disposable keeperControllerDisposable = scene.getInput().registerListener(keeperController);

        ShooterController shooterController = new ShooterController(ripley);
        Disposable shooterControllerDisposable = scene.getInput().registerListener(shooterController);

        /*Alien alien = new Alien();
        scene.addActor(alien, 150, 100);*/
        SpawnPoint spawnPoint = new SpawnPoint(5);
        scene.addActor(spawnPoint, 150, 100);


        scene.follow(ripley);

        scene.getGame().pushActorContainer(ripley.getBackpack());


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
            if (Objects.equals(door.getName(), "exit door")) {
                movableControllerDisposable.dispose();
                keeperControllerDisposable.dispose();
                shooterControllerDisposable.dispose();
                scene.cancelActions(ripley);
                int centerX = screenWidth / 2;
                int centerY = screenHeight / 2;

                Overlay overlay = scene.getGame().getOverlay();
                overlay.drawText("Good job!", centerX - 50, centerY).showFor(10);
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
//            factories.put("energy", (type, name) -> new Energy());
//            factories.put("ammo", (type, name) -> new Ammo());
//            factories.put("alien", (type, name) -> createAlien(type));
//            factories.put("alien mother", (type, name) -> createMotherAlien(type));
//            factories.put("front door", Factory::createDoor);
//            factories.put("back door", Factory::createDoor);
//            factories.put("exit door", Factory::createDoor);
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
            if (Objects.equals(type, "running")) {
                return new MotherAlien(100, new RandomlyMoving());
            } else if (Objects.equals(type, "waiting1")) {
                return new MotherAlien(100, new Observing<>(Door.DOOR_OPENED, door -> door.getName().equals("front door"), new RandomlyMoving()));
            } else if (Objects.equals(type, "waiting2")) {
                return new MotherAlien(100, new Observing<>(Door.DOOR_OPENED, door -> door.getName().equals("back door"), new RandomlyMoving()));
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
            } else if (Objects.equals(type, "waiting2")) {
                return new Alien(100, new Observing<>(Door.DOOR_OPENED, door -> door.getName().equals("back door"), new RandomlyMoving()));
            } else {
                return new Alien();
            }
        }

    }
}

