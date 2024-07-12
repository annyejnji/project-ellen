package sk.tuke.kpi.oop.game.controllers;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.gamelib.Input;
import sk.tuke.kpi.gamelib.KeyboardListener;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.actions.Move;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MovableController implements KeyboardListener {
    private Movable actor;
    private Move<Movable> move;

    private Disposable moveDisposable;
    private Set<Input.Key> keysUsed = new HashSet<>();


    private Map<Input.Key, Direction> keyDirectionMap = Map.ofEntries(
        Map.entry(Input.Key.UP, Direction.NORTH),
        Map.entry(Input.Key.DOWN, Direction.SOUTH),
        Map.entry(Input.Key.RIGHT, Direction.EAST),
        Map.entry(Input.Key.LEFT, Direction.WEST)
    );

    public MovableController(Movable actor) {
        this.actor = actor;
    }

    @Override
    public void keyPressed(@NotNull Input.Key key) {
        if (keyDirectionMap.containsKey(key)) {
            keysUsed.add(key);
            movement();
        }
    }

    @Override
    public void keyReleased(@NotNull Input.Key key) {
        if (keyDirectionMap.containsKey(key)) {
            keysUsed.remove(key);
            movement();
        }
    }

    private void movement() {
        if (move != null) {
            move.stop();
            move = null;
        }

        if (moveDisposable != null) {
            moveDisposable.dispose();
        }

        Direction direction = Direction.NONE;
        for (Input.Key key : keysUsed) {
            direction = direction.combine(keyDirectionMap.get(key));
        }
        if (direction != Direction.NONE) {
            move = new Move<>(direction, Integer.MAX_VALUE);
            moveDisposable = move.scheduleFor(actor);
            //move.scheduleOn(Objects.requireNonNull(actor.getScene())); // TOTO TU JE ZLE, CO S TYM?
        }

    }
}
