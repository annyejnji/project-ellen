package sk.tuke.kpi.oop.game.scenarios;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.Scenario;
import sk.tuke.kpi.oop.game.ChainBomb;
import sk.tuke.kpi.oop.game.Teleport;


public class TrainingGameplay extends Scenario {

    @Override
    public void setupPlay(@NotNull Scene scene) {
//        Map<String, MapMarker> markers = scene.getMap().getMarkers();
//
//        Reactor reactor = new Reactor();
//        MapMarker reactorArea1 = markers.get("reactor-area-1");
//        scene.addActor(reactor, reactorArea1.getPosX(), reactorArea1.getPosY());
//        reactor.turnOn();
//
//        Cooler cooler = new Cooler(reactor);
//        //scene.addActor(cooler, 100, 190);
//        MapMarker coolerArea1 = markers.get("cooler-area-1");
//        scene.addActor(cooler, coolerArea1.getPosX(), coolerArea1.getPosY());
//
//        new ActionSequence<>(
//            new Wait<>(5),
//            new Invoke<>(cooler::turnOn)
//        ).scheduleFor(cooler);
//
//        FireExtinguisher fireExtinguisher = new FireExtinguisher();
//        scene.addActor(fireExtinguisher, 250, 300);
//
//        new When<>(
//            () -> reactor.getTemperature() > 4000,
//            new Invoke<>(() -> {
//                fireExtinguisher.useWith(reactor);
//                cooler.turnOn();
//            })
//        ).scheduleFor(reactor);
//
//        Hammer hammer = new Hammer();
//        scene.addActor(hammer, 250, 250);
//
//        new When<>(
//            () -> reactor.getTemperature() <= 2000 && reactor.getDamage() >= 50,
//            new Invoke<>(() -> hammer.useWith(reactor))
//        ).scheduleFor(reactor);
//
        Teleport teleportA = new Teleport();
        scene.addActor(teleportA, 50, 260);

        Teleport teleportB = new Teleport();
        scene.addActor(teleportB, 275, 260);

        Teleport teleportC = new Teleport();
        scene.addActor(teleportC, 275, 100);
        teleportA.setDestination(teleportB);
        teleportB.setDestination(teleportC);

        float wait = 3;
        ChainBomb b1 = new ChainBomb(wait);
        ChainBomb b2 = new ChainBomb(wait);
        ChainBomb b3 = new ChainBomb(wait);

        scene.addActor(b1, 150, 150);
        scene.addActor(b2, 200, 95);
        scene.addActor(b3, 125, 70);

        b1.activate();
    }

}

