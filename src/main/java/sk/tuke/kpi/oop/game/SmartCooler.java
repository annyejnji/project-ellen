package sk.tuke.kpi.oop.game;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.actions.Loop;

public class SmartCooler extends Cooler {
    private final Reactor reactor;

    public SmartCooler(Reactor reactor) {
        super(reactor);
        this.reactor = reactor;
    }

    private void controlCooler() {
        if (reactor == null) {
            return;
        }
        int temperature = reactor.getTemperature();
        if (temperature > 2500 && !isOn()) {
            turnOn();
        } else if (temperature < 1500 && isOn()) {
            turnOff();
        }
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);

        new Loop<>(new Invoke<>(this::controlCooler)).scheduleFor(this);
    }
}
