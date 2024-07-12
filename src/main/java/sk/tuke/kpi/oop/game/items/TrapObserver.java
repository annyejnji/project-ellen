package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.messages.Topic;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TrapObserver {
    private final List<Trap> observedTraps = new ArrayList<>();
    public static Topic<String> TRAP_OBSERVER_MESSAGE = Topic.create("*Trap message*", String.class);

    public void addObserver(Trap trap) {
        observedTraps.add(trap);
    }

    public void removeObserver(Trap trap) {
        observedTraps.remove(trap);
    }

    public void notifyTrapActivated(Trap trap) {
        Objects.requireNonNull(trap.getScene()).getMessageBus().publish(TRAP_OBSERVER_MESSAGE, "*TRAP activated*");
    }

    public void checkTraps() {
        for (Trap trap : observedTraps) {
            if (trap.isActivated()) {
                notifyTrapActivated(trap);
            }
            if (!trap.isActivated()) {
                notifyTrapDeactivated(trap);
            }
        }
    }

    public void notifyTrapDeactivated(Trap trap) {
        Objects.requireNonNull(trap.getScene()).getMessageBus().publish(TRAP_OBSERVER_MESSAGE, "*TRAP deactivated");
    }
}
