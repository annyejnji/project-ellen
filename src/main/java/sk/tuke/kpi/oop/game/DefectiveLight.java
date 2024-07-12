package sk.tuke.kpi.oop.game;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.actions.Loop;

import java.util.Objects;
import java.util.Random;

public class DefectiveLight extends Light implements Repairable{
    private final Random random;
    private boolean isRepaired;


    public DefectiveLight() {
        super();
        random = new Random();
        isRepaired = false;

    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);

        new Loop<>(new Invoke<>(this::randomToggle)).scheduleFor(this);
    }

    private void randomToggle() {
        if (!isRepaired) {
            int randomNumber = random.nextInt(21);
            if (randomNumber == 1) {
                toggle();
            }
        }
    }

    public boolean repair() {
        if (!isRepaired) {
            isRepaired = true;
            toggle();
            Objects.requireNonNull(getScene()).scheduleAction(new ActionSequence<>(
                new Wait<>(10),
                new Invoke<>(this::resetRepairStatus)
            ));
            return true;
        }
        return false;
    }

    private void resetRepairStatus() {
        isRepaired = false;
    }
}

