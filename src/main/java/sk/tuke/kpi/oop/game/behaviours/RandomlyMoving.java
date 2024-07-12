package sk.tuke.kpi.oop.game.behaviours;

import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.actions.Move;

import java.util.Random;

public class RandomlyMoving implements Behaviour<Movable> {

    private final Random random = new Random();

    @Override
    public void setUp(Movable actor) {
        if (actor == null) {
            return;
        }
        scheduleRandomMovement(actor);
    }

    private void scheduleRandomMovement(Movable actor) {
        Direction[] directions = Direction.values();
        Direction randomDirection = directions[random.nextInt(directions.length)];
        Move<Movable> moveAction = new Move<>(randomDirection, 1.0f);

        new ActionSequence<>(
            moveAction,
            new Wait<>(1),
            new Invoke<>(() -> {
                if (!moveAction.isDone()) {
                    moveAction.stop();
                }
                scheduleRandomMovement(actor);
            })
        ).scheduleFor(actor);
    }
}

