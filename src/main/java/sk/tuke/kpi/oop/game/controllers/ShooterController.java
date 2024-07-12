package sk.tuke.kpi.oop.game.controllers;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Input;
import sk.tuke.kpi.gamelib.KeyboardListener;
import sk.tuke.kpi.oop.game.actions.Fire;
import sk.tuke.kpi.oop.game.characters.Armed;

public class ShooterController implements KeyboardListener {
    private final Armed actor;

    public ShooterController(Armed shooter) {
        this.actor = shooter;
    }

    @Override
    public void keyPressed(@NotNull Input.Key key) {
        if (key == Input.Key.SPACE) {
            shoot();
        }
    }

    public void shoot() {
        if (actor.getFirearm() != null) {
            new Fire<>().scheduleFor(actor);
        }
    }
}

