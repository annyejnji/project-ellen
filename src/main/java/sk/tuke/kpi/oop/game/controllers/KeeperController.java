package sk.tuke.kpi.oop.game.controllers;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Input;
import sk.tuke.kpi.gamelib.KeyboardListener;
import sk.tuke.kpi.oop.game.Keeper;
import sk.tuke.kpi.oop.game.actions.Drop;
import sk.tuke.kpi.oop.game.actions.Shift;
import sk.tuke.kpi.oop.game.actions.Take;
import sk.tuke.kpi.oop.game.actions.Use;
import sk.tuke.kpi.oop.game.atm.ATM;
import sk.tuke.kpi.oop.game.atm.AmmoToHealth;
import sk.tuke.kpi.oop.game.atm.HealthToAmmo;
import sk.tuke.kpi.oop.game.items.Collectible;
import sk.tuke.kpi.oop.game.items.Trap;
import sk.tuke.kpi.oop.game.items.Usable;
import sk.tuke.kpi.oop.game.openables.Switcher;

import java.util.Objects;

public class KeeperController implements KeyboardListener {
    private final Keeper keeper;

    public KeeperController(Keeper keeper) {
        this.keeper = keeper;
    }

    @Override
    public void keyPressed(@NotNull Input.Key key) {
        KeyboardListener.super.keyPressed(key);

        if (key == Input.Key.S) {
            new Shift<>().scheduleFor(keeper);
        } else if (key == Input.Key.ENTER) {
            new Take<>().scheduleFor(keeper);
        } else if (key == Input.Key.BACKSPACE) {
            new Drop<>().scheduleFor(keeper);
        } else if (key == Input.Key.U) {
            handleU();
        } else if (key == Input.Key.B) {
            handleB();
        } else if (key == Input.Key.H) {
            handleH();
        } else if (key == Input.Key.A) {
            handleA();
        }
    }

    private void handleA() {
        for (Actor actor : Objects.requireNonNull(keeper.getScene()).getActors()) {
            if (actor instanceof ATM && keeper.intersects(actor)) {
                ATM atm = (ATM) actor;
                atm.setTransactionStrategy(new HealthToAmmo(10));
                atm.performTransaction();
            }
        }

            /*ATM atm = getATM(keeper.getScene());
            if (atm != null && keeper.intersects(atm)) {
                atm.setTransactionStrategy(new AmmoToHealth(10));
                atm.performTransaction();
            }*/
    }

    private void handleH() {
        for (Actor actor : Objects.requireNonNull(keeper.getScene()).getActors()) {
            if (actor instanceof ATM && keeper.intersects(actor)) {
                ATM atm = (ATM) actor;
                atm.setTransactionStrategy(new AmmoToHealth(10));
                atm.performTransaction();
            }
        }
    }

    private void handleB() {
        Collectible topItem = keeper.getBackpack().peek();
        if (topItem instanceof Usable) {
            Usable<?> usable = (Usable<?>) topItem;
            new Use<>(usable).scheduleForIntersectingWith(keeper);
        }
    }

    private void handleU() {
        for (Actor actor : Objects.requireNonNull(keeper.getScene()).getActors()) {
            if (actor instanceof Usable && keeper.intersects(actor)) {
                Usable<?> usable = (Usable<?>) actor;
                new Use<>(usable).scheduleForIntersectingWith(keeper);
                break;
            }
            if (actor instanceof Trap && keeper.intersects(actor)) {
                Trap trap = (Trap) actor;
                trap.checkIntersectionWithRipley(keeper);
            }
            if (actor instanceof Switcher && keeper.intersects(actor)) {
                Switcher switcher = (Switcher) actor;
                switcher.turnGreen(keeper);
            }
        }
    }
}

